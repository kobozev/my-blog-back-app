package ru.practicum.kobozevva.blog.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.repository.PostRepositoryCustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Post> findPosts(ParsedSearch parsedSearch, int offset, int limit) {
        Pair<String, Map<String, Object>> whereAndParams = buildWhereAndParams(parsedSearch);

        String sql = """
                    SELECT p.id, p.title, p.text, p.likes_count
                    FROM "posts" p
                """ + whereAndParams.getFirst() +
                " ORDER BY p.id LIMIT :limit OFFSET :offset";

        whereAndParams.getSecond().put("limit", limit);
        whereAndParams.getSecond().put("offset", offset);

        return jdbc.query(sql, whereAndParams.getSecond(), (rs, rowNum) ->
                Post.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .text(rs.getString("text"))
                        .likesCount(rs.getInt("likes_count"))
                        .build()
        );
    }

    @Override
    public Long countPosts(ParsedSearch parsedSearch) {
        Pair<String, Map<String, Object>> whereAndParams = buildWhereAndParams(parsedSearch);

        String sql = """
                    SELECT count (*)
                    FROM "posts" p
                """ + whereAndParams.getFirst();

        return jdbc.queryForObject(sql, whereAndParams.getSecond(), Long.class);
    }

    @Override
    public ParsedSearch parseSearch(String search) {
        if (search == null || search.isBlank()) {
            return new ParsedSearch(List.of(), null);
        }

        String[] words = search.split("\\s+");
        List<String> tags = new ArrayList<>();
        List<String> titleWords = new ArrayList<>();

        for (String word : words) {
            if (word.isBlank()) continue;
            if (word.startsWith("#")) {
                if (word.length() > 1) {
                    tags.add(word.substring(1));
                }
            } else {
                titleWords.add(word);
            }
        }

        String titleSubstring = titleWords.isEmpty() ? null : String.join(" ", titleWords);

        return new ParsedSearch(tags, titleSubstring);
    }

    private Pair<String, Map<String, Object>> buildWhereAndParams(ParsedSearch parsedSearch) {
        Map<String, Object> params = new HashMap<>();

        if (parsedSearch.titleSubstring() == null && parsedSearch.tags().isEmpty()) {
            return Pair.of("", params);
        }

        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        if (parsedSearch.titleSubstring() != null) {
            where.append(" AND LOWER(p.title) LIKE LOWER(CONCAT('%', :titleSubstring, '%')) ");
            params.put("titleSubstring", parsedSearch.titleSubstring());
        }

        if (!parsedSearch.tags().isEmpty()) {
            where.append("""
            AND p.id IN (
                SELECT pt.post_id
                FROM "post_tags" pt
                JOIN "tags" t ON t.id = pt.tag_id
                WHERE t.tag_name IN (:tags)
                GROUP BY pt.post_id
                HAVING COUNT(DISTINCT t.tag_name) = :tagCount
            )
            """);
            params.put("tags", parsedSearch.tags());
            params.put("tagCount", parsedSearch.tags().size());
        }

        return Pair.of(where.toString(), params);
    }
}