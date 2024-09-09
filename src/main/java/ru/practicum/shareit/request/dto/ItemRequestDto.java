package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemShortForRequestDto;
import ru.practicum.shareit.request.model.ItemRequestStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestDto {
    private long id;
    private String name;
    private String description;
    private ItemRequestStatus status;
    private List<ItemShortForRequestDto> offeredItems;
    private LocalDateTime created;
}
