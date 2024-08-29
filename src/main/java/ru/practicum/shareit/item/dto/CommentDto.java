package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

@Data
public class CommentDto {
    private long id;
    private String text;
    private UserDto author;
    private ItemDto item;
}
