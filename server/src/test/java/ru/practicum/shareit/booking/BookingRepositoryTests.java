package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemTestObjects;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserTestObjects;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTests {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private Booking booking;
    private Booking updatedBooking;
    private Booking secondBooking;
    private User booker;
    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        BookingTestObjects bookingTestObjects = new BookingTestObjects();
        booking = bookingTestObjects.booking;
        secondBooking = bookingTestObjects.secondBooking;
        updatedBooking = bookingTestObjects.updatedBooking;

        UserTestObjects userTestObjects = new UserTestObjects();
        booker = userTestObjects.user;
        owner = userTestObjects.secondUser;

        booking.setBooker(booker);
        secondBooking.setBooker(booker);
        updatedBooking.setBooker(booker);

        ItemTestObjects itemTestObjects = new ItemTestObjects();
        item = itemTestObjects.item;
        item.setOwner(owner);

        booking.setItem(item);
        updatedBooking.setItem(item);
        secondBooking.setItem(item);
    }

    @Test
    void saveBookingShouldReturnUserWithId() {
        booker.setId(0);
        userRepository.save(booker);
        owner.setId(0);
        userRepository.save(owner);
        item.setId(0);
        itemRepository.save(item);
        booking.setId(0);
        Booking actualBooking = bookingRepository.save(booking);
        Assertions.assertNotNull(actualBooking);
        Assertions.assertTrue(actualBooking.getId() > 0);
    }

    @Test
    void correctUpdateBooking() {
        booker.setId(0);
        userRepository.save(booker);
        owner.setId(0);
        userRepository.save(owner);
        item.setId(0);
        itemRepository.save(item);
        booking.setId(0);
        booking = bookingRepository.save(booking);
        updatedBooking.setId(booking.getId());
        Booking actualBooking = bookingRepository.save(updatedBooking);

        Assertions.assertNotNull(actualBooking);
        Assertions.assertEquals(updatedBooking.getId(), actualBooking.getId());
        Assertions.assertEquals(updatedBooking.getStart(), actualBooking.getStart());
        Assertions.assertEquals(updatedBooking.getStatus(), actualBooking.getStatus());
    }

    @Test
    void findAllByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualTest() {
        booker.setId(0);
        booker = userRepository.save(booker);
        owner.setId(0);
        userRepository.save(owner);
        item.setId(0);
        itemRepository.save(item);
        booking.setId(0);
        bookingRepository.save(booking);
        secondBooking.setId(0);
        bookingRepository.save(secondBooking);

        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStartLessThanEqualAndEndGreaterThanEqual(
                booker.getId(), LocalDateTime.now());
        Assertions.assertEquals(1, bookings.size());

    }
}
