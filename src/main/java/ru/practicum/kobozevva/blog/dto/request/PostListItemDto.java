package ru.practicum.kobozevva.blog.dto.request;

import java.util.List;

public record PostListItemDto(
        Long id,
        String title,
        String text,
        List<String> tags,
        int likesCount,
        int commentsCount
) {
}