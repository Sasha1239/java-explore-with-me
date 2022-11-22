package ru.practicum.dto;

import org.springframework.stereotype.Component;
import ru.practicum.model.EndpointHit;
import ru.practicum.utilits.DateFormatterCustom;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EndpointHitMapper {
    private static final DateFormatterCustom formatter = new DateFormatterCustom();

    public static ViewStatisticDto toViewStatisticDto(EndpointHit endpointHit) {
        return new ViewStatisticDto(endpointHit.getApp(), endpointHit.getUri(), 0);
    }

    public static EndpointHit fromEndpointHitDto(EndpointHitDto endpointHitDto) {
        return new EndpointHit(null,
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                formatter.stringToDate(endpointHitDto.getTimestamp()));
    }

    public static List<ViewStatisticDto> toDto(List<EndpointHit> endpointHits) {
        return endpointHits
                .stream()
                .map(EndpointHitMapper::toViewStatisticDto)
                .collect(Collectors.toList());
    }
}
