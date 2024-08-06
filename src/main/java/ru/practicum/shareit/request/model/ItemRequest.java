package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
public class ItemRequest {
    private long id;
    private String name;
    private String description;
    private User requester;
    private ItemRequestStatus status;
    private List<Item> offeredItems;
}
