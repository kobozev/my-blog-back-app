package ru.practicum.kobozevva.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.kobozevva.blog.dto.request.CommentCreateRequestDto;
import ru.practicum.kobozevva.blog.dto.request.CommentUpdateRequestDto;
import ru.practicum.kobozevva.blog.dto.response.CommentResponseDto;
import ru.practicum.kobozevva.blog.mapper.CommentMapper;
import ru.practicum.kobozevva.blog.model.Comment;
import ru.practicum.kobozevva.blog.service.CommentService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private static final Logger log =
            LoggerFactory.getLogger(CommentController.class);

    public CommentController(CommentService commentService,
                             CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    // GET /api/posts/{postId}/comments
    @GetMapping
    public List<CommentResponseDto> getComments(@PathVariable long postId) {
        log.debug("HTTP GET /api/posts/{}/comments", postId);

        List<CommentResponseDto> comments = commentService.getCommentsByPost(postId).stream()
                .map(commentMapper::toResponse)
                .toList();
        log.debug("Post id={} comments -> found={}", postId, comments.size());

        return comments;
    }

     // GET /api/posts/{postId}/comments/{commentId}
    @GetMapping("/{commentId}")
    public CommentResponseDto getComment(@PathVariable long postId,
                                         @PathVariable long commentId) {

        log.debug("GET /api/posts/{}/comments/{}", postId, commentId);

        Comment comment = commentService.getComment(postId, commentId);

        log.debug(
                "Comment id={} for postId={} -> textLength={}",
                postId,
                commentId,
                comment.getText() != null ? comment.getText().length() : 0
        );

        return commentMapper.toResponse(comment);
    }

    // POST /api/posts/{postId}/comments
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@PathVariable long postId,
                                            @RequestBody CommentCreateRequestDto request) {
        if (request == null || request.text() == null || request.text().isBlank()) {
            throw new IllegalArgumentException("Comment text must not be empty");
        }

        log.debug(
                "POST /api/posts/{}/comments textLength={}",
                postId,
                request.text().length()
        );

        Comment created = commentService.createComment(postId, request.text());

        log.debug("Comment id={} for postId={} created", created.getId(), created.getPostId());

        return commentMapper.toResponse(created);
    }

    // PUT /api/posts/{postId}/comments/{commentId}
    @PutMapping("/{commentId}")
    public CommentResponseDto updateComment(@PathVariable long postId,
                                            @PathVariable long commentId,
                                            @RequestBody CommentUpdateRequestDto request) {
        if (request ==null || request.text() == null || request.text().isBlank()) {
            throw new IllegalArgumentException("Comment text must not be empty");
        }

        log.debug(
                "PUT /api/posts/{postId}/comments/{commentId} textLength={}",
                postId,
                request.text().length()
        );

        Comment updated = commentService.updateComment(postId, commentId, request.text());

        log.debug("Comment id={} for postId={} updated", updated.getId(), updated.getPostId());

        return commentMapper.toResponse(updated);
    }

     // DELETE /api/posts/{postId}/comments/{commentId}
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable long postId,
                              @PathVariable long commentId) {
        log.debug("DELETE /api/posts/{}/comments/{}", postId, commentId);

        commentService.deleteComment(postId, commentId);

        log.debug("Comment id={} for postId={} deleted", postId, commentId);
    }
}