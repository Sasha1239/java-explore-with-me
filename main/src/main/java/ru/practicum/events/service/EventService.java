package ru.practicum.events.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventNewDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.requests.dto.RequestDto;

import java.util.List;
import java.util.Optional;

public interface EventService {
    EventFullDto publishEventAdmin(Long eventId);

    EventFullDto updateEventAdmin(Long eventId, EventNewDto eventNewDto);

    EventFullDto rejectEventAdmin(Long eventId);

    EventFullDto createPrivateEvent(Long userId, EventNewDto eventNewDto);

    EventFullDto updatePrivateEvent(Long userId, EventNewDto eventNewDto);

    List<EventShortDto> getAllPrivateEvent(Long userId, Integer from, Integer size);

    EventFullDto getPrivateEvent(Long userId, Long eventId);

    EventFullDto cancelPrivateEvent(Long userId, Long eventId);

    List<RequestDto> getEventRequestsPrivate(Long userId, Long eventId);

    RequestDto confirmRequestPrivate(Long userId, Long eventId, Long requestId);

    RequestDto rejectRequestPrivate(Long userId, Long eventId, Long requestId);

    Event getEventPrivate(Long eventId);

    List<EventShortDto> getAllPublicEvents(Optional<String> textOptional,
                                           Optional<List<Long>> categoriesOptional,
                                           Optional<Boolean> paidOptional,
                                           Optional<String> rangeStartOptional,
                                           Optional<String> rangeEndOptional,
                                           Optional<Boolean> onlyAvailableOptional,
                                           Optional<String> sortOptional,
                                           int from,
                                           int size);

    EventFullDto getPublicEvent(Long eventId);

    List<EventFullDto> getAllEventsAdmin(Optional<List<Long>> usersOptional,
                                         Optional<List<String>> statesOptional,
                                         Optional<List<Long>> categoriesOptional,
                                         Optional<String> rangeStartOptional,
                                         Optional<String> rangeEndOptional,
                                         int from,
                                         int size);
}
