package ru.practicum.shareit.booking.handler.booker;

import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.handler.GetAllBookingsHandler;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public class GetAllBookersByStatusBookings extends GetAllBookingsHandler {
    @Override
    public List<Booking> handle(Long userId, BookingState state, BookingRepository bookingRepository) {
        if (state == BookingState.WAITING || state == BookingState.REJECTED)
            return bookingRepository.findAllByBookerIdAndStatus(
                    userId,
                    BookingStatus.valueOf(state.toString()));
        return handleNext(userId, state, bookingRepository);
    }
}
