package ru.practicum.requests.dto;

import org.springframework.stereotype.Component;
import ru.practicum.requests.model.Request;
import ru.practicum.utilits.DateFormatterCustom;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper {

    private static final DateFormatterCustom dateFormatterCustom = new DateFormatterCustom();

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                dateFormatterCustom.dateToString(request.getCreated()),
                request.getStatus().toString());
    }

    public static List<RequestDto> toRequestDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }
}
