package ru.practicum.shareit.booking.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.model.Booking;

@Mapper
public interface BookingMapper {
    BookingMapper MAPPER = Mappers.getMapper(BookingMapper.class);

    Booking bookingCreateToBooking(BookingCreateDto bookingCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void bookingUpdateToBooking(BookingUpdateDto bookingUpdateDto, @MappingTarget Booking booking);

    BookingDto bookingToBookingDto(Booking booking);
}
