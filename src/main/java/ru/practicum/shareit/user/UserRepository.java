package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User createUser(User user);

    Optional<User> findUserById(Long id);

    List<User> getAllUsers();

    User updateUser(User user);

    boolean deleteUser(Long id);
}
