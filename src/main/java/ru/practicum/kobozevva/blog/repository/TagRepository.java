package ru.practicum.kobozevva.blog.repository;

import ru.practicum.kobozevva.blog.model.Tag;

import java.util.Collection;
import java.util.List;

public interface TagRepository {

    List<Tag> findByPostId(long postId);

    List<Tag> findOrCreate(Collection<String> tagNames);

    void bindTagsToPost(long postId, List<Tag> tags);

    void deleteBindingsByPostId(long postId);
}