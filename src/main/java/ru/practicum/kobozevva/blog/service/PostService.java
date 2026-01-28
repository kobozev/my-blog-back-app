package ru.practicum.kobozevva.blog.service;

import ru.practicum.kobozevva.blog.model.Post;

import java.util.List;

public interface PostService {

    List<Post> searchPosts(String search, int pageNumber, int pageSize);

    int getLastPage(String search, int pageSize);

    Post getPostById(long postId);

    Post createPost(Post post);

    Post updatePost(Post post);

    void deletePost(long postId);

    int addLike(long postId);
}