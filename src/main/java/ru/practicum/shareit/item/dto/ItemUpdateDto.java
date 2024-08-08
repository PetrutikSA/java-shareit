package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.util.annotation.NullOrNotBlank;

@Data
public class ItemUpdateDto {
    @NullOrNotBlank
    private String name;
    @NullOrNotBlank
    private String description;
    private Boolean available;
}
