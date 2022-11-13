package ru.practicum.users.service;

import ru.practicum.users.dto.UserFullDto;
import ru.practicum.users.model.User;

import java.util.List;

public interface UserService {
    UserFullDto createUser(UserFullDto userFullDto);

    List<UserFullDto> getAllUsers(Long[] ids, Integer from, Integer size);

    void deleteUser(Long userId);

    User getUser(Long userId);
}
