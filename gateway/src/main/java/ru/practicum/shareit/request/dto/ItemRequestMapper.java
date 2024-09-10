package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestMapper {

    ItemRequest itemRequestCreateToItemRequest(ItemRequestCreateDto itemRequestCreateDto);

    ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest);
}
