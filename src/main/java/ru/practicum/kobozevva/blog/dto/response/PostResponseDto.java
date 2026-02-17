package ru.practicum.kobozevva.blog.dto.response;

import java.util.List;

public record PostResponseDto(
        Long id,
        String title,
        String text,          // полный Markdown
        List<String> tags,
        int likesCount,
        int commentsCount
) {
}