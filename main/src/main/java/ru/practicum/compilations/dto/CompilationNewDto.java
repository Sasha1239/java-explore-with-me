package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationNewDto {
    private Long id;
    private List<Long> events;
    private boolean pinned;

    @NotBlank(message = "Заголовок подборки не может быть пустым")
    private String title;
}
