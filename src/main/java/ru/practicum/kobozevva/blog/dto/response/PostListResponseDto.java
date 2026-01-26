package ru.practicum.kobozevva.blog.dto.response;

import ru.practicum.kobozevva.blog.dto.request.PostListItemDto;

import java.util.List;

public record PostListResponseDto(
        List<PostListItemDto> posts,
        boolean hasPrev,
        boolean hasNext,
        int lastPage
) {
}