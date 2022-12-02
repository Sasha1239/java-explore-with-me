package ru.practicum.users.service;

import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUsers(Long[] ids, Integer from, Integer size);

    void deleteUser(Long userId);

    User getUser(Long userId);
}
