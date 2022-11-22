package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitMapper;
import ru.practicum.dto.ViewStatisticDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStatisticSpec;
import ru.practicum.repository.EndpointHitRepository;
import ru.practicum.utilits.DateFormatterCustom;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@AllArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;

    private final DateFormatterCustom dateFormatterCustom;

    @Override
    public void addEndpointHit(EndpointHitDto endpointHitDto) {
        endpointHitRepository.save(EndpointHitMapper.fromEndpointHitDto(endpointHitDto));
    }

    @Override
    public List<ViewStatisticDto> getStats(ViewStatisticSpec viewStatisticSpec) {
        viewStatisticSpec.setStart(dateDecoder(viewStatisticSpec.getStart()));
        viewStatisticSpec.setEnd(dateDecoder(viewStatisticSpec.getEnd()));

        List<EndpointHit> endpointHits;

        if (viewStatisticSpec.getUris() != null && !viewStatisticSpec.isUnique()) {
            endpointHits = endpointHitRepository.findAllByTimestampBetweenAndUriIn(
                    dateFormatterCustom.stringToDate(viewStatisticSpec.getStart()),
                    dateFormatterCustom.stringToDate(viewStatisticSpec.getEnd()), List.of(viewStatisticSpec.getUris()));
        } else if (viewStatisticSpec.getUris() == null && viewStatisticSpec.isUnique()) {
            endpointHits = endpointHitRepository.findAllByUniqueIp(
                    dateFormatterCustom.stringToDate(viewStatisticSpec.getStart()),
                    dateFormatterCustom.stringToDate(viewStatisticSpec.getEnd()));
        } else if (viewStatisticSpec.getUris() != null && viewStatisticSpec.isUnique()) {
            endpointHits = endpointHitRepository.findAllByUrisAndUniqueIp(
                    dateFormatterCustom.stringToDate(viewStatisticSpec.getStart()),
                    dateFormatterCustom.stringToDate(viewStatisticSpec.getEnd()), List.of(viewStatisticSpec.getUris()));
        } else {
            endpointHits = endpointHitRepository.findAllByTimestampBetween(
                    dateFormatterCustom.stringToDate(viewStatisticSpec.getStart()),
                    dateFormatterCustom.stringToDate(viewStatisticSpec.getEnd()));
        }

        List<ViewStatisticDto> viewStats = EndpointHitMapper.toDto(endpointHits);

        for (ViewStatisticDto viewStat : viewStats) {
            viewStat.setHits(endpointHitRepository.getHits(viewStat.getUri()));
        }

        return viewStats;
    }

    private String dateDecoder(String date) {
        return URLDecoder.decode(date, StandardCharsets.UTF_8);
    }
}
