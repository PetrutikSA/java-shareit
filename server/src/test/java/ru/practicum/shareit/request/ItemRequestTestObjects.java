package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestStatus;

import java.time.LocalDateTime;

public class ItemRequestTestObjects {
    public ItemRequestCreateDto itemRequestCreateDto;
    public ItemRequestDto expectedItemRequestDtoCreated;
    public ItemRequestDto secondExpectedItemRequestDto;
    public ItemRequest itemRequest;
    public ItemRequest secondItemRequest;
    public long itemRequestId = 1L;
    public long secondItemRequestId = 2L;
    public long userId = 1L;

    public ItemRequestTestObjects() {
        itemRequestCreateDto = new ItemRequestCreateDto();
        itemRequestCreateDto.setName("itemRequest");
        itemRequestCreateDto.setDescription("description");

        expectedItemRequestDtoCreated = new ItemRequestDto();
        expectedItemRequestDtoCreated.setId(itemRequestId);
        expectedItemRequestDtoCreated.setName("itemRequest");
        expectedItemRequestDtoCreated.setDescription("description");
        expectedItemRequestDtoCreated.setStatus(ItemRequestStatus.ACTIVE);

        secondExpectedItemRequestDto = new ItemRequestDto();
        secondExpectedItemRequestDto.setId(secondItemRequestId);
        secondExpectedItemRequestDto.setName("second itemRequest");
        secondExpectedItemRequestDto.setDescription("second description");

        itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestId);
        itemRequest.setName("itemRequest");
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setStatus(ItemRequestStatus.ACTIVE);

        secondItemRequest = new ItemRequest();
        secondItemRequest.setId(secondItemRequestId);
        secondItemRequest.setName(secondExpectedItemRequestDto.getName());
        secondItemRequest.setDescription(secondExpectedItemRequestDto.getDescription());
    }
}
