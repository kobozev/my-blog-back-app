package ru.practicum.kobozevva.blog.repository;

import ru.practicum.kobozevva.blog.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(long id);

    List<Post> search(String search, int pageNumber, int pageSize);

    int countBySearch(String search);

    Post save(Post post);

    void update(Post post);

    void deleteById(long id);

    boolean existsById(long id);

    int incrementLikes(long postId);
}