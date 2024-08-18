package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.exception.InternalServerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Map<Long, Item>> items = new HashMap<>(); // userId, itemId, item
    private long currentMaxId = 0;

    @Override
    public Item createItem(Long userId, Item item) {
        currentMaxId++;
        item.setId(currentMaxId);
        items.putIfAbsent(userId, new HashMap<>());
        Map<Long, Item> userItems = items.get(userId);
        if (userItems != null) {
            userItems.put(currentMaxId, item);
        } else {
            currentMaxId--;
            throw new InternalServerException("Could not create Item");
        }
        return item;
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        for (Map<Long, Item> entry : items.values()) {
            if (entry.containsKey(itemId)) {
                return Optional.ofNullable(entry.get(itemId));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return (items.containsKey(userId)) ? items.get(userId).values().stream().toList() : new ArrayList<>();
    }

    @Override
    public boolean updateItem(Long userId, Item item, Long itemId) {
        Map<Long, Item> userItems = items.get(userId);
        userItems.put(itemId, item);
        return true;
    }

    @Override
    public boolean deleteItem(Long userId, Long itemId) {
        Map<Long, Item> userItems = items.get(userId);
        Item item = userItems.remove(itemId);
        return item != null;
    }

    @Override
    public List<Item> searchItem(String text) {
        Set<Item> allItems = new HashSet<>();
        for (Map<Long, Item> entry : items.values()) {
            allItems.addAll(entry.values());
        }
        return allItems.stream()
                .filter(Item::getAvailable)
                .filter(item -> {
                    String name = item.getName().toUpperCase();
                    String description = item.getDescription().toUpperCase();
                    return name.contains(text.toUpperCase()) || description.contains(text.toUpperCase());
                })
                .toList();
    }
}
