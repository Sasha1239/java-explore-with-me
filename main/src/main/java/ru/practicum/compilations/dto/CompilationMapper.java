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

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(),
                EventMapper.toEventShortDtoList(compilation.getEvents()),
                compilation.isPinned(),
                compilation.getTitle());
    }

    public static Compilation fromCompilationDto(CompilationNewDto compilationNewDto) {
        return new Compilation(compilationNewDto.getId(),
                getIds(compilationNewDto.getEvents()),
                compilationNewDto.isPinned(),
                compilationNewDto.getTitle());
    }

    public static List<CompilationDto> toCompilationDtoList(List<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    private static List<Event> getIds(List<Long> ids) {
        List<Event> events = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            events.add(new Event());
            events.get(i).setId(ids.get(i));
        }

        return events;
    }
}
