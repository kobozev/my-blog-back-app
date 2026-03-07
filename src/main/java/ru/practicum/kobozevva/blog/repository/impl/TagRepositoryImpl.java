package ru.practicum.kobozevva.blog.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.kobozevva.blog.repository.TagRepositoryCustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepositoryCustom {
    private final NamedParameterJdbcTemplate jdbc;

    private static final String POST_ID_PARAMETER_NAME = "postId" ;

    private static final String SQL_UPSERT_TAGS_BY_NAMES = """
            INSERT INTO "tags"(tag_name)
            SELECT * FROM (VALUES %s) AS v(name)
            WHERE NOT EXISTS (
                SELECT 1 FROM "tags" t WHERE t.tag_name = v.name
            )
            """;

    private static final String SQL_SELECT_TAG_IDS_BY_NAMES = """
            SELECT id, tag_name
            FROM "tags"
            WHERE tag_name IN (:names)
            """;

    private static final String SQL_DELETE_ALL_TAGS_BY_POST_ID = """
        DELETE FROM "post_tags" pt WHERE pt.post_id = :postId
        """;

    private static final String SQL_INSERT_TAGS_BY_POST_ID = """
            INSERT INTO "post_tags"(post_id, tag_id)
            VALUES (:postId, :tagId)
            ON CONFLICT DO NOTHING
            """;

    private static final String SQL_FIND_TAGS_BY_POST_ID = """
            SELECT t.id, t.tag_name
            FROM "tags" t
            JOIN "post_tags" pt ON t.id = pt.tag_id
            WHERE pt.post_id = :postId
            """;

    private static final String SQL_FIND_TAGS_GROUPED_BY_POST_ID = """
            SELECT pt.post_id, t.id, t.tag_name
            FROM "tags" t
            JOIN "post_tags" pt ON t.id = pt.tag_id
            WHERE pt.post_id IN (:postIds)
            ORDER BY pt.post_id, t.id
            """;

    @Override
    public List<Long> upsertAndGetIds(List<String> names) {
        if (names.isEmpty()) {
            return List.of();
        }

        // --- 1. Вставка отсутствующих тегов ---
        // формируем VALUES для SQL
        String values = names.stream()
                .map(n -> "(:name_" + names.indexOf(n) + ")")
                .collect(Collectors.joining(", "));

        // формируем карту параметров
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            params.put("name_" + i, names.get(i));
        }

        jdbc.update(SQL_UPSERT_TAGS_BY_NAMES.formatted(values), params);

        // --- 2. Получаем id всех тегов ---
        return jdbc.query(
                SQL_SELECT_TAG_IDS_BY_NAMES,
                Map.of("names", names),
                (rs, rowNum) -> rs.getLong("id")
        );
    }

    @Override
    public void assignTagsToPost(Long postId, List<Long> tagIds) {
        jdbc.update(SQL_DELETE_ALL_TAGS_BY_POST_ID, Map.of(POST_ID_PARAMETER_NAME, postId));

        var params = tagIds.stream()
                .map(tagId -> Map.of(POST_ID_PARAMETER_NAME, postId, "tagId", tagId))
                .toArray(Map[]::new);

        jdbc.batchUpdate(SQL_INSERT_TAGS_BY_POST_ID, params);
    }

    @Override
    public List<String> findByPostId(Long postId) {
        return jdbc.query(
                SQL_FIND_TAGS_BY_POST_ID,
                Map.of(POST_ID_PARAMETER_NAME, postId),
                (rs, rowNum) -> rs.getString("tag_name")
        );
    }

    @Override
    public Map<Long, List<String>> findGroupedByPostId(List<Long> postIds) {
        return jdbc.query(SQL_FIND_TAGS_GROUPED_BY_POST_ID, Map.of("postIds", postIds),rs -> {
            Map<Long, List<String>> map = new LinkedHashMap<>();
            while (rs.next()) {
                Long postId = rs.getLong("post_id");
                map.computeIfAbsent(postId, k -> new ArrayList<>()).add(rs.getString("tag_name"));
            }
            return map;
        });
    }
}