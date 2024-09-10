package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.handler.GetAllBookingsHandler;
import ru.practicum.shareit.booking.handler.booker.GetAllBookersBookings;
import ru.practicum.shareit.booking.handler.booker.GetAllBookersByStatusBookings;
import ru.practicum.shareit.booking.handler.booker.GetAllBookersCurrentBookings;
import ru.practicum.shareit.booking.handler.booker.GetAllBookersFutureBookings;
import ru.practicum.shareit.booking.handler.booker.GetAllBookersPastBookings;
import ru.practicum.shareit.booking.handler.owner.GetAllOwnersBookings;
import ru.practicum.shareit.booking.handler.owner.GetAllOwnersByStatusBookings;
import ru.practicum.shareit.booking.handler.owner.GetAllOwnersCurrentBookings;
import ru.practicum.shareit.booking.handler.owner.GetAllOwnersFutureBookings;
import ru.practicum.shareit.booking.handler.owner.GetAllOwnersPastBookings;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.AccessForbiddenException;
import ru.practicum.shareit.util.exception.BadRequestException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto createBookingRequest(Long userId, BookingCreateDto bookingCreateDto) {
        User booker = getUserFromRepository(userId);
        bookingCreateDto.setStatus(BookingStatus.WAITING);
        long itemId = bookingCreateDto.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId, Item.class));
        if (!item.getAvailable()) {
            throw new BadRequestException("Item is unavailable, couldn't be booked");
        }
        if (bookingCreateDto.getStart().isEqual(bookingCreateDto.getEnd()) ||
                bookingCreateDto.getStart().isAfter(bookingCreateDto.getEnd())) {
            throw new BadRequestException("End time should be after start time");
        }
        Booking booking = bookingMapper.bookingCreateToBooking(bookingCreateDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking = bookingRepository.save(booking);
        return bookingMapper.bookingToBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto replyToBookingRequest(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(bookingId, Booking.class));
        User owner = booking.getItem().getOwner();
        if (owner.getId() != userId)
            throw new AccessForbiddenException("Replying to booking could be performed only by item's owner");
        BookingStatus status = (approved) ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return bookingMapper.bookingToBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findByIdWhereBookerIdOrItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new NotFoundException(bookingId, Booking.class));
        return bookingMapper.bookingToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookersBookings(Long userId, BookingState state) {
        getUserFromRepository(userId); //check existence
        GetAllBookingsHandler getAllBookingsHandler = GetAllBookingsHandler.link(
                new GetAllBookersBookings(),
                new GetAllBookersCurrentBookings(),
                new GetAllBookersPastBookings(),
                new GetAllBookersFutureBookings(),
                new GetAllBookersByStatusBookings()
        );
        List<Booking> bookings = getAllBookingsHandler.handle(userId, state, bookingRepository);
        return bookings.stream()
                .map(bookingMapper::bookingToBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getAllOwnersBookings(Long userId, BookingState state) {
        getUserFromRepository(userId); //check existence
        GetAllBookingsHandler getAllBookingsHandler = GetAllBookingsHandler.link(
                new GetAllOwnersBookings(),
                new GetAllOwnersCurrentBookings(),
                new GetAllOwnersPastBookings(),
                new GetAllOwnersFutureBookings(),
                new GetAllOwnersByStatusBookings()
        );
        List<Booking> bookings = getAllBookingsHandler.handle(userId, state, bookingRepository);
        return bookings.stream()
                .map(bookingMapper::bookingToBookingDto)
                .toList();
    }

    private User getUserFromRepository(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, User.class));
    }
}
