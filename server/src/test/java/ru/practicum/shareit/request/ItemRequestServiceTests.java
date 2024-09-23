package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.CommentMapperImpl;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapperImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserTestObjects;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ItemRequestServiceImpl.class, ItemMapperImpl.class, ItemRequestMapperImpl.class, CommentMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTests {
    @MockBean
    private final ItemRepository itemRepository;
    @MockBean
    private final UserRepository userRepository;
    @MockBean
    private final ItemRequestRepository itemRequestRepository;

    private final ItemRequestService itemRequestService;

    private ItemRequestCreateDto itemRequestCreateDto;
    private ItemRequestDto expectedItemRequestDtoCreated;
    private ItemRequest itemRequest;
    private long itemId;
    private long userId;
    private User user;

    @BeforeEach
    void setUp() {
        ItemRequestTestObjects itemRequestTestObjects = new ItemRequestTestObjects();

        itemRequestCreateDto = itemRequestTestObjects.itemRequestCreateDto;
        expectedItemRequestDtoCreated = itemRequestTestObjects.expectedItemRequestDtoCreated;
        itemRequest = itemRequestTestObjects.itemRequest;

        UserTestObjects userTestObjects = new UserTestObjects();
        user = userTestObjects.user;
    }

    @Test
    void saveItemRequestUserExistTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRequestRepository.save(Mockito.any()))
                .thenReturn(itemRequest);

        ItemRequestDto actualItem = itemRequestService.createItemRequest(userId, itemRequestCreateDto);

        Assertions.assertNotNull(actualItem);
        assertThat(actualItem)
                .usingRecursiveComparison().ignoringFields("id", "created").isEqualTo(expectedItemRequestDtoCreated);
    }


    @Test
    void saveItemUserNotExistTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> {
            itemRequestService.createItemRequest(userId, itemRequestCreateDto);
        });
    }

    @Test
    void getByIdTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRequestRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(itemRequest));

        ItemRequestDto actualItem = itemRequestService.getItemRequestById(userId, itemId);

        Assertions.assertNotNull(actualItem);
        assertThat(actualItem)
                .usingRecursiveComparison().ignoringFields("created").isEqualTo(expectedItemRequestDtoCreated);
    }

    @Test
    void getAllOthersItemRequestsTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRequestRepository.findAllByRequesterIdNot(userId))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> actualItems = itemRequestService.getAllOthersItemRequests(userId);

        Assertions.assertNotNull(actualItems);
        assertThat(actualItems.get(0))
                .usingRecursiveComparison().ignoringFields("id", "created").isEqualTo(expectedItemRequestDtoCreated);
    }
}
