package ru.practicum.users.dto;

import org.springframework.stereotype.Component;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toUserFullDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User fromFullDtoToUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public List<UserDto> toUserFullDtoList(List<User> users) {
        return users.stream().map(this::toUserFullDto).collect(Collectors.toList());
    }
}
