package ru.practicum.shareit.booking.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    Booking bookingCreateToBooking(BookingCreateDto bookingCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void bookingUpdateToBooking(BookingUpdateDto bookingUpdateDto, @MappingTarget Booking booking);

    BookingDto bookingToBookingDto(Booking booking);

    BookingOnlyDatesDto bookingToBookingOnlyDatesDto(Booking booking);
}
