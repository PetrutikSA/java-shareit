package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingCreateDto {
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
}
