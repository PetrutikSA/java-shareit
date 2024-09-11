package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserTestObjects;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql"}),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:test-data-after.sql"})
})
public class ItemRequestIntegrationTests {
    private final TestRestTemplate testRestTemplate;

    private ItemRequestCreateDto itemRequestCreateDto;
    private ItemRequestDto expectedItemRequestDtoCreated;
    private long userId;
    private ItemRequest itemRequest;
    private User user;
    private UserCreateDto userCreateDto;

    @BeforeEach
    void setUp() {
        ItemRequestTestObjects itemRequestTestObjects = new ItemRequestTestObjects();
        itemRequestCreateDto = itemRequestTestObjects.itemRequestCreateDto;
        expectedItemRequestDtoCreated = itemRequestTestObjects.expectedItemRequestDtoCreated;
        itemRequest = itemRequestTestObjects.itemRequest;

        UserTestObjects userTestObjects = new UserTestObjects();
        user = userTestObjects.user;
        userCreateDto = userTestObjects.userCreateDto;
        itemRequest.setRequester(user);
        userId = 1L;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createItemFullIntegrationTest() {
        createUser();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set(HeadersConfig.USER_ID, String.valueOf(userId));

        HttpEntity<ItemRequestCreateDto> request = new HttpEntity<>(itemRequestCreateDto, headers);

        ResponseEntity<ItemRequestDto> response = testRestTemplate.exchange(
                "http://localhost:9090/requests", HttpMethod.POST, request, ItemRequestDto.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        ItemRequestDto actualItem = response.getBody();
        assertThat(actualItem)
                .usingRecursiveComparison().ignoringFields("id", "created").isEqualTo(expectedItemRequestDtoCreated);
    }

    @Test
    void getAllItemsRequestsFullIntegrationTest() {
        createUser();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set(HeadersConfig.USER_ID, String.valueOf(userId));

        HttpEntity<ItemRequestCreateDto> request = new HttpEntity<>(itemRequestCreateDto, headers);

        ItemRequestCreateDto secondItemRequestCreateDto = new ItemRequestCreateDto();
        secondItemRequestCreateDto.setDescription("second");
        secondItemRequestCreateDto.setName("second name");

        HttpEntity<ItemRequestCreateDto> secondRequest = new HttpEntity<>(secondItemRequestCreateDto, headers);

        testRestTemplate.exchange(
                "http://localhost:9090/requests", HttpMethod.POST, request, ItemDto.class
        );

        testRestTemplate.exchange(
                "http://localhost:9090/requests", HttpMethod.POST, secondRequest, ItemDto.class
        );

        request = new HttpEntity<>(headers);

        ResponseEntity<List<ItemRequestDto>> response = testRestTemplate.exchange(
                "http://localhost:9090/requests", HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                }
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        List<ItemRequestDto> usersList = response.getBody();
        assertThat(usersList).hasSize(2);
    }

    private void createUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto, headers);

        testRestTemplate.exchange(
                "http://localhost:9090/users", HttpMethod.POST, request, UserDto.class
        );
    }
}
