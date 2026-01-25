package ru.practicum.kobozevva.blog.model;

import java.time.OffsetDateTime;

public class PostImage {

    private final Long postId;
    private final byte[] image;
    private final String contentType;
    private final OffsetDateTime updatedAt;

    public PostImage(Long postId, byte[] image, String contentType, OffsetDateTime updatedAt) {
        this.postId = postId;
        this.image = image;
        this.contentType = contentType;
        this.updatedAt = updatedAt;
    }

    public Long getPostId() {
        return postId;
    }

    public byte[] getImage() {
        return image;
    }

    public String getContentType() {
        return contentType;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}