package ru.practicum.kobozevva.blog.repository;

import java.util.List;
import java.util.Map;

public interface TagRepositoryCustom {
    List<Long> upsertAndGetIds(List<String> names);

    void assignTagsToPost(Long postId, List<Long> tagIds);

    default void upsertTagsAndAssignToPost(Long postId, List<String> tagNames) {
        List<Long> tagIds = upsertAndGetIds(tagNames);
        assignTagsToPost(postId, tagIds);
    }

    List<String> findByPostId(Long postId);

    Map<Long, List<String>> findGroupedByPostId(List<Long> postIds);
}