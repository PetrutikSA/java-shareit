package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    Booking bookingCreateToBooking(BookingCreateDto bookingCreateDto);

    BookingDto bookingToBookingDto(Booking booking);

    BookingOnlyDatesDto bookingToBookingOnlyDatesDto(Booking booking);
}
