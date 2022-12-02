package ru.practicum.requests.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@AllArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto createRequest(@PathVariable Long userId, @RequestParam(value = "eventId") Long eventId) {
        log.info("Заявка на участие в событии с id: {} для пользователя: {} добавлена", eventId, userId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getAllRequestsUser(@PathVariable Long userId) {
        return requestService.getAllRequestsUser(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Заявка на событие c id: {} отменена", requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}
