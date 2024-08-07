package ru.practicum.shareit.request.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper
public interface ItemRequestMapper {
    ItemRequestMapper MAPPER = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequest itemRequestCreateToItemRequest(ItemRequestCreateDto itemRequestCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void itemRequestUpdateToItemRequest(ItemRequestUpdateDto itemRequestUpdateDto,
                                        @MappingTarget ItemRequest itemRequest);

    ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest);
}
