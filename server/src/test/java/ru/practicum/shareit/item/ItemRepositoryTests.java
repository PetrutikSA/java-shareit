package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserTestObjects;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTests {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private Item item;
    private Item updatedItem;
    private Item secondItem;
    private User user;

    @BeforeEach
    void setUp() {
        ItemTestObjects itemTestObjects = new ItemTestObjects();
        item = itemTestObjects.item;
        secondItem = itemTestObjects.secondItem;
        updatedItem = itemTestObjects.updatedItem;

        UserTestObjects userTestObjects = new UserTestObjects();
        user = userTestObjects.user;

        item.setOwner(user);
        secondItem.setOwner(user);
        updatedItem.setOwner(user);
    }

    @Test
    void saveItemShouldReturnUserWithId() {
        user.setId(0);
        userRepository.save(user);
        item.setId(0);
        Item actualItem = itemRepository.save(item);
        Assertions.assertNotNull(actualItem);
        Assertions.assertTrue(actualItem.getId() > 0);
    }

    @Test
    void correctUpdateExistedUser() {
        user.setId(0);
        userRepository.save(user);
        item.setId(0);
        item = itemRepository.save(item);
        updatedItem.setId(item.getId());
        Item actualItem = itemRepository.save(updatedItem);

        Assertions.assertNotNull(actualItem);
        Assertions.assertEquals(item.getId(), actualItem.getId());
        Assertions.assertEquals(updatedItem.getName(), actualItem.getName());
        Assertions.assertEquals(updatedItem.getDescription(), actualItem.getDescription());
    }
    @Test
    void findAllByNameOrDescriptionTest() {
        user.setId(0);
        userRepository.save(user);
        item.setId(0);
        itemRepository.save(item);
        List<Item> items = itemRepository.findAllByNameOrDescription(item.getDescription());
        Assertions.assertEquals(item.getName(), items.get(0).getName());
        Assertions.assertEquals(item.getDescription(), items.get(0).getDescription());

    }

    @Test
    void getAllItemsTest() {
        user.setId(0);
        item.setId(0);
        secondItem.setId(0);
        userRepository.save(user);
        itemRepository.save(item);
        itemRepository.save(secondItem);

        List<Item> items = itemRepository.findAll();
        Assertions.assertEquals(2, items.size());
    }
}
