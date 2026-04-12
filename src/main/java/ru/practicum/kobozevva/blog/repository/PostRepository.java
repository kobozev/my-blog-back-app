package ru.practicum.kobozevva.blog.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.kobozevva.blog.model.Post;

public interface PostRepository extends CrudRepository<Post, Long>, PostRepositoryCustom {
}