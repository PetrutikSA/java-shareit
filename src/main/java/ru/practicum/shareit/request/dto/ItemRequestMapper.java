package ru.practicum.shareit.request.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestMapper {

    ItemRequest itemRequestCreateToItemRequest(ItemRequestCreateDto itemRequestCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void itemRequestUpdateToItemRequest(ItemRequestUpdateDto itemRequestUpdateDto,
                                        @MappingTarget ItemRequest itemRequest);

    ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest);
}
