package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatisticDto;
import ru.practicum.model.ViewStatisticSpec;
import ru.practicum.service.EndpointHitService;

import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@Slf4j
public class EndpointHitController {

    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    public void addHit(@RequestBody EndpointHitDto endpointHitDto) {
        endpointHitService.addEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatisticDto> getHits(ViewStatisticSpec viewStatisticSpec) {
        return endpointHitService.getStats(viewStatisticSpec);
    }
}
