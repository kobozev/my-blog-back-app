package ru.practicum.kobozevva.blog.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.kobozevva.blog.dto.request.PostCreateRequestDto;
import ru.practicum.kobozevva.blog.dto.request.PostUpdateRequestDto;
import ru.practicum.kobozevva.blog.dto.response.PostResponseDto;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.model.Tag;

import java.util.List;

@Component
public final class PostMapper {

    private PostMapper() {
    }

    public PostResponseDto toResponse(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getText(),
                toTagNames(post.getTags()),
                post.getLikesCount(),
                post.getCommentsCount()
        );
    }

    public Post fromCreateRequest(PostCreateRequestDto request) {
        Post post = new Post();
        post.setTitle(request.title());
        post.setText(request.text());
//        post.setTags(null);
        post.setLikesCount(0);
        post.setCommentsCount(0);
        return post;
    }

    public Post fromUpdateRequest(PostUpdateRequestDto request) {
        Post post = new Post();
        post.setId(request.id());
        post.setTitle(request.title());
        post.setText(request.text());
//        post.setTags(null);
        return post;
    }

    private List<String> toTagNames(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        return tags.stream()
                .map(Tag::getName)
                .toList();
    }
}