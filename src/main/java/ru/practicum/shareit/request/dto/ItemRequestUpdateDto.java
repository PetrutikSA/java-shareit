package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import ru.practicum.shareit.request.model.ItemRequestStatus;

public class ItemRequestUpdateDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private ItemRequestStatus status;
}
