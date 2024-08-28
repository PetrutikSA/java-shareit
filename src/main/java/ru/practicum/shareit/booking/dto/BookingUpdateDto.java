package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

public class BookingUpdateDto {
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private BookingStatus status;
}
