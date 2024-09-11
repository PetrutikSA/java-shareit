package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {
    private final UserRepository userRepository;

    private User user;
    private User updatedUser;
    private User secondUser;

    @BeforeEach
    void setUp() {
        UserTestObjects userTestObjects = new UserTestObjects();
        user = userTestObjects.user;
        secondUser = userTestObjects.secondUser;
        updatedUser = userTestObjects.updatedUser;
    }

    @Test
    void saveUserShouldReturnUserWithId() {
        user.setId(0);
        User actualUser = userRepository.save(user);
        Assertions.assertNotNull(actualUser);
        Assertions.assertTrue(actualUser.getId() > 0);
    }

    @Test
    void correctUpdateExistedUser() {
        user.setId(0);
        user = userRepository.save(user);
        updatedUser.setId(user.getId());
        User actualUser = userRepository.save(updatedUser);

        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(user.getId(), actualUser.getId());
        Assertions.assertEquals(updatedUser.getName(), actualUser.getName());
        Assertions.assertEquals(updatedUser.getEmail(), actualUser.getEmail());
    }
    @Test
    void checkEmailExistenceTest() {
        userRepository.save(user);
        boolean isUserEmailExist = userRepository.existsByEmail(user.getEmail());
        boolean isSecondUserEmailExist = userRepository.existsByEmail(secondUser.getEmail());
        Assertions.assertTrue(isUserEmailExist);
        Assertions.assertFalse(isSecondUserEmailExist);

    }

    @Test
    void getAllUsersTest() {
        userRepository.save(user);
        userRepository.save(secondUser);

        List<User> actualUsers = userRepository.findAll();
        Assertions.assertEquals(actualUsers.size(), 2);
    }
}
