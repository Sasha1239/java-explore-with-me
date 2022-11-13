package ru.practicum.users.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.UserFullDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserFullDto createUser(@Valid @RequestBody UserFullDto userFullDto) {
        log.info("Создан пользователь с id = {}", userFullDto.getId());
        return userService.createUser(userFullDto);
    }

    @DeleteMapping(path = "/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удален пользователь с id = {}", userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserFullDto> getAllUsers(@RequestParam(value = "ids", required = false) Long[] ids,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userService.getAllUsers(ids, from, size);
    }
}
