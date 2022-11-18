package ru.practicum.compilations.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationNewDto;
import ru.practicum.compilations.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@Slf4j
public class CompilationController {
    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    public CompilationDto createCompilation(@RequestBody CompilationNewDto compilationNewDto) {
        log.info("Добавлена подборка событий {}", compilationNewDto);
        return compilationService.createCompilation(compilationNewDto);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("Получена подборка событий с id {}", compId);
        return compilationService.getCompilation(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAllCompilations(@RequestParam(value = "pinned", required = false) boolean pinned,
                                                   @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                                   Integer from,
                                                   @Positive @RequestParam(value = "size", defaultValue = "10")
                                                   Integer size) {
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Удалена подборка событий с id {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addCompilationEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("В подборку с id {} добавлено событие с id {}", compId, eventId);
        compilationService.addCompilationEvent(compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteCompilationEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Из подборки с id {} удалено событие с id {}", compId, eventId);
        compilationService.deleteCompilationEvent(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Подборка с id закреплена {}", compId);
        compilationService.pinCompilation(true, compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Подборка с id откреплена {}", compId);
        compilationService.pinCompilation(false, compId);
    }
}
