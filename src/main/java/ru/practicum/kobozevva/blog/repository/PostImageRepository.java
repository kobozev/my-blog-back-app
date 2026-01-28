package ru.practicum.kobozevva.blog.repository;

import ru.practicum.kobozevva.blog.model.PostImage;

import java.util.Optional;

public interface PostImageRepository {

    Optional<PostImage> findByPostId(long postId);

    void saveOrUpdate(PostImage postImage);
}