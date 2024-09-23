package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private UserDto expectedUserDtoCreated;
    private UserDto secondExpectedUserDto;
    private UserDto expectedUserDtoUpdated;
    private long userId;

    @BeforeEach
    void setUp() {
        UserTestObjects userTestObjects = new UserTestObjects();
        userCreateDto = userTestObjects.userCreateDto;
        userUpdateDto = userTestObjects.userUpdateDto;
        expectedUserDtoCreated = userTestObjects.expectedUserDtoCreated;
        secondExpectedUserDto = userTestObjects.secondExpectedUserDto;
        expectedUserDtoUpdated = userTestObjects.expectedUserDtoUpdated;
        userId = userTestObjects.userId;
    }

    @Test
    void saveUserTest() throws Exception {
        Mockito.when(userService.createUser(Mockito.any()))
                .thenReturn(expectedUserDtoCreated);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserDtoCreated.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserDtoCreated.getEmail())));
    }

    @Test
    void updateUserTest() throws Exception {
        Mockito.when(userService.updateUser(eq(userId), Mockito.any()))
                .thenReturn(expectedUserDtoUpdated);

        mvc.perform(patch("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(userUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Shareit-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserDtoUpdated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserDtoUpdated.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserDtoUpdated.getEmail())));
    }

    @Test
    void deleteUserTest() throws Exception {
        mvc.perform(delete("/users/{userId}", userId)
                        .header("X-Shareit-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void allUsersGetTest() throws Exception {
        expectedUserDtoUpdated.setId(2);
        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(expectedUserDtoCreated, secondExpectedUserDto));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Shareit-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(expectedUserDtoCreated.getName())))
                .andExpect(jsonPath("$[1].name", is(secondExpectedUserDto.getName())));
    }

    @Test
    void userGetTestById() throws Exception {
        Mockito.when(userService.getUserById(userId))
                .thenReturn(expectedUserDtoCreated);

        mvc.perform(get("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Shareit-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserDtoCreated.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserDtoCreated.getEmail())));
    }
}
