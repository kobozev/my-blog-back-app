package ru.practicum.kobozevva.blog.service;

import ru.practicum.kobozevva.blog.model.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getTagsByPost(long postId);

    void replacePostTags(long postId, List<String> tagNames);
}