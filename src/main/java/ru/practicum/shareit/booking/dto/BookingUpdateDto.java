package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

public class BookingUpdateDto {
    @FutureOrPresent
    private LocalDate start;
    @Future
    private LocalDate end;
}
