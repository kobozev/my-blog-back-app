package ru.practicum.kobozevva.blog.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostImageService {

    void updatePostImage(long postId, MultipartFile image);

    byte[] getPostImage(long postId);

    String getContentType(long postId);
}