package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import ru.practicum.shareit.util.annotation.NullOrNotBlank;

@Data
public class UserUpdateDto {
    @NullOrNotBlank
    private String name;
    @NullOrNotBlank
    @Email
    private String email;
}
