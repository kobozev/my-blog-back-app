package ru.practicum.kobozevva.blog.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Comment {

    private Long id;
    private Long postId;
    private String text;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Comment() {
    }

    public Comment(Long id,
                   Long postId,
                   String text,
                   OffsetDateTime createdAt,
                   OffsetDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}