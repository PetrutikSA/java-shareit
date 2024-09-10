package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemShortForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
    private long id;
    private String name;
    private String description;
    private ItemRequestStatus status;
    private List<ItemShortForRequestDto> items;
    private LocalDateTime created;
}
