package ru.practicum.comments.dto;

import org.springframework.stereotype.Component;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

@Component
public class CommentMapper {
    public static CommentDto toCommentFullDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getUser().getId(), comment.getEvent().getId());
    }

    public static Comment fromShortCommentDto(CommentShortDto commentShortDto, User user, Event event) {
        return new Comment(commentShortDto.getId(), commentShortDto.getText(), user, event);
    }
}
