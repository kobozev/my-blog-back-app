package ru.practicum.kobozevva.blog.service;

import ru.practicum.kobozevva.blog.dto.comment.CommentDto;
import ru.practicum.kobozevva.blog.dto.comment.NewCommentDto;
import ru.practicum.kobozevva.blog.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long postId, NewCommentDto newCommentDto);

    CommentDto getCommentById(Long postId, Long commentId);

    List<CommentDto> findComments(Long postId);

    CommentDto updateComment(Long postId, Long commentId, UpdateCommentDto updateCommentDto);

    void deleteCommentById(Long postId, Long commentId);
}