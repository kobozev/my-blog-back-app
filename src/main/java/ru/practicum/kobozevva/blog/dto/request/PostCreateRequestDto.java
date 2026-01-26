package ru.practicum.kobozevva.blog.dto.request;

import java.util.List;

public record PostCreateRequestDto(
        String title,
        String text,
        List<String> tags
) {
}