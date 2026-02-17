package ru.practicum.kobozevva.blog.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.kobozevva.blog.dto.request.CommentCreateRequestDto;
import ru.practicum.kobozevva.blog.dto.request.CommentUpdateRequestDto;
import ru.practicum.kobozevva.blog.dto.response.CommentResponseDto;
import ru.practicum.kobozevva.blog.model.Comment;

@Component
public final class CommentMapper {

    private CommentMapper() {
    }

    public CommentResponseDto toResponse(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getPostId()
        );
    }

    public Comment fromCreateRequest(CommentCreateRequestDto request) {
        Comment comment = new Comment();
        comment.setPostId(request.postId());
        comment.setText(request.text());
        return comment;
    }

    public Comment fromUpdateRequest(CommentUpdateRequestDto request) {
        Comment comment = new Comment();
        comment.setId(request.id());
        comment.setPostId(request.postId());
        comment.setText(request.text());
        return comment;
    }
}