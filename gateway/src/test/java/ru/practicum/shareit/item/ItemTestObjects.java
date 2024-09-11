package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;


public class ItemTestObjects {
    public ItemCreateDto itemCreateDto;
    public ItemUpdateDto itemUpdateDto;
    public long itemId = 1L;
    public long secondItemId = 2L;
    public long userId = 1L;

    public ItemTestObjects() {
        itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("item");
        itemCreateDto.setDescription("description");
        itemCreateDto.setAvailable(true);

        itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("updated item");
        itemUpdateDto.setDescription("updated description");
        itemUpdateDto.setAvailable(false);
    }
}
