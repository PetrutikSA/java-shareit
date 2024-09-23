package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapperImpl;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemTestObjects;
import ru.practicum.shareit.item.dto.CommentMapperImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserTestObjects;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.AccessForbiddenException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {BookingServiceImpl.class, ItemMapperImpl.class, CommentMapperImpl.class, BookingMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTests {
    @MockBean
    private final ItemRepository itemRepository;
    @MockBean
    private final UserRepository userRepository;
    @MockBean
    private final ItemRequestRepository itemRequestRepository;
    @MockBean
    private final BookingRepository bookingRepository;

    private final BookingService bookingService;

    private BookingCreateDto bookingCreateDto;
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

    @BeforeEach
    void setUp() {
        BookingTestObjects bookingTestObjects = new BookingTestObjects();
        bookingCreateDto = bookingTestObjects.bookingCreateDto;
        bookingUpdateDto = bookingTestObjects.bookingUpdateDto;
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

        ItemTestObjects itemTestObjects = new ItemTestObjects();
        item = itemTestObjects.item;
        itemDto = itemTestObjects.expectedItemDtoCreated;
        item.setOwner(owner);

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
    void saveBookingItemAvailableExistTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.save(Mockito.any()))
                .thenReturn(booking);

        BookingDto actualBooking = bookingService.createBookingRequest(userId, bookingCreateDto);

        Assertions.assertNotNull(actualBooking);
        Assertions.assertEquals(expectedBookingDtoCreated, actualBooking);
    }

    @Test
    void saveBookingItemNotAvailableExistTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> {
            bookingService.createBookingRequest(userId, bookingCreateDto);
        });
    }

    @Test
    void replyByOwnerTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(bookingRepository.save(Mockito.any()))
                .thenReturn(booking);
        BookingDto actualBooking = bookingService.replyToBookingRequest(owner.getId(), bookingId, true);

        Assertions.assertNotNull(actualBooking);
        Assertions.assertEquals(expectedBookingDtoUpdated, actualBooking);
    }

    @Test
    void replyNotByOwnerTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(bookingRepository.save(Mockito.any()))
                .thenReturn(booking);
        Assertions.assertThrows(AccessForbiddenException.class, () -> {
            bookingService.replyToBookingRequest(userId, bookingId, true);
        });
    }

    @Test
    void getByIdTest() {
        Mockito.when(bookingRepository.findByIdWhereBookerIdOrItemOwnerId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.ofNullable(booking));

        BookingDto actualBooking = bookingService.getBookingById(userId, bookingId);

        Assertions.assertNotNull(actualBooking);
        Assertions.assertEquals(expectedBookingDtoCreated, actualBooking);
    }

    @Test
    void getAllBookersBookingTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(bookingRepository.findAllByBookerId(Mockito.any()))
                .thenReturn(List.of(booking, updatedBooking));

        List<BookingDto> actualBookings = bookingService.getAllBookersBookings(userId, BookingState.ALL);

        Assertions.assertNotNull(actualBookings);
        Assertions.assertEquals(expectedBookingDtoCreated, actualBookings.get(0));
        Assertions.assertEquals(expectedBookingDtoUpdated, actualBookings.get(1));
    }

    @Test
    void getAllOwnBookingTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(bookingRepository.findAllByItemOwnerId(Mockito.any()))
                .thenReturn(List.of(booking, updatedBooking));

        List<BookingDto> actualBookings = bookingService.getAllOwnersBookings(userId, BookingState.ALL);

        Assertions.assertNotNull(actualBookings);
        Assertions.assertEquals(expectedBookingDtoCreated, actualBookings.get(0));
        Assertions.assertEquals(expectedBookingDtoUpdated, actualBookings.get(1));
    }
}
