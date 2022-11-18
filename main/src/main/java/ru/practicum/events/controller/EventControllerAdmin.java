package ru.practicum.events.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventNewDto;
import ru.practicum.events.service.EventService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
@AllArgsConstructor
@Slf4j
public class EventControllerAdmin {
    private final EventService eventService;

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEventAdmin(@PathVariable Long eventId) {
        log.info("Администратором опубликовано событие c id: {}", eventId);
        return eventService.publishEventAdmin(eventId);
    }

    @PutMapping("{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable Long eventId, @RequestBody EventNewDto eventNewDto) {
        log.info("Администратором обновлено событие c id {}", eventId);
        return eventService.updateEventAdmin(eventId, eventNewDto);
    }

    @GetMapping
    public List<EventFullDto> getAllEventsAdmin(@RequestParam Optional<List<Long>> users,
                                                @RequestParam Optional<List<String>> states,
                                                @RequestParam Optional<List<Long>> categories,
                                                @RequestParam Optional<String> rangeStart,
                                                @RequestParam Optional<String> rangeEnd,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "20") int size) {

        return eventService.getAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEventAdmin(@PathVariable Long eventId) {
        log.info("Администратором отклонено событие c id: {}", eventId);
        return eventService.rejectEventAdmin(eventId);
    }
}
