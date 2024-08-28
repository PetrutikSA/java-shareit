package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{
    @Override
    public BookingDto createBookingRequest(BookingCreateDto bookingCreateDto) {
        return null;
    }

    @Override
    public BookingDto replyToBookingRequest(Long userId, Long bookingId, boolean approved) {
        return null;
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getAllBookersBookings(Long userId, BookingState state) {
        return null;
    }

    @Override
    public List<BookingDto> getAllOwnersBookings(Long userId, BookingState state) {
        return null;
    }
}
