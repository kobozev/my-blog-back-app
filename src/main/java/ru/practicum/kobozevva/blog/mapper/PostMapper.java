package ru.practicum.kobozevva.blog.mapper;

import ru.practicum.kobozevva.blog.dto.request.PostCreateRequestDto;
import ru.practicum.kobozevva.blog.dto.request.PostUpdateRequestDto;
import ru.practicum.kobozevva.blog.dto.response.PostResponseDto;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.model.Tag;

import java.util.List;

public final class PostMapper {

    private PostMapper() {
    }

    public static PostResponseDto toResponse(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getText(),
                toTagNames(post.getTags()),
                post.getLikesCount(),
                post.getCommentsCount()
        );
    }

    public static Post fromCreateRequest(PostCreateRequestDto request, List<Tag> tags) {
        Post post = new Post();
        post.setTitle(request.title());
        post.setText(request.text());
        post.setTags(tags);
        post.setLikesCount(0);
        post.setCommentsCount(0);
        return post;
    }

    public static Post fromUpdateRequest(PostUpdateRequestDto request, List<Tag> tags) {
        Post post = new Post();
        post.setId(request.id());
        post.setTitle(request.title());
        post.setText(request.text());
        post.setTags(tags);
        return post;
    }

    private static List<String> toTagNames(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        return tags.stream()
                .map(Tag::getName)
                .toList();
    }
}