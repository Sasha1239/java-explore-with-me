package ru.practicum.compilations.dto;

import org.springframework.stereotype.Component;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.events.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    private final EventMapper eventMapper = new EventMapper();

    public CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(),
                eventMapper.toEventShortDtoList(compilation.getEvents()),
                compilation.isPinned(),
                compilation.getTitle());
    }

    public Compilation fromCompilationDto(CompilationNewDto compilationNewDto) {
        return new Compilation(compilationNewDto.getId(),
                getIds(compilationNewDto.getEvents()),
                compilationNewDto.isPinned(),
                compilationNewDto.getTitle());
    }

    public List<CompilationDto> toCompilationDtoList(List<Compilation> compilations) {
        return compilations.stream().map(this::toCompilationDto).collect(Collectors.toList());
    }

    private List<Event> getIds(List<Long> ids) {
        List<Event> events = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            events.add(new Event());
            events.get(i).setId(ids.get(i));
        }

        return events;
    }
}
