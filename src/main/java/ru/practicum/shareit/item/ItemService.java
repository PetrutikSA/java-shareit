package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingDatesDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemCreateDto itemCreateDto);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemWithNearestBookingDatesDto> getAllItems(Long userId);

    ItemDto updateItem(Long userId, ItemUpdateDto itemUpdateDto, Long itemId);

    void deleteItem(Long userId, Long itemId);

    List<ItemDto> searchItem(String text);
}
