package ru.practicum.shareit.item.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    Item itemCreateToItem(ItemCreateDto itemCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void itemUpdateToItem(ItemUpdateDto itemUpdateDto, @MappingTarget Item item);

    ItemDto itemToItemDto(Item item);

    ItemWithNearestBookingDatesDto itemToItemWithNearestDatesDto(Item item);
}
