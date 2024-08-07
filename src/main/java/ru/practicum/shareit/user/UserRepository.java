package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User createUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(User user);

    boolean deleteUser(Long id);
}
