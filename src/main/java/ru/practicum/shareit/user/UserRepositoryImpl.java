package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long currentMaxId = 0;

    @Override
    public User createUser(User user) {
        currentMaxId++;
        user.setId(currentMaxId);
        users.put(currentMaxId, user);
        return user;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return (users.containsKey(id)) ? Optional.ofNullable(users.get(id)) : Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public boolean updateUser(User user) {
        users.put(user.getId(), user);
        return true;
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = users.remove(id);
        return user != null;
    }
}
