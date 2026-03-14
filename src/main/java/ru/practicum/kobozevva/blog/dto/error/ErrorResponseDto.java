package ru.practicum.kobozevva.blog.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErrorResponseDto(int statusCode,
                            String message,
                            String path,

                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                            LocalDateTime timestamp) {
}
