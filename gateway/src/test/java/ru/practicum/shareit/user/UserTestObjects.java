package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public class UserTestObjects {
    public UserCreateDto userCreateDto;
    public UserUpdateDto userUpdateDto;
    public String userCreatedDtoJson;
    public String userUpdatedDtoJson;
    public long userId;

    public UserTestObjects() {
        userCreateDto = new UserCreateDto();
        userCreateDto.setName("name");
        userCreateDto.setEmail("email@ya.ru");

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("updatedName");
        userUpdateDto.setEmail("updatedEmail@ya.ru");

        userId = 1L;

        userCreatedDtoJson = "{" +
                "\"name\": \"" + userCreateDto.getName() + "\"," +
                "\"email\": \"" + userCreateDto.getEmail() + "\"" +
                "}";
    }
}
