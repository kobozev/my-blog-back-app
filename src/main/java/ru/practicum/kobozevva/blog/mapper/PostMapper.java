package ru.practicum.kobozevva.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.kobozevva.blog.dto.post.NewPostDto;
import ru.practicum.kobozevva.blog.dto.post.PostDto;
import ru.practicum.kobozevva.blog.dto.post.PostPreviewDto;
import ru.practicum.kobozevva.blog.dto.post.UpdatePostDto;
import ru.practicum.kobozevva.blog.model.Post;

import java.util.List;

@Mapper
public interface PostMapper {
    PostDto toDto(Post post, Long commentsCount, List<String> tags);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "commentsCount", ignore = true)
    PostPreviewDto toPreviewDto(Post post);

    List<PostPreviewDto> toPreviewDto(List<Post> posts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likesCount", ignore = true)
    Post update(@MappingTarget Post post, UpdatePostDto updatePostDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likesCount", ignore = true)
    Post toEntity(NewPostDto newPostDto);
}