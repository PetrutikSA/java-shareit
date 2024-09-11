package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

public class BookingTestObjects {
    public BookingCreateDto bookingCreateDto;
    public BookingUpdateDto bookingUpdateDto;
    public BookingDto expectedBookingDtoCreated;
    public BookingDto secondExpectedBookingDto;
    public BookingDto expectedBookingDtoUpdated;
    public Booking booking;
    public Booking updatedBooking;
    public Booking secondBooking;
    public long bookingId = 1L;
    public long secondBookingId = 2L;
    public long userId = 1L;
    public long itemId = 1L;

    public BookingTestObjects() {
        bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(itemId);
        bookingCreateDto.setStart(LocalDateTime.now());
        bookingCreateDto.setEnd(LocalDateTime.now().plusMinutes(5));
        bookingCreateDto.setStatus(BookingStatus.WAITING);

        bookingUpdateDto = new BookingUpdateDto();
        bookingUpdateDto.setStart(bookingCreateDto.getStart());
        bookingUpdateDto.setEnd(bookingCreateDto.getEnd());
        bookingUpdateDto.setStatus(BookingStatus.APPROVED);

        expectedBookingDtoCreated = new BookingDto();
        expectedBookingDtoCreated.setId(itemId);
        expectedBookingDtoCreated.setStart(bookingCreateDto.getStart());
        expectedBookingDtoCreated.setEnd(bookingCreateDto.getEnd());
        expectedBookingDtoCreated.setStatus(BookingStatus.WAITING);

        secondExpectedBookingDto = new BookingDto();
        secondExpectedBookingDto.setId(itemId);
        secondExpectedBookingDto.setStart(LocalDateTime.now().plusHours(3));
        secondExpectedBookingDto.setEnd(LocalDateTime.now().plusHours(5));
        secondExpectedBookingDto.setStatus(BookingStatus.WAITING);

        expectedBookingDtoUpdated = new BookingDto();
        expectedBookingDtoUpdated.setId(itemId);
        expectedBookingDtoUpdated.setStart(bookingUpdateDto.getStart());
        expectedBookingDtoUpdated.setEnd(bookingUpdateDto.getEnd());
        expectedBookingDtoUpdated.setStatus(BookingStatus.APPROVED);

        booking = new Booking();
        booking.setId(itemId);
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        secondBooking = new Booking();
        secondBooking.setId(itemId);
        secondBooking.setStart(LocalDateTime.now().plusHours(3));
        secondBooking.setEnd(LocalDateTime.now().plusHours(5));
        secondBooking.setStatus(BookingStatus.WAITING);

        updatedBooking = new Booking();
        updatedBooking.setId(itemId);
        updatedBooking.setStart(bookingUpdateDto.getStart());
        updatedBooking.setEnd(bookingUpdateDto.getEnd());
        updatedBooking.setStatus(BookingStatus.APPROVED);
    }
}
