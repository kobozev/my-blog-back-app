package ru.practicum.kobozevva.blog.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.kobozevva.blog.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends CrudRepository<Comment, Long>, CommentRepositoryCustom {
    long countByPostId(Long postId);

    List<Comment> findAllByPostId(Long postId);
}