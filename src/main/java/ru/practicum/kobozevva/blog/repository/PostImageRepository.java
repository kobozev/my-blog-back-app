package ru.practicum.kobozevva.blog.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.kobozevva.blog.model.PostImage;

import java.util.Optional;

public interface PostImageRepository extends CrudRepository<PostImage, Long> {
    Optional<PostImage> findByPostId(Long postId);
}