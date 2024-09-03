package ru.practicum.shareit.booking.handler.booker;

import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.handler.GetAllBookingsHandler;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;

public class GetAllBookersFutureBookings extends GetAllBookingsHandler {
    @Override
    public List<Booking> handle(Long userId, BookingState state, BookingRepository bookingRepository) {
        if (state == BookingState.FUTURE)
            return bookingRepository.findAllByBookerIdAndStartGreaterThan(userId, LocalDateTime.now());
        return handleNext(userId, state, bookingRepository);
    }
}
