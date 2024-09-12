package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingTestObjects;
import ru.practicum.shareit.booking.dto.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapperImpl;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingDatesDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserTestObjects;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.BadRequestException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(classes = {ItemServiceImpl.class, ItemMapperImpl.class, CommentMapperImpl.class, BookingMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTests {
    @MockBean
    private final ItemRepository itemRepository;
    @MockBean
    private final UserRepository userRepository;
    @MockBean
    private final ItemRequestRepository itemRequestRepository;
    @MockBean
    private final BookingRepository bookingRepository;
    @MockBean
    private final CommentRepository commentRepository;
    private final ItemService itemService;

    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private CommentCreateDto commentCreateDto;
    private ItemDto expectedItemDtoCreated;
    private ItemDto expectedItemDtoUpdated;
    private Item item;
    private Item secondItem;
    private Item updatedItem;
    private Comment comment;
    private Booking booking;
    private long itemId;
    private long userId;
    private ItemWithNearestBookingDatesDto expectedItemDtoWithBookingsDatesCreated;
    private ItemWithNearestBookingDatesDto secondExpectedItemDtoWithBookingsDates;
    private User user;

    @BeforeEach
    void setUp() {
        ItemTestObjects itemTestObjects = new ItemTestObjects();
        itemCreateDto = itemTestObjects.itemCreateDto;
        itemUpdateDto = itemTestObjects.itemUpdateDto;
        expectedItemDtoCreated = itemTestObjects.expectedItemDtoCreated;
        expectedItemDtoUpdated = itemTestObjects.expectedItemDtoUpdated;
        itemId = itemTestObjects.itemId;
        userId = itemTestObjects.userId;
        expectedItemDtoWithBookingsDatesCreated = itemTestObjects.expectedItemDtoWithBookingsDatesCreated;
        secondExpectedItemDtoWithBookingsDates = itemTestObjects.secondExpectedItemDtoWithBookingsDates;
        item = itemTestObjects.item;
        item.setOwner(user);
        secondItem = itemTestObjects.secondItem;
        updatedItem = itemTestObjects.updatedItem;
        commentCreateDto = itemTestObjects.commentCreateDto;
        comment = itemTestObjects.comment;

        UserTestObjects userTestObjects = new UserTestObjects();
        user = userTestObjects.user;

        item.setOwner(user);
        secondItem.setOwner(user);

        BookingTestObjects bookingTestObjects = new BookingTestObjects();
        booking = bookingTestObjects.booking;
        booking.setBooker(user);

        comment.setAuthor(user);
        comment.setItem(item);
    }

    @Test
    void saveItemUserExistTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.save(Mockito.any()))
                .thenReturn(item);

        ItemDto actualItem = itemService.createItem(userId, itemCreateDto);

        Assertions.assertNotNull(actualItem);
        Assertions.assertEquals(expectedItemDtoCreated, actualItem);
    }


    @Test
    void saveItemUserNotExistTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.createItem(userId, itemCreateDto);
        });
    }

    @Test
    void updateItemExistTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.save(Mockito.any()))
                .thenReturn(updatedItem);

        ItemDto actualItem = itemService.updateItem(userId, itemUpdateDto, itemId);

        Assertions.assertNotNull(actualItem);
        Assertions.assertEquals(expectedItemDtoUpdated, actualItem);
    }

    @Test
    void updateItemNotExistTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(userId, itemUpdateDto, itemId);
        });
    }

    @Test
    void deleteItemTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.save(Mockito.any()))
                .thenReturn(updatedItem);
        itemService.deleteItem(userId, itemId);
        Mockito.verify(itemRepository).deleteById(itemId);
    }

    @Test
    void getByIdTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(item));

        ItemWithNearestBookingDatesDto actualItem = itemService.getItemById(userId, itemId);

        Assertions.assertNotNull(actualItem);
        Assertions.assertEquals(expectedItemDtoWithBookingsDatesCreated, actualItem);
    }

    @Test
    void getAllTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findAllByOwnerId(userId))
                .thenReturn(List.of(item, secondItem));

        List<ItemWithNearestBookingDatesDto> actualItems = itemService.getAllItems(userId);

        Assertions.assertNotNull(actualItems);
        Assertions.assertEquals(expectedItemDtoWithBookingsDatesCreated, actualItems.get(0));
        Assertions.assertEquals(secondExpectedItemDtoWithBookingsDates, actualItems.get(1));
    }

    @Test
    void createCommentTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(userId))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findAllByBookerIdAndItemIdAndEndLessThan(eq(userId), eq(itemId), Mockito.any()))
                .thenReturn(List.of(booking));
        Mockito.when(commentRepository.save(Mockito.any()))
                .thenReturn(comment);

        CommentDto actualComment = itemService.createComment(userId, itemId, commentCreateDto);

        Assertions.assertNotNull(actualComment);
        Assertions.assertEquals(comment.getText(), actualComment.getText());
    }

    @Test
    void createCommentNotByBookerBadRequestTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(userId))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findAllByBookerIdAndItemIdAndEndLessThan(userId, itemId, LocalDateTime.now()))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(BadRequestException.class, () -> {
            itemService.createComment(userId, itemId, commentCreateDto);
        });
    }
}
