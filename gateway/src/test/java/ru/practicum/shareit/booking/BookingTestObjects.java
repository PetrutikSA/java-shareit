package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;

import java.time.LocalDateTime;

public class BookingTestObjects {
    public BookingCreateDto bookingCreateDto;
    public BookingUpdateDto bookingUpdateDto;
    public long bookingId = 1L;
    public long secondBookingId = 2L;
    public long userId = 1L;
    public long itemId = 1L;

    public BookingTestObjects() {
        bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(itemId);
        bookingCreateDto.setStart(LocalDateTime.now().plusHours(1));
        bookingCreateDto.setEnd(LocalDateTime.now().plusHours(5));
        bookingCreateDto.setStatus(BookingStatus.WAITING);

        bookingUpdateDto = new BookingUpdateDto();
        bookingUpdateDto.setStart(bookingCreateDto.getStart());
        bookingUpdateDto.setEnd(bookingCreateDto.getEnd());
        bookingUpdateDto.setStatus(BookingStatus.APPROVED);
    }
}
