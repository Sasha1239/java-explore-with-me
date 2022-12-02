package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatisticDto;
import ru.practicum.model.ViewStatisticSpec;

import java.util.List;

public interface EndpointHitService {

    void addEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStatisticDto> getStats(ViewStatisticSpec viewStatisticSpec);
}
