package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerValidationTests {
    @MockBean
    private UserClient userClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private String userCreatedDtoJson;
    private long userId;

    @BeforeEach
    void setUp() {
        UserTestObjects userTestObjects = new UserTestObjects();
        userCreateDto = userTestObjects.userCreateDto;
        userUpdateDto = userTestObjects.userUpdateDto;
        userCreatedDtoJson = userTestObjects.userCreatedDtoJson;
        userId = userTestObjects.userId;
    }

    @Test
    void saveUserTest() throws Exception {
        Mockito.when(userClient.createUser(Mockito.any()))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(userCreateDto), HttpStatus.OK));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userCreateDto.getName())))
                .andExpect(jsonPath("$.email", is(userCreateDto.getEmail())));
    }

    @Test
    void updateUserTest() throws Exception {
        Mockito.when(userClient.updateUser(eq(userId), Mockito.any()))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(userUpdateDto), HttpStatus.OK));

        mvc.perform(patch("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(userUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Shareit-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userUpdateDto.getName())))
                .andExpect(jsonPath("$.email", is(userUpdateDto.getEmail())));
    }

    @Test
    void saveUserNotCorrectEmailTest() throws Exception {
        userCreateDto.setEmail("qqqqqqqqqq");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveBlankEmailTest() throws Exception {
        userCreateDto.setEmail("     ");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveUserNullNameTest() throws Exception {
        userCreateDto.setName(null);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateNullNameCorrect() throws Exception {
        userCreateDto.setEmail(userUpdateDto.getEmail());
        userUpdateDto.setName(null);
        Mockito.when(userClient.updateUser(eq(userId), Mockito.any()))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(userCreateDto), HttpStatus.OK));

        mvc.perform(patch("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(userUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Shareit-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userCreateDto.getName())))
                .andExpect(jsonPath("$.email", is(userCreateDto.getEmail())));
    }

    @Test
    void updateNotCorrectEmail() throws Exception {
        userUpdateDto.setEmail("qqqqqqqqqq");
        mvc.perform(patch("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(userUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Shareit-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void allUsersGetTest() throws Exception {
        Mockito.when(userClient.getAllUsers())
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(List.of(userCreateDto, userUpdateDto)),
                        HttpStatus.OK));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Shareit-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(userCreateDto.getName())))
                .andExpect(jsonPath("$[1].name", is(userUpdateDto.getName())));
    }

    @Test
    void userGetTestById() throws Exception {
        Mockito.when(userClient.getUserById(userId))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(userCreateDto), HttpStatus.OK));

        mvc.perform(get("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Shareit-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userCreateDto.getName())))
                .andExpect(jsonPath("$.email", is(userCreateDto.getEmail())));
    }
}
