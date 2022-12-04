package ru.practicum.comments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.service.CommentService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/comments")
@AllArgsConstructor
@Slf4j
public class CommentControllerAdmin {
    private final CommentService commentService;

    @GetMapping("/users/{userId}")
    public List<Comment> getAllUserComments(@PathVariable Long userId) {
        return commentService.getAllUserComments(userId);
    }

    @GetMapping("/events/{commentId}")
    public List<Comment> getAllEventComments(@PathVariable Long commentId) {
        return commentService.getAllEventComments(commentId);
    }
}
