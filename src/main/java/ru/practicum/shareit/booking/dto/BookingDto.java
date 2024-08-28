package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

@Data
public class BookingDto {
    private long id;
    private Item item;
    private LocalDate start;
    private LocalDate end;
    private BookingState status;
}
