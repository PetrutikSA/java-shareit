package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequestCreateDto {
    private String name;
    @NotNull
    @NotBlank
    private String description;
}
