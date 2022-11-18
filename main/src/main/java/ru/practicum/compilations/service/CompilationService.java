package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationNewDto;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(CompilationNewDto compilationNewDto);

    CompilationDto getCompilation(Long compilationId);

    List<CompilationDto> getAllCompilations(boolean pinned, Integer from, Integer size);

    void deleteCompilation(Long compilationId);

    void addCompilationEvent(Long compilationId, Long eventId);

    void deleteCompilationEvent(Long compilationId, Long eventId);

    void pinCompilation(boolean pinned, Long compilationId);
}
