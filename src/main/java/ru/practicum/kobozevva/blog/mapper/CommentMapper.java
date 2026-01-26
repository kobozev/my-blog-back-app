package ru.practicum.kobozevva.blog.mapper;

import ru.practicum.kobozevva.blog.dto.request.CommentCreateRequestDto;
import ru.practicum.kobozevva.blog.dto.request.CommentUpdateRequestDto;
import ru.practicum.kobozevva.blog.dto.response.CommentResponseDto;
import ru.practicum.kobozevva.blog.model.Comment;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponseDto toResponse(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getPostId()
        );
    }

    public static Comment fromCreateRequest(CommentCreateRequestDto request) {
        Comment comment = new Comment();
        comment.setPostId(request.postId());
        comment.setText(request.text());
        return comment;
    }

    public static Comment fromUpdateRequest(CommentUpdateRequestDto request) {
        Comment comment = new Comment();
        comment.setId(request.id());
        comment.setPostId(request.postId());
        comment.setText(request.text());
        return comment;
    }
}