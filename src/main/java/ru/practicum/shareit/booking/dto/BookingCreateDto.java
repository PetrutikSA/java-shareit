package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingCreateDto {
    @NotNull
    @FutureOrPresent
    private LocalDate start;
    @NotNull
    @Future
    private LocalDate end;
}
