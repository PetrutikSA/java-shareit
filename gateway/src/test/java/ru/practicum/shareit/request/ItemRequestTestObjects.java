package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

public class ItemRequestTestObjects {
    public ItemRequestCreateDto itemRequestCreateDto;
    public long itemRequestId = 1L;
    public long secondItemRequestId = 2L;
    public long userId = 1L;

    public ItemRequestTestObjects() {
        itemRequestCreateDto = new ItemRequestCreateDto();
        itemRequestCreateDto.setName("itemRequest");
        itemRequestCreateDto.setDescription("description");
    }
}
