package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.practicum.shareit.config.HeadersConfig;
import ru.practicum.shareit.user.UserTestObjects;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql"}),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:test-data-after.sql"})
})
class UserIntegrationTests {
    private final TestRestTemplate testRestTemplate;

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
    void contextLoads() {
    }

    @Test
    void createUserFullIntegrationTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto, headers);

        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "http://localhost:9090/users", HttpMethod.POST, request, UserDto.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        UserDto actualUser = response.getBody();
        assertThat(actualUser)
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedUserDtoCreated);
    }

    @Test
    void getAllUsersFullIntegrationTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto, headers);
        UserCreateDto secondUserCreateDto = new UserCreateDto();
        secondUserCreateDto.setEmail(secondExpectedUserDto.getEmail());
        secondUserCreateDto.setName(secondExpectedUserDto.getName());
        HttpEntity<UserCreateDto> secondRequest = new HttpEntity<>(secondUserCreateDto, headers);

        testRestTemplate.exchange(
                "http://localhost:9090/users", HttpMethod.POST, request, UserDto.class
        );

        testRestTemplate.exchange(
                "http://localhost:9090/users", HttpMethod.POST, secondRequest, UserDto.class
        );

        ResponseEntity<List<UserDto>> response = testRestTemplate.exchange(
                "http://localhost:9090/users", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                }
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        List<UserDto> usersList = response.getBody();
        assertThat(usersList.get(0))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedUserDtoCreated);
        assertThat(usersList.get(1))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(secondExpectedUserDto);
    }

    @Test
    void deleteUserFullIntegrationTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set(HeadersConfig.USER_ID, String.valueOf(userId));

        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto, headers);
        ResponseEntity<UserDto> createResponse = testRestTemplate.exchange(
                "http://localhost:9090/users", HttpMethod.POST, request, UserDto.class
        );
        UserDto actualUser = createResponse.getBody();

        ResponseEntity<Void> response = testRestTemplate.exchange(
                "http://localhost:9090/users/" + actualUser.getId(), HttpMethod.DELETE,
                null, Void.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}
