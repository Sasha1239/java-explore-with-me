package ru.practicum.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.service.EventService;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.Status;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = {@Lazy})
public class RequestServiceImpl implements RequestService {
    private RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventService;


    //Создание запроса от пользователя на событие
    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        Event event = eventService.getEventPrivate(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Нельзя добавить запрос на участие в своем событии");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ValidationException("Достигнут лимит запросов на участие");
        }

        Request request = new Request();

        if (event.getRequestModeration()) {
            request.setStatus(Status.PENDING);
        } else {
            request.setStatus(Status.CONFIRMED);
            eventService.increaseConfirmedRequestsPrivate(event);
        }

        request.setEvent(event);
        request.setRequester(userService.getUser(userId));
        request.setCreated(LocalDateTime.now());

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    //Отменена заявки пользователем
    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = validationRequest(requestId);

        if (!request.getRequester().getId().equals(userId)) {
            throw new ValidationException("Нельзя отменить чужую заявку");
        }
        if (request.getStatus().equals(Status.REJECTED) || request.getStatus().equals(Status.CANCELED)) {
            throw new ValidationException("Заявка уже отклонена");
        }
        if (request.getStatus().equals(Status.CONFIRMED)) {
            eventService.decreaseConfirmedRequestsPrivate(eventService.getEventPrivate(request.getEvent().getId()));
        }

        request.setStatus(Status.CANCELED);

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    //Получение заявок пользователя
    @Override
    public List<RequestDto> getAllRequestsUser(Long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);

        return RequestMapper.toRequestDtoList(requests);
    }

    @Override
    public List<RequestDto> getAllRequestsEvent(Long eventId) {
        List<Request> requests = requestRepository.findAllByEventId(eventId);

        return RequestMapper.toRequestDtoList(requests);
    }

    @Override
    public void addRequest(Request request) {
        requestRepository.save(request);
    }

    @Override
    public Request getRequest(Long requestId) {
        return validationRequest(requestId);
    }

    @Override
    public void rejectAllRequestEvent(Long eventId) {
        requestRepository.rejectAll(eventId);
    }

    private Request validationRequest(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Неверный идентификатор запроса"));
    }
}
