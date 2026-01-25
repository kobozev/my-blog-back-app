package ru.practicum.kobozevva.blog.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Like {

    private final Long id;
    private final Long postId;
    private final OffsetDateTime createdAt;

    public Like(Long id, Long postId, OffsetDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Like like)) return false;
        return Objects.equals(id, like.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}