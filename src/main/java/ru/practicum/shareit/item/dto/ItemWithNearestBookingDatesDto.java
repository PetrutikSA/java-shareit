package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingOnlyDatesDto;

@Data
public class ItemWithNearestBookingDatesDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingOnlyDatesDto last;
    private BookingOnlyDatesDto next;
}
