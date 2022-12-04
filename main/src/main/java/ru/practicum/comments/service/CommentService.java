package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(CommentShortDto commentShortDto, Long userId, Long eventId);

    CommentDto getComment(Long commentId);

    List<Comment> getAllUserComments(Long userId);

    List<Comment> getAllEventComments(Long eventId);

    CommentDto updateComment(Long userId, Long commentId, CommentShortDto commentShortDto);

    void deleteComment(Long commentId, Long userId);

    void deleteCommentAdmin(Long commentId, Long userId);
}
