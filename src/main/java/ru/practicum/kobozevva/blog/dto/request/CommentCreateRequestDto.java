package ru.practicum.kobozevva.blog.dto.request;

public record CommentCreateRequestDto(
        String text,
        Long postId
) {
}