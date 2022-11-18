package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventNewDto {
    private Long eventId;

    @NotBlank(message = "Краткое описание события не может быть пустым")
    @Size(max = 2000, min = 20)
    private String annotation;

    private Long category;

    @NotBlank(message = "Описание события не может быть пустым")
    @Size(max = 7000, min = 20)
    private String description;

    private String eventDate;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;

    @Size(max = 120, min = 3)
    private String title;
}
