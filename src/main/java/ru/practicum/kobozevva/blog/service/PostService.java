package ru.practicum.kobozevva.blog.service;

import org.springframework.web.multipart.MultipartFile;
import ru.practicum.kobozevva.blog.dto.post.NewPostDto;
import ru.practicum.kobozevva.blog.dto.post.PostDto;
import ru.practicum.kobozevva.blog.dto.post.PostsDto;
import ru.practicum.kobozevva.blog.dto.post.UpdatePostDto;

public interface PostService {
    PostDto createPost(NewPostDto newPostDto);

    PostDto getPostById(Long postId);

    PostsDto findPosts(String search, Integer pageNumber, Integer pageSize);

    PostDto updatePost(Long postId, UpdatePostDto updatePostDto);

    void deletePostById(Long postId);

    Integer likePost(Long postId);

    void updatePostImage(Long postId, MultipartFile imageFile);

    byte[] getPostImage(Long postId);
}