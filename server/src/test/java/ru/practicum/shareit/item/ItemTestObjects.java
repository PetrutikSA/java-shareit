package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingDatesDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

public class ItemTestObjects {
    public ItemCreateDto itemCreateDto;
    public ItemUpdateDto itemUpdateDto;
    public ItemDto expectedItemDtoCreated;
    public ItemDto secondExpectedItemDto;
    public CommentCreateDto commentCreateDto;
    public ItemWithNearestBookingDatesDto expectedItemDtoWithBookingsDatesCreated;
    public ItemWithNearestBookingDatesDto secondExpectedItemDtoWithBookingsDates;
    public ItemDto expectedItemDtoUpdated;
    public Item item;
    public Item updatedItem;
    public Item secondItem;
    public Comment comment;
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

        expectedItemDtoCreated = new ItemDto();
        expectedItemDtoCreated.setId(itemId);
        expectedItemDtoCreated.setName("item");
        expectedItemDtoCreated.setDescription("description");
        expectedItemDtoCreated.setAvailable(true);

        secondExpectedItemDto = new ItemDto();
        secondExpectedItemDto.setId(secondItemId);
        secondExpectedItemDto.setName("second item");
        secondExpectedItemDto.setDescription("second description");
        secondExpectedItemDto.setAvailable(true);

        expectedItemDtoUpdated = new ItemDto();
        expectedItemDtoUpdated.setId(itemId);
        expectedItemDtoUpdated.setName(itemUpdateDto.getName());
        expectedItemDtoUpdated.setDescription(itemUpdateDto.getDescription());
        expectedItemDtoUpdated.setAvailable(itemUpdateDto.getAvailable());

        item = new Item();
        item.setId(itemId);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);

        secondItem = new Item();
        secondItem.setId(secondItemId);
        secondItem.setName(secondExpectedItemDto.getName());
        secondItem.setDescription(secondExpectedItemDto.getDescription());
        secondItem.setAvailable(secondExpectedItemDto.getAvailable());

        updatedItem = new Item();
        updatedItem.setId(itemId);
        updatedItem.setName(itemUpdateDto.getName());
        updatedItem.setDescription(itemUpdateDto.getDescription());
        updatedItem.setAvailable(itemUpdateDto.getAvailable());

        expectedItemDtoWithBookingsDatesCreated = new ItemWithNearestBookingDatesDto();
        expectedItemDtoWithBookingsDatesCreated.setId(itemId);
        expectedItemDtoWithBookingsDatesCreated.setName("item");
        expectedItemDtoWithBookingsDatesCreated.setDescription("description");
        expectedItemDtoWithBookingsDatesCreated.setAvailable(true);

        secondExpectedItemDtoWithBookingsDates = new ItemWithNearestBookingDatesDto();
        secondExpectedItemDtoWithBookingsDates.setId(secondItemId);
        secondExpectedItemDtoWithBookingsDates.setName(secondExpectedItemDto.getName());
        secondExpectedItemDtoWithBookingsDates.setDescription(secondExpectedItemDto.getDescription());
        secondExpectedItemDtoWithBookingsDates.setAvailable(secondExpectedItemDto.getAvailable());

        commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("text");
        comment = new Comment();
        comment.setText(commentCreateDto.getText());
    }
}
