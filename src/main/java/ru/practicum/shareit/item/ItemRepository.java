package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Long userId, Item item);

    Optional<Item> findItemById(Long itemId);

    List<Item> getAllItems(Long userId);

    boolean updateItem(Long userId, Item item, Long itemId);

    boolean deleteItem(Long userId, Long itemId);

    List<Item> searchItem(String text);
}
