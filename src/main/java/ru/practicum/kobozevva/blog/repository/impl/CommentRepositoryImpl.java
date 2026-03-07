package ru.practicum.kobozevva.blog.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.kobozevva.blog.model.Comment;
import ru.practicum.kobozevva.blog.repository.CommentRepositoryCustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final NamedParameterJdbcTemplate jdbc;

    private static final String SQL_COUNT_GROUPED_BY_POST_ID = """
            SELECT post_id, count(*) AS cnt
            FROM "post_comments"
            WHERE post_id IN (:postIds)
            GROUP BY post_id
        """;

    private static final String SQL_FIND_GROUPED_BY_POST_ID = """
            SELECT id, post_id, text
            FROM "post_comments"
            WHERE post_id IN (:postIds)
            ORDER BY post_id, id
        """;

    @Override
    public Map<Long, Long> countGroupedByPostId(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Map.of();
        }

        return jdbc.query(SQL_COUNT_GROUPED_BY_POST_ID, Map.of("postIds", postIds),rs -> {
            Map<Long, Long> map = new HashMap<>();

            while (rs.next()) {
                map.put(rs.getLong("post_id"), rs.getLong("cnt"));
            }

            return map;
        });
    }

    @Override
    public Map<Long, List<Comment>> findGroupedByPostId(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Map.of();
        }

        return jdbc.query(SQL_FIND_GROUPED_BY_POST_ID, Map.of("postIds", postIds),rs -> {
            Map<Long, List<Comment>> map = new LinkedHashMap<>(); // preserving order

            while (rs.next()) {
                Long postId = rs.getLong("post_id");

                Comment c = new Comment();
                c.setId(rs.getLong("id"));
                c.setPostId(postId);
                c.setText(rs.getString("text"));

                map.computeIfAbsent(postId, k -> new ArrayList<>()).add(c);
            }

            return map;
        });
    }
}