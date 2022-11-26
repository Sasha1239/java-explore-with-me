package ru.practicum.events.service;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.service.CategoryService;
import ru.practicum.events.client.EventClient;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.QEvent;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.Status;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.requests.service.RequestService;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UserService;
import ru.practicum.utilits.DateFormatterCustom;
import ru.practicum.utilits.PageableRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestService requestService;
    private final RequestRepository requestRepository;

    private final EventClient eventClient;
    private final DateFormatterCustom dateFormatterCustom;

    //Публикация события админом
    @Override
    public EventFullDto publishEventAdmin(Long eventId) {
        Event event = validationEvent(eventId);

        if (event.getEventDate().plusHours(1).isAfter(LocalDateTime.now()) && event.getState() == State.PENDING) {
            event.setPublishedOn(LocalDateTime.now());
            event.setState(State.PUBLISHED);
            eventRepository.save(event);
        }

        return EventMapper.toEventFullDto(event);
    }

    //Обновление события админом
    @Override
    public EventFullDto updateEventAdmin(Long eventId, EventNewDto eventNewDto) {
        Event event = validationEvent(eventId);

        validationEventDto(eventNewDto, event);

        Optional.ofNullable(eventNewDto.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(eventNewDto.getRequestModeration()).ifPresent(event::setRequestModeration);

        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    //Получение всех событий админом
    @Override
    public List<EventFullDto> getAllEventsAdmin(Optional<List<Long>> usersOptional,
                                                Optional<List<String>> statesOptional,
                                                Optional<List<Long>> categoriesOptional,
                                                Optional<String> rangeStartOptional,
                                                Optional<String> rangeEndOptional,
                                                int from,
                                                int size) {
        Pageable pageable = PageableRequest.of(from, size);

        BooleanBuilder builder = new BooleanBuilder();

        usersOptional.ifPresent(users -> builder.and(QEvent.event.initiator.id.in(users)));

        statesOptional.ifPresent(states -> builder.and(QEvent.event.state.in(states.stream()
                .map(State::from)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()))));

        categoriesOptional.ifPresent(categories -> builder.and(QEvent.event.category.id.in(categories)));

        rangeStartOptional.ifPresent(start -> builder.and(QEvent.event.eventDate
                .after(dateFormatterCustom.stringToDate(start))));

        rangeEndOptional.ifPresent(end -> builder.and(QEvent.event.eventDate
                .before(dateFormatterCustom.stringToDate(end))));

        return EventMapper.toEventFullDtoPageList(eventRepository.findAll(builder, pageable));
    }

    //Отклонение события админом
    @Override
    public EventFullDto rejectEventAdmin(Long eventId) {
        Event event = validationEvent(eventId);

        if (event.getState() == State.PENDING) {
            event.setState(State.CANCELED);
            eventRepository.save(event);
        }

        return EventMapper.toEventFullDto(event);
    }


    //Создание события
    @Override
    public EventFullDto createPrivateEvent(Long userId, EventNewDto eventNewDto) {
        validationEventDate(eventNewDto.getEventDate());

        if (eventNewDto.getAnnotation() == null) {
            throw new ValidationException("Краткое описание события не может быть пустым");
        }
        if (eventNewDto.getDescription() == null) {
            throw new ValidationException("Описание события не может быть пустым");
        }

        Event event = EventMapper.fromEventNewDto(eventNewDto);

        event.setCategory(CategoryMapper.fromCategoryDto(categoryService.getCategory(eventNewDto.getCategory())));
        event.setInitiator(UserMapper.fromFullDtoToUser(userService.getAllUsers(new Long[]{userId}, 0, 1)
                .get(0)));

        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    //Обновление события
    @Override
    public EventFullDto updatePrivateEvent(Long userId, EventNewDto eventNewDto) {
        Event event = validationEvent(eventNewDto.getEventId());
        validationEventDto(eventNewDto, event);

        if (!userId.equals(event.getInitiator().getId())) {
            throw new ValidationException("Редактировать событие может только его инициатор");
        }
        if (event.getState() == State.PUBLISHED) {
            throw new ValidationException("Редактировать можно только отмененные события или события которые находятся" +
                    " на модерации");
        }
        if (event.getState() == State.CANCELED) {
            event.setState(State.PENDING);
        }

        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    //Получение всех событий пользователя
    @Override
    public List<EventShortDto> getAllPrivateEvent(Long userId, Integer from, Integer size) {
        Pageable pageable = PageableRequest.of(from, size, Sort.unsorted());

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);

        return EventMapper.toEventShortDtoList(events);
    }

    //Получение события пользователем
    @Override
    public EventFullDto getPrivateEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);

        if (event == null) {
            throw new NotFoundException("Такого события не существует");
        }

        return EventMapper.toEventFullDto(event);
    }

    //Отмена события пользователем
    @Override
    public EventFullDto cancelPrivateEvent(Long userId, Long eventId) {
        Event event = validationEvent(eventId);

        if (!userId.equals(event.getInitiator().getId())) {
            throw new ValidationException("Отменить событие может только инициатор");
        }
        if (event.getState().equals(State.CANCELED) || event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Отменить событие можнот только то, которое находится на модерации");
        }

        event.setState(State.CANCELED);

        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    //Получение запросов на участие в событии пользователя
    @Override
    public List<RequestDto> getEventRequestsPrivate(Long userId, Long eventId) {
        Event event = validationEvent(eventId);

        if (!userId.equals(event.getInitiator().getId())) {
            throw new ValidationException("Просматривать заявки на событие доступно только инициатору");
        }

        return requestService.getAllRequestsEvent(eventId);
    }

    //Подтверждение заявки
    @Override
    public RequestDto confirmRequestPrivate(Long userId, Long eventId, Long requestId) {
        Event event = validationEvent(eventId);
        Request request = requestService.getRequest(requestId);
        validationUpdateRequest(event, request);
        validationUpdateRequestUser(userId, event);

        if (event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ValidationException("Достигнут лимит запросов на участие в событии");
        }
        if (event.getParticipantLimit() - event.getConfirmedRequests() == 1) {
            requestRepository.saveAll(requestRepository
                    .findByEvent_IdAndStatus(eventId, Status.PENDING).stream()
                    .peek(e -> e.setStatus(Status.CANCELED))
                    .collect(Collectors.toList()));
        }

        List<Request> requests = requestRepository.findByEvent_IdAndStatus(eventId, Status.CONFIRMED);
        event.setConfirmedRequests(requests.size());

        request.setStatus(Status.CONFIRMED);

        requestService.addRequest(request);

        return RequestMapper.toRequestDto(request);
    }

    //Отклонение заявки
    @Override
    public RequestDto rejectRequestPrivate(Long userId, Long eventId, Long requestId) {
        validationEvent(eventId);

        Request request = requestService.getRequest(requestId);

        if (request.getStatus() == Status.REJECTED || request.getStatus() == Status.CANCELED) {
            throw new ValidationException("Запрос уже отменен администратором");
        }

        request.setStatus(Status.REJECTED);

        requestService.addRequest(request);

        return RequestMapper.toRequestDto(request);
    }

    //Получение события
    @Override
    public Event getEventPrivate(Long eventId) {
        return validationEvent(eventId);
    }

    //Получение всех событий
    @Override
    public List<EventShortDto> getAllPublicEvents(Optional<String> textOptional,
                                                  Optional<List<Long>> categoriesOptional,
                                                  Optional<Boolean> paidOptional,
                                                  Optional<String> rangeStartOptional,
                                                  Optional<String> rangeEndOptional,
                                                  Optional<Boolean> onlyAvailableOptional,
                                                  Optional<String> sortOptional,
                                                  int from,
                                                  int size) {
        BooleanBuilder builder = new BooleanBuilder();
        Pageable pageable = PageableRequest.of(from, size);

        textOptional.ifPresent(text -> builder.and(QEvent.event.annotation.likeIgnoreCase(text)
                .or(QEvent.event.description.likeIgnoreCase(text))));

        categoriesOptional.ifPresent(categories -> builder.and(QEvent.event.category.id.in(categories)));

        paidOptional.ifPresent(paid -> builder.and(QEvent.event.paid.eq(paid)));

        rangeStartOptional.ifPresent(start -> builder.and(QEvent.event.eventDate
                .after(dateFormatterCustom.stringToDate(start))));

        rangeEndOptional.ifPresent(end -> builder.and(QEvent.event.eventDate
                .before(dateFormatterCustom.stringToDate(end))));

        List<Event> events = eventRepository.findAll(builder, pageable).stream().collect(Collectors.toList());

        setViews(events);

        sortOptional.flatMap(s -> Optional.ofNullable(EventSort.from(s))).ifPresent(sort -> {
            switch (sort) {
                case EVENT_DATE:
                    events.sort(Comparator.comparing(Event::getEventDate));
                    break;
                case VIEWS:
                    events.sort(Comparator.comparing(Event::getViews));
                    break;
                default:
                    events.sort(Comparator.comparing(Event::getId));
            }
        });

        return EventMapper.toEventShortDtoList(events);
    }

    //Получение события
    @Override
    public EventFullDto getPublicEvent(Long eventId) {
        Event event = validationEvent(eventId);

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Такого события не существует");
        }

        setViews(List.of(event));

        return EventMapper.toEventFullDto(event);
    }

    private void setViews(List<Event> events) {
        try {
            List<ViewStatisticDto> views = eventClient.getHits(events.stream().map(Event::getId)
                    .collect(Collectors.toList()), false).getBody();

            if (views != null && views.size() > 0) {
                for (Event event : events) {
                    event.setViews(views.get(0).getHits());
                }
            }
        } catch (RestClientException e) {
            log.info("Соединение с сервисом отсутствует");
        }
    }

    private Event validationEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Неверный идентификатор события"));
    }

    private void validationEventDate(String eventDate) {
        String[] lines = eventDate.split(" ");

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(lines[0]), LocalTime.parse(lines[1]));

        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Дата и время начала события не может быть раньше, " +
                    "чем через два часа от текущего момента");
        }
    }

    private void validationEventDto(EventNewDto eventNewDto, Event event) {
        if (eventNewDto.getAnnotation() != null) {
            event.setAnnotation(eventNewDto.getAnnotation());
        }
        if (eventNewDto.getCategory() != null) {
            event.setCategory(CategoryMapper.fromCategoryDto(categoryService.getCategory(eventNewDto.getCategory())));
        }
        if (eventNewDto.getDescription() != null) {
            event.setDescription(eventNewDto.getDescription());
        }
        if (eventNewDto.getEventDate() != null) {
            validationEventDate(eventNewDto.getEventDate());
            event.setEventDate(dateFormatterCustom.stringToDate(eventNewDto.getEventDate()));
        }
        if (eventNewDto.getPaid() != null) {
            event.setPaid(eventNewDto.getPaid());
        }
        if (eventNewDto.getParticipantLimit() != 0) {
            event.setParticipantLimit(eventNewDto.getParticipantLimit());
        }
        if (eventNewDto.getTitle() != null) {
            event.setTitle(eventNewDto.getTitle());
        }
    }

    private void validationUpdateRequest(Event event, Request request) {
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new ValidationException("Этому запросу нельзя изменить статус");
        }
        if (!request.getStatus().equals(Status.PENDING)) {
            throw new ValidationException("Запрос не в статусе ожидания");
        }
        if (!request.getEvent().getId().equals(event.getId())) {
            throw new ValidationException("Запрос не соответствует событию");
        }
    }

    private void validationUpdateRequestUser(Long userId, Event event) {
        User initiator = userService.getUser(userId);
        if (!event.getInitiator().getId().equals(initiator.getId())) {
            throw new ValidationException("Нельзя изменить чужой запрос");
        }
    }
}
