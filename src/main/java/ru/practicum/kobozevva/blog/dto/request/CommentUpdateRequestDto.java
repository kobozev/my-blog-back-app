package ru.practicum.kobozevva.blog.dto.request;

public record CommentUpdateRequestDto(
        Long id,
        String text,
        Long postId
) {
}