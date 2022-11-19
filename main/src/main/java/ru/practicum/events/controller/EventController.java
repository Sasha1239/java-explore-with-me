package ru.practicum.events.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import ru.practicum.events.client.EventClient;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventNewDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.service.EventService;
import ru.practicum.requests.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@AllArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;
    private final EventClient eventClient;

    @PostMapping("/users/{userId}/events")
    public EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody EventNewDto eventNewDto) {
        log.info("Создано событие {}", eventNewDto);
        return eventService.createPrivateEvent(userId, eventNewDto);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEvent(@PathVariable Long userId, @Valid @RequestBody EventNewDto eventNewDto) {
        log.info("Обновлено событие {}", eventNewDto);
        return eventService.updatePrivateEvent(userId, eventNewDto);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getAllEventsUser(@PathVariable Long userId,
                                                @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                                Integer from,
                                                @Positive @RequestParam(value = "size", defaultValue = "10")
                                                Integer size) {
        return eventService.getAllPrivateEvent(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получено событие с id {}", eventId);
        return eventService.getPrivateEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Отклонено событие с id {}", eventId);
        return eventService.cancelPrivateEvent(userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getEventRequest(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventRequestsPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long reqId) {
        log.info("Подтверждена заявка с id: {}", reqId);
        return eventService.confirmRequestPrivate(userId, eventId, reqId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @PathVariable Long reqId) {
        log.info("Отклонена заявка c id: {}", reqId);
        return eventService.rejectRequestPrivate(userId, eventId, reqId);
    }

    @GetMapping("/events")
    public List<EventShortDto> getAllEvents(@RequestParam Optional<String> text,
                                            @RequestParam Optional<List<Long>> categories,
                                            @RequestParam Optional<Boolean> paid,
                                            @RequestParam Optional<String> rangeStart,
                                            @RequestParam Optional<String> rangeEnd,
                                            @RequestParam Optional<Boolean> onlyAvailable,
                                            @RequestParam Optional<String> sort,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "20") int size) {

        return eventService.getAllPublicEvents(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable(name = "id") Long eventId, HttpServletRequest request) {
        log.info("Получено событие c id: {}", eventId);

        try {
            eventClient.addHit(request);
        } catch (RestClientException e) {
            log.info("Соединение с сервисом отсутствует");
        }

        return eventService.getPublicEvent(eventId);
    }
}
