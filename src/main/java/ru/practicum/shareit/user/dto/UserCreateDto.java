package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateDto {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String email;
}
