package ru.practicum.users.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.dto.UserFullDto;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utilits.PageableRequest;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //Создание пользователя
    @Override
    public UserFullDto createUser(UserFullDto userFullDto) {
        User user = userRepository.save(userMapper.fromFullDtoToUser(userFullDto));
        return userMapper.toUserFullDto(user);
    }

    //Получение пользователей
    @Override
    public List<UserFullDto> getAllUsers(Long[] ids, Integer from, Integer size) {
        List<User> users;

        users = (ids == null) ? userRepository.findAll(getPageable(from, size)).getContent()
                : userRepository.findAllById(Arrays.asList(ids));

        return userMapper.toUserFullDtoList(users);
    }

    //Удаление пользователя
    @Override
    public void deleteUser(Long userId) {
        getUser(userId);
        userRepository.deleteById(userId);
    }

    //Получение пользователя
    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Неверный идентификатор пользователя"));
    }

    private Pageable getPageable(Integer from, Integer size) {
        return new PageableRequest(from, size, Sort.unsorted());
    }
}
