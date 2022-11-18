package ru.practicum.events.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.events.dto.EndpointHitDto;
import ru.practicum.events.dto.ViewStatisticDto;
import ru.practicum.utilits.DateFormatterCustom;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventClient {
    private final RestTemplate restTemplateBuilder;
    private final DateFormatterCustom dateFormatterCustom;

    public EventClient(@Value("${statistic.url}") String url, RestTemplateBuilder restTemplateBuilder,
                       DateFormatterCustom dateFormatterCustom) {
        this.dateFormatterCustom = dateFormatterCustom;
        this.restTemplateBuilder = restTemplateBuilder.uriTemplateHandler(new DefaultUriBuilderFactory(url)).build();
    }

    public void addHit(HttpServletRequest request) {
        restTemplateBuilder.postForEntity("/hit", getHttpEntity(makeEndpointHit(request)), EndpointHitDto.class);
    }

    public ResponseEntity<List<ViewStatisticDto>> getHits(LocalDateTime start, LocalDateTime end, String[] uris,
                                                          boolean unique) {
        return restTemplateBuilder.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET, getHttpEntity(null), new ParameterizedTypeReference<>() {}, encodeDate(start),
                encodeDate(end), uris, unique);
    }

    private EndpointHitDto makeEndpointHit(HttpServletRequest httpServletRequest) {
        return new EndpointHitDto("ewm-main-service", httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(), dateFormatterCustom.dateToString(LocalDateTime.now()));
    }

    private <T> HttpEntity<T> getHttpEntity(T dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return dto == null ? new HttpEntity<>(headers) : new HttpEntity<>(dto, headers);
    }

    private String encodeDate(LocalDateTime date) {
        return URLEncoder.encode(dateFormatterCustom.dateToString(date), StandardCharsets.UTF_8);
    }
}
