package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserTestObjects;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestRepositoryTests {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private ItemRequest itemRequest;
    private User user;

    @BeforeEach
    void setUp() {
        ItemRequestTestObjects itemRequestTestObjects = new ItemRequestTestObjects();
        itemRequest = itemRequestTestObjects.itemRequest;

        UserTestObjects userTestObjects = new UserTestObjects();
        user = userTestObjects.user;
    }

    @Test
    void saveItemRequestShouldReturnUserWithId() {
        user.setId(0);
        user = userRepository.save(user);
        itemRequest.setId(0);
        itemRequest.setRequester(user);
        ItemRequest actualItem = itemRequestRepository.save(itemRequest);
        Assertions.assertNotNull(actualItem);
        Assertions.assertTrue(actualItem.getId() > 0);
    }

    @Test
    void findByIdTest() {
        user.setId(0);
        userRepository.save(user);
        user = userRepository.save(user);
        itemRequest.setId(0);
        itemRequest.setRequester(user);
        itemRequest = itemRequestRepository.save(itemRequest);
        ItemRequest actualItemRequest = itemRequestRepository.findById(itemRequest.getId()).get();
        Assertions.assertEquals(itemRequest.getName(), actualItemRequest.getName());
        Assertions.assertEquals(itemRequest.getDescription(), actualItemRequest.getDescription());

    }

    @Test
    void getAllOwnItemRequestsTest() {
        user.setId(0);
        userRepository.save(user);
        user = userRepository.save(user);
        itemRequest.setId(0);
        itemRequest.setRequester(user);
        itemRequestRepository.save(itemRequest);

        List<ItemRequest> items = itemRequestRepository.findAllByRequesterId(user.getId());
        Assertions.assertEquals(1, items.size());
    }
}
