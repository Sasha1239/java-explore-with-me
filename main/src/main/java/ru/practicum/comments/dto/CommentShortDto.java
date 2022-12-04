package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentShortDto {
    private Long id;

    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
}
