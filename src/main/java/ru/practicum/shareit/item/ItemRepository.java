package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Long userId, Item item);

    Item getItemById(Long userId, Long itemId);

    List<Item> getAllItems(Long userId);

    Item updateItem(Long userId, Item item, Long itemId);

    void deleteItem(Long userId, Long itemId);

    List<Item> searchItem(String text);
}
