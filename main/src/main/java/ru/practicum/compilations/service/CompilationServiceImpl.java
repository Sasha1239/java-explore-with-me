package ru.practicum.compilations.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.dto.CompilationNewDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.service.EventService;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.utilits.PageableRequest;

import java.util.List;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;

    private final EventService eventService;

    //Создание подборки
    @Override
    public CompilationDto createCompilation(CompilationNewDto compilationNewDto) {
        if (compilationNewDto.getTitle() == null) {
            throw new ValidationException("Заголовок подборки не может быть пустым");
        }

        Compilation compilation = compilationRepository.save(CompilationMapper.fromCompilationDto(compilationNewDto));
        compilation.getEvents().replaceAll(event -> eventService.getEventPrivate(event.getId()));

        return CompilationMapper.toCompilationDto(compilation);
    }

    //Получение подборки
    @Override
    public CompilationDto getCompilation(Long compilationId) {
        Compilation compilation = validationCompilation(compilationId);

        return CompilationMapper.toCompilationDto(compilation);
    }

    //Получение всех подборок
    @Override
    public List<CompilationDto> getAllCompilations(boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = compilationRepository.findAllByPinnedIs(pinned, PageableRequest.of(from, size));

        return CompilationMapper.toCompilationDtoList(compilations);
    }

    //Удаление подборки
    @Override
    public void deleteCompilation(Long compilationId) {
        compilationRepository.deleteById(compilationId);
    }

    //Добавление события в подборку
    @Override
    @Transactional
    public void addCompilationEvent(Long compilationId, Long eventId) {
        validationCompilation(compilationId);
        compilationRepository.addEvent(compilationId, eventId);
    }

    //Удаление события из подборки
    @Override
    @Transactional
    public void deleteCompilationEvent(Long compilationId, Long eventId) {
        validationCompilation(compilationId);
        compilationRepository.deleteEvent(eventId, compilationId);
    }

    //Закрепление/открепление подборки
    @Override
    @Transactional
    public void pinCompilation(boolean pinned, Long compilationId) {
        validationCompilation(compilationId);
        compilationRepository.pinningCompilation(pinned, compilationId);
    }

    private Compilation validationCompilation(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException("Неверный идентификатор подборки"));
    }
}
