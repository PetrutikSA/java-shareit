package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapperImpl;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.UserConflictException;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {UserServiceImpl.class, UserMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @MockBean private final UserRepository userRepository;
    private final UserService userService;

    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private UserDto expectedUserDtoCreated;
    private UserDto secondExpectedUserDto;
    private UserDto expectedUserDtoUpdated;
    private User user;
    private User updatedUser;
    private User secondUser;
    private long userId;

    @BeforeEach
    void setUp() {
        UserTestObjects userTestObjects = new UserTestObjects();
        userCreateDto = userTestObjects.userCreateDto;
        userUpdateDto = userTestObjects.userUpdateDto;
        expectedUserDtoCreated = userTestObjects.expectedUserDtoCreated;
        secondExpectedUserDto = userTestObjects.secondExpectedUserDto;
        expectedUserDtoUpdated = userTestObjects.expectedUserDtoUpdated;
        user = userTestObjects.user;
        secondUser = userTestObjects.secondUser;
        updatedUser = userTestObjects.updatedUser;

        userId = userTestObjects.userId;
    }

    @Test
    void saveUserUniqueEmailTest() {
        Mockito.when(userRepository.existsByEmail(Mockito.any()))
                .thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(user);

        UserDto actualUser = userService.createUser(userCreateDto);

        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUserDtoCreated, actualUser);
    }

    @Test
    void saveUserNotCorrectEmailTest() {
        Mockito.when(userRepository.existsByEmail(Mockito.any()))
                .thenReturn(true);

        Assertions.assertThrows(UserConflictException.class, () -> {
            userService.createUser(userCreateDto);
        });
    }

    @Test
    void updateUserUniqueEmailTest() {
        Mockito.when(userRepository.existsByEmail(Mockito.any()))
                .thenReturn(false);
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(updatedUser);

        UserDto actualUser = userService.updateUser(userId, userUpdateDto);

        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUserDtoUpdated, actualUser);
    }

    @Test
    void updateUserNotCorrectEmailTest() {
        Mockito.when(userRepository.existsByEmail(Mockito.any()))
                .thenReturn(true);
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));
        Assertions.assertThrows(UserConflictException.class, () -> {
            userService.updateUser(userId, userUpdateDto);
        });
    }

    @Test
    void deleteUserTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepository).deleteById(userId);
    }

    @Test
    void getByIdTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));

        UserDto actualUser = userService.getUserById(userId);

        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUserDtoCreated, actualUser);
    }

    @Test
    void getAllTest() {
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(user, secondUser));

        List<UserDto> actualUsers = userService.getAllUsers();

        Assertions.assertNotNull(actualUsers);
        Assertions.assertEquals(expectedUserDtoCreated, actualUsers.get(0));
        Assertions.assertEquals(secondExpectedUserDto, actualUsers.get(1));
    }
}
