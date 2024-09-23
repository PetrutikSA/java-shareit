package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemShortForRequestDto {
    private long id;
    private String name;
    private long userId;
}
