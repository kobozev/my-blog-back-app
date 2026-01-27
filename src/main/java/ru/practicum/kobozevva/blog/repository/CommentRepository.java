package ru.practicum.kobozevva.blog.repository;

import ru.practicum.kobozevva.blog.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findByPostId(long postId);

    Optional<Comment> findById(long postId, long commentId);

    Comment save(Comment comment);

    Comment update(Comment comment);

    void delete(long postId, long commentId);
}