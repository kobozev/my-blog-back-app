package ru.practicum.kobozevva.blog.dto.error;

public record ErrorResponseDto(
        String message,
        int status
) {}