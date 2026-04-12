package ru.practicum.kobozevva.blog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.kobozevva.blog.dto.comment.CommentDto;
import ru.practicum.kobozevva.blog.dto.comment.NewCommentDto;
import ru.practicum.kobozevva.blog.dto.comment.UpdateCommentDto;
import ru.practicum.kobozevva.blog.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentDto createComment(@PathVariable("postId") Long postId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("POST /api/posts/{postId}/comments with params(postId {}, newCommentDto {})", postId, newCommentDto);
        return commentService.createComment(postId, newCommentDto);
    }

    @GetMapping("/{id}")
    public CommentDto getCommentById(@PathVariable("postId") Long postId, @PathVariable("id") Long commentId) {
        log.info("GET /api/posts/{postId}/comments/{id} with params(postId {}, id {})", postId, commentId);
        return commentService.getCommentById(postId, commentId);
    }

    @GetMapping
    public List<CommentDto> findComments(@PathVariable("postId") Long postId) {
        log.info("GET /api/posts/{postId}/comments with params(postId {})", postId);
        return commentService.findComments(postId);
    }

    @PutMapping("/{id}")
    public CommentDto updateComment(@PathVariable("postId") Long postId,
                                    @PathVariable("id") Long commentId,
                                    @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("PUT /api/posts/{postId}/comments/{id} with params(postId {}, id {}, updateCommentDto {})",
                postId, commentId, updateCommentDto);
        return commentService.updateComment(postId, commentId, updateCommentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCommentById(@PathVariable("postId") Long postId, @PathVariable("id") Long commentId) {
        log.info("DELETE /api/posts/{postId}/comments/{id} with params(postId {}, id {})", postId, commentId);
        commentService.deleteCommentById(postId, commentId);
    }
}