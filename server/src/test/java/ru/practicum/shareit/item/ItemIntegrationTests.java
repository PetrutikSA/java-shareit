package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingDatesDto;
import ru.practicum.shareit.item.model.Item;
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
public class ItemIntegrationTests {
    private final TestRestTemplate testRestTemplate;

    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private ItemDto expectedItemDtoCreated;
    private ItemDto expectedItemDtoUpdated;
    private Item item;
    private Item secondItem;
    private Item updatedItem;
    private long itemId;
    private long userId;
    private ItemWithNearestBookingDatesDto expectedItemDtoWithBookingsDatesCreated;
    private ItemWithNearestBookingDatesDto secondExpectedItemDtoWithBookingsDates;
    private User user;
    private UserCreateDto userCreateDto;
    private

    @BeforeEach
    void setUp() {
        ItemTestObjects itemTestObjects = new ItemTestObjects();
        itemCreateDto = itemTestObjects.itemCreateDto;
        itemUpdateDto = itemTestObjects.itemUpdateDto;
        expectedItemDtoCreated = itemTestObjects.expectedItemDtoCreated;
        expectedItemDtoUpdated = itemTestObjects.expectedItemDtoUpdated;
        itemId = itemTestObjects.itemId;
        userId = itemTestObjects.userId;
        expectedItemDtoWithBookingsDatesCreated = itemTestObjects.expectedItemDtoWithBookingsDatesCreated;
        secondExpectedItemDtoWithBookingsDates = itemTestObjects.secondExpectedItemDtoWithBookingsDates;
        item = itemTestObjects.item;
        secondItem = itemTestObjects.secondItem;
        updatedItem = itemTestObjects.updatedItem;

        UserTestObjects userTestObjects = new UserTestObjects();
        user = userTestObjects.user;
        userCreateDto = userTestObjects.userCreateDto;
        item.setOwner(user);
        secondItem.setOwner(user);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createUserFullIntegrationTest() {
        createUser();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set(HeadersConfig.USER_ID, String.valueOf(userId));

        HttpEntity<ItemCreateDto> request = new HttpEntity<>(itemCreateDto, headers);

        ResponseEntity<ItemDto> response = testRestTemplate.exchange(
                "http://localhost:9090/items", HttpMethod.POST, request, ItemDto.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        ItemDto actualItem = response.getBody();
        assertThat(actualItem)
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedItemDtoCreated);
    }

    @Test
    void getAllUsersFullIntegrationTest(){
        createUser();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set(HeadersConfig.USER_ID, String.valueOf(userId));

        HttpEntity<ItemCreateDto> request = new HttpEntity<>(itemCreateDto, headers);

        ItemCreateDto secondItemCreateDto = new ItemCreateDto();
        secondItemCreateDto.setDescription(secondExpectedItemDtoWithBookingsDates.getDescription());
        secondItemCreateDto.setName(secondExpectedItemDtoWithBookingsDates.getName());
        secondItemCreateDto.setAvailable(secondExpectedItemDtoWithBookingsDates.getAvailable());

        HttpEntity<ItemCreateDto> secondRequest = new HttpEntity<>(secondItemCreateDto, headers);

        testRestTemplate.exchange(
                "http://localhost:9090/items", HttpMethod.POST, request, ItemDto.class
        );

        testRestTemplate.exchange(
                "http://localhost:9090/items", HttpMethod.POST, secondRequest, ItemDto.class
        );

        request = new HttpEntity<>(headers);

        ResponseEntity<List<ItemWithNearestBookingDatesDto>> response = testRestTemplate.exchange(
                "http://localhost:9090/items", HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                }
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        List<ItemWithNearestBookingDatesDto> usersList = response.getBody();
        assertThat(usersList.get(0))
                .usingRecursiveComparison().ignoringFields("id", "comments")
                .isEqualTo(expectedItemDtoWithBookingsDatesCreated);
        assertThat(usersList.get(1))
                .usingRecursiveComparison().ignoringFields("id","comments")
                .isEqualTo(secondExpectedItemDtoWithBookingsDates);
    }

    @Test
    void deleteUserFullIntegrationTest(){
        createUser();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set(HeadersConfig.USER_ID, String.valueOf(userId));

        HttpEntity<ItemCreateDto> createRequest = new HttpEntity<>(itemCreateDto, headers);

        ResponseEntity<ItemDto> createResponse = testRestTemplate.exchange(
                "http://localhost:9090/items", HttpMethod.POST, createRequest, ItemDto.class
        );
        ItemDto actualItem = createResponse.getBody();

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "http://localhost:9090/items/" + actualItem.getId(), HttpMethod.DELETE,
                request, Void.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    private void createUser(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto, headers);

        testRestTemplate.exchange(
                "http://localhost:9090/users", HttpMethod.POST, request, UserDto.class
        );
    }
}
