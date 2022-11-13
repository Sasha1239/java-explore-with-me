package ru.practicum.users.dto;

import org.springframework.stereotype.Component;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserFullDto toUserFullDto(User user) {
        return new UserFullDto(user.getId(), user.getName(), user.getEmail());
    }

    public User fromFullDtoToUser(UserFullDto userFullDto) {
        return new User(userFullDto.getId(), userFullDto.getName(), userFullDto.getEmail());
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public List<UserFullDto> toUserFullDtoList(List<User> users) {
        return users.stream().map(this::toUserFullDto).collect(Collectors.toList());
    }
}
