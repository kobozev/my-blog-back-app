package ru.practicum.kobozevva.blog.dto.response;

public record CommentResponseDto(
        Long id,
        String text,
        Long postId
) {
}