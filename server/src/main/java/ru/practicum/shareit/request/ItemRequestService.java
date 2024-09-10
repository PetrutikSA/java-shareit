package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    public ItemRequestDto createItemRequest(Long userId, ItemRequestCreateDto itemRequestCreateDto);

    List<ItemRequestDto> getAllOwnItemRequests(Long userId);

    List<ItemRequestDto> getAllOthersItemRequests(Long userId);

    ItemRequestDto getItemRequestById(Long userId, Long itemRequestId);
}
