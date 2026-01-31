package ru.practicum.kobozevva.blog.dto.response;

import java.util.List;

public record PostListResponseDto(
        List<PostResponseDto> posts,
        boolean hasPrev,
        boolean hasNext,
        int lastPage
) {
}