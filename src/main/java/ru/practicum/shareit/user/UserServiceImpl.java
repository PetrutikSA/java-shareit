package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.util.exception.InternalServerException;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.UserConflictException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserCreateDto userCreateDto) {
        User user = UserMapper.MAPPER.userCreateToUser(userCreateDto);
        emailVacantValidation(user.getEmail());
        user = userRepository.createUser(user);
        log.info("Created new user: {}", user);
        return UserMapper.MAPPER.userToUserDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = getUserFromRepository(id);
        return UserMapper.MAPPER.userToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapper.MAPPER::userToUserDto)
                .toList();
    }

    @Override
    public UserDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = getUserFromRepository(id);
        String updatedEmail = userUpdateDto.getEmail();
        if (updatedEmail != null && !updatedEmail.equals(user.getEmail())) {
            emailVacantValidation(userUpdateDto.getEmail());
        }
        UserMapper.MAPPER.userUpdateToUser(userUpdateDto, user);
        boolean isUpdated = userRepository.updateUser(user);
        if (!isUpdated) throw new InternalServerException(String.format("Could not update user: %s", user.toString()));
        log.info("Updated user: {}", user);
        return UserMapper.MAPPER.userToUserDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserFromRepository(id); //check existence of User
        boolean isDeleted = userRepository.deleteUser(id);
        if (!isDeleted) throw new InternalServerException(String.format("Could not delete user: %s", user.toString()));
        log.info("Deleted new user: {}", user);
    }

    private User getUserFromRepository(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException(id, User.class));
    }

    private void emailVacantValidation(String email) {
        if (userRepository.isEmailAlreadyInUse(email))
            throw new UserConflictException(String.format("User with email %s already registered", email));
    }
}
