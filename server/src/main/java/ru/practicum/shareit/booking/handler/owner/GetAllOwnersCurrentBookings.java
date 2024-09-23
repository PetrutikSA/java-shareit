package ru.practicum.shareit.booking.handler.owner;

import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.handler.GetAllBookingsHandler;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;

public class GetAllOwnersCurrentBookings extends GetAllBookingsHandler {
    @Override
    public List<Booking> handle(Long userId, BookingState state, BookingRepository bookingRepository) {
        if (state == BookingState.CURRENT)
            return bookingRepository.findAllByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqual(
                    userId,
                    LocalDateTime.now()
            );
        return handleNext(userId, state, bookingRepository);
    }
}
