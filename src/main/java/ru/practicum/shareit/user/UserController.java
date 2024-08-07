package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @PostMapping
    public UserDto createUser(@RequestBody UserCreateDto userCreateDto) {
        return null;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return null;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return null;
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, UserUpdateDto userUpdateDto) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
    }
}