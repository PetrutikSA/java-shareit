package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDate;

public class BookingUpdateDto {
    @FutureOrPresent
    private LocalDate start;
    @Future
    private LocalDate end;
    private BookingState state;
}
