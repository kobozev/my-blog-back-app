package ru.practicum.kobozevva.blog.repository;

import ru.practicum.kobozevva.blog.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepositoryCustom {
    Map<Long, Long> countGroupedByPostId(List<Long> postIds);

    Map<Long, List<Comment>> findGroupedByPostId(List<Long> postIds);
}