package ru.practicum.comments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@AllArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/users/{userId}/comments/events/{eventId}")
    public Comment createComment(@PathVariable(value = "userId") @Positive Long userId,
                                 @PathVariable(value = "eventId") @Positive Long eventId,
                                 @RequestBody @Valid CommentShortDto commentShortDto) {
        log.info("Создан комментарий");
        return commentService.createComment(commentShortDto, userId, eventId);
    }

    @GetMapping("comments/{commentId}")
    public CommentDto getComment(@PathVariable Long commentId) {
        log.info("Получен комментарий");
        return commentService.getComment(commentId);
    }

    @PatchMapping("/{userId}/{commentId}")
    public CommentDto updateComment(@RequestBody CommentShortDto commentShortDto, @PathVariable Long commentId,
                                    @PathVariable Long userId) {
        log.info("Обновлен комментарий");
        return commentService.updateComment(userId, commentId, commentShortDto);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @PathVariable Long userId) {
        log.info("Удален комментарий");
        commentService.deleteComment(commentId, userId);
    }
}
