package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.config.HeadersConfig;
import ru.practicum.shareit.item.ItemTestObjects;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserTestObjects;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql"}),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:test-data-after.sql"})
})
public class BookingIntegrationTest {
    private final TestRestTemplate testRestTemplate;

    private BookingCreateDto bookingCreateDto;
    private BookingCreateDto secondBookingCreateDto;
    private BookingUpdateDto bookingUpdateDto;
    private BookingDto expectedBookingDtoCreated;
    private BookingDto expectedBookingDtoUpdated;
    private Booking booking;
    private Booking secondBooking;
    private Booking updatedBooking;
    private long bookingId;
    private long userId;
    private User booker;
    private UserDto bookerDto;
    private User owner;
    private Item item;
    private ItemDto itemDto;
    private UserCreateDto bookerCreateDto;
    private UserCreateDto ownerCreateDto;
    private ItemCreateDto itemCreateDto;

    @BeforeEach
    void setUp() {
        BookingTestObjects bookingTestObjects = new BookingTestObjects();
        bookingCreateDto = bookingTestObjects.bookingCreateDto;
        bookingUpdateDto = bookingTestObjects.bookingUpdateDto;

        secondBookingCreateDto = new BookingCreateDto();
        secondBookingCreateDto.setItemId(bookingCreateDto.getItemId());
        secondBookingCreateDto.setStart(bookingUpdateDto.getStart());
        secondBookingCreateDto.setEnd(bookingUpdateDto.getEnd());
        secondBookingCreateDto.setStatus(bookingUpdateDto.getStatus());

        expectedBookingDtoCreated = bookingTestObjects.expectedBookingDtoCreated;
        expectedBookingDtoUpdated = bookingTestObjects.expectedBookingDtoUpdated;
        bookingId = bookingTestObjects.bookingId;
        userId = bookingTestObjects.userId;
        booking = bookingTestObjects.booking;
        secondBooking = bookingTestObjects.secondBooking;
        updatedBooking = bookingTestObjects.updatedBooking;

        UserTestObjects userTestObjects = new UserTestObjects();
        booker = userTestObjects.user;
        owner = userTestObjects.secondUser;
        bookerDto = userTestObjects.expectedUserDtoCreated;
        bookerCreateDto = userTestObjects.userCreateDto;
        ownerCreateDto = new UserCreateDto();
        ownerCreateDto.setEmail(owner.getEmail());
        ownerCreateDto.setName(owner.getName());

        ItemTestObjects itemTestObjects = new ItemTestObjects();
        item = itemTestObjects.item;
        itemDto = itemTestObjects.expectedItemDtoCreated;
        item.setOwner(owner);

        itemCreateDto = itemTestObjects.itemCreateDto;

        booking.setBooker(booker);
        secondBooking.setBooker(booker);
        updatedBooking.setBooker(booker);
        expectedBookingDtoCreated.setBooker(bookerDto);
        expectedBookingDtoUpdated.setBooker(bookerDto);
        booking.setItem(item);
        updatedBooking.setItem(item);
        expectedBookingDtoCreated.setItem(itemDto);
        expectedBookingDtoUpdated.setItem(itemDto);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createBookingFullIntegrationTest() {
        createUser(bookerCreateDto);
        createUser(ownerCreateDto);
        createItem(itemCreateDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set(HeadersConfig.USER_ID, String.valueOf(userId));

        HttpEntity<BookingCreateDto> request = new HttpEntity<>(bookingCreateDto, headers);

        ResponseEntity<BookingDto> response = testRestTemplate.exchange(
                "http://localhost:9090/bookings", HttpMethod.POST, request, BookingDto.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        BookingDto actualItem = response.getBody();
        assertThat(actualItem)
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBookingDtoCreated);
    }


    @Test
    void getBookingByIdIntegrationTest() {
        createUser(bookerCreateDto);
        createUser(ownerCreateDto);
        createItem(itemCreateDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set(HeadersConfig.USER_ID, String.valueOf(userId));

        HttpEntity<BookingCreateDto> request = new HttpEntity<>(bookingCreateDto, headers);

        testRestTemplate.exchange(
                "http://localhost:9090/bookings", HttpMethod.POST, request, BookingDto.class
        );

        request = new HttpEntity<>(headers);

        ResponseEntity<BookingDto> response = testRestTemplate.exchange(
                "http://localhost:9090/bookings/1", HttpMethod.GET, request, BookingDto.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        BookingDto actualBooking = response.getBody();
        BookingDto actualItem = response.getBody();
        assertThat(actualItem)
                .usingRecursiveComparison().ignoringFields("id", "start", "end").isEqualTo(expectedBookingDtoCreated);
    }

    private void createUser(UserCreateDto userCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto, headers);

        testRestTemplate.exchange(
                "http://localhost:9090/users", HttpMethod.POST, request, UserDto.class
        );
    }

    private void createItem(ItemCreateDto itemCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set(HeadersConfig.USER_ID, String.valueOf(userId));
        HttpEntity<ItemCreateDto> request = new HttpEntity<>(itemCreateDto, headers);

        testRestTemplate.exchange(
                "http://localhost:9090/items", HttpMethod.POST, request, UserDto.class
        );
    }
}