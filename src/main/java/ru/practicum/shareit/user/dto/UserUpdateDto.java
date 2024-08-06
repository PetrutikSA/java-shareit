package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
