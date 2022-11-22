package ru.practicum.events.dto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.utilits.DateFormatterCustom;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    private static final DateFormatterCustom dateFormatterCustom = new DateFormatterCustom();

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                new CategoryDto(event.getCategory().getId(), event.getCategory().getName()),
                event.getConfirmedRequests(),
                dateFormatterCustom.dateToString(event.getCreatedOn()),
                event.getDescription(),
                dateFormatterCustom.dateToString(event.getEventDate()),
                new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                dateFormatterCustom.dateToString(event.getPublishedOn()),
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                event.getViews());
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(event.getId(),
                event.getAnnotation(),
                new CategoryDto(event.getCategory().getId(), event.getCategory().getName()),
                event.getConfirmedRequests(),
                dateFormatterCustom.dateToString(event.getEventDate()),
                new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }

    public static Event fromEventNewDto(EventNewDto eventNewDto) {
        return new Event(eventNewDto.getEventId(),
                eventNewDto.getAnnotation(),
                new Category(eventNewDto.getCategory(), null),
                0,
                LocalDateTime.now(),
                eventNewDto.getDescription(),
                dateFormatterCustom.stringToDate(eventNewDto.getEventDate()),
                null,
                eventNewDto.getLocation(),
                eventNewDto.getPaid(),
                eventNewDto.getParticipantLimit(),
                null,
                eventNewDto.getRequestModeration(),
                State.PENDING,
                eventNewDto.getTitle(),
                0,
                null);
    }

    public static List<EventFullDto> toEventFullDtoPageList(Page<Event> events) {
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    public static List<EventShortDto> toEventShortDtoPageList(Page<Event> events) {
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public static List<EventShortDto> toEventShortDtoList(List<Event> events) {
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }
}
