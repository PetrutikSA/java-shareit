package ru.practicum.shareit.booking.handler.owner;

import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.handler.GetAllBookingsHandler;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public class GetAllOwnersBookings extends GetAllBookingsHandler {
    @Override
    public List<Booking> handle(Long userId, BookingState state, BookingRepository bookingRepository) {
        if (state == BookingState.ALL)
            return bookingRepository.findAllByItemOwnerId(userId);
        return handleNext(userId, state, bookingRepository);
    }
}
