package ru.practicum.requests.service;

import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.model.Request;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getAllRequestsUser(Long userId);

    List<RequestDto> getAllRequestsEvent(Long eventId);

    void addRequest(Request request);

    Request getRequest(Long requestId);

    void rejectAllRequestEvent(Long eventId);
}
