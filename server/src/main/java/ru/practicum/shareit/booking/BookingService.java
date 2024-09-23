package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto createBookingRequest(Long userId, BookingCreateDto bookingCreateDto);

    BookingDto replyToBookingRequest(Long userId, Long bookingId, boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getAllBookersBookings(Long userId, BookingState state);

    List<BookingDto> getAllOwnersBookings(Long userId, BookingState state);
}