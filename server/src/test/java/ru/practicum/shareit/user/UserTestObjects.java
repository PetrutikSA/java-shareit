package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

public class UserTestObjects {
    public UserCreateDto userCreateDto;
    public UserUpdateDto userUpdateDto;
    public UserDto expectedUserDtoCreated;
    public UserDto secondExpectedUserDto;
    public UserDto expectedUserDtoUpdated;
    public User user;
    public User updatedUser;
    public User secondUser;
    public long userId = 1L;
    public long secondUserId = 2L;

    public UserTestObjects() {
        userCreateDto = new UserCreateDto();
        userCreateDto.setName("name");
        userCreateDto.setEmail("email@ya.ru");

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("updatedName");
        userUpdateDto.setEmail("updatedEmail@ya.ru");

        expectedUserDtoCreated = new UserDto();
        expectedUserDtoCreated.setId(userId);
        expectedUserDtoCreated.setName(userCreateDto.getName());
        expectedUserDtoCreated.setEmail(userCreateDto.getEmail());

        secondExpectedUserDto = new UserDto();
        secondExpectedUserDto.setId(secondUserId);
        secondExpectedUserDto.setName("secondName");
        secondExpectedUserDto.setEmail("secondEmail@ya.ru");

        expectedUserDtoUpdated = new UserDto();
        expectedUserDtoUpdated.setId(userId);
        expectedUserDtoUpdated.setName(userUpdateDto.getName());
        expectedUserDtoUpdated.setEmail(userUpdateDto.getEmail());

        user = new User();
        user.setId(userId);
        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());

        updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName(userUpdateDto.getName());
        updatedUser.setEmail(userUpdateDto.getEmail());

        secondUser = new User();
        secondUser.setId(secondUserId);
        secondUser.setName(secondExpectedUserDto.getName());
        secondUser.setEmail(secondExpectedUserDto.getEmail());
    }
}
