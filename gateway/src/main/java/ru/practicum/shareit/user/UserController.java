package ru.practicum.shareit.user;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated UserCreateDto userCreateDto) {
        log.info("Create user {}", userCreateDto);
        return userClient.createUser(userCreateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable Long id) {
        log.info("Get user with Id={}", id);
        return userClient.getUserById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@Positive @PathVariable Long id,
                                             @RequestBody @Validated UserUpdateDto userUpdateDto) {
        log.info("Update user {}", userUpdateDto);
        return userClient.updateUser(id, userUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@Positive @PathVariable Long id) {
        log.info("Delete user with id = {}", id);
        return userClient.deleteUser(id);
    }
}
