package ru.practicum.comments.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.EventService;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UserService;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final EventService eventService;
    private final UserService userService;

    //Создание комментария
    @Override
    public Comment createComment(CommentShortDto commentShortDto, Long userId, Long eventId) {
        User user = userService.getUser(userId);
        Event event = eventService.getEventPrivate(eventId);

        return commentRepository.save(CommentMapper.fromShortCommentDto(commentShortDto, user, event));
    }

    //Получение комментария
    @Override
    public CommentDto getComment(Long commentId) {
        Comment comment = validationComment(commentId);

        return CommentMapper.toCommentFullDto(comment);
    }

    //Получение всех комментариев пользователя
    @Override
    public List<Comment> getAllUserComments(Long userId) {
        return commentRepository.getAllByUserId(userId);
    }

    //Получение всех комментариев события
    @Override
    public List<Comment> getAllEventComments(Long eventId) {
        return commentRepository.getAllByEventId(eventId);
    }

    //Обновление комменатрия
    @Override
    public CommentDto updateComment(Long userId, Long commentId, CommentShortDto commentShortDto) {
        validationComment(commentId);

        Comment comment = validationUpdateCommentOtherUser(commentId, userId);
        comment.setText(commentShortDto.getText());

        return CommentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    //Удаление комментария
    @Override
    public void deleteComment(Long commentId, Long userId) {
        validationComment(commentId);
        validationUpdateCommentOtherUser(commentId, userId);

        commentRepository.deleteById(commentId);
    }

    //Удаление комментария админом
    @Override
    public void deleteCommentAdmin(Long commentId, Long userId) {
        validationComment(commentId);

        commentRepository.deleteById(commentId);
    }

    private Comment validationComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Неверный идентификатор комментария"));
    }

    private Comment validationUpdateCommentOtherUser(Long commentId, Long userId) {
        Comment comment = validationComment(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new ValidationException("Нельзя изменить/удалить комментарий другого пользователя");
        }

        return comment;
    }
}
