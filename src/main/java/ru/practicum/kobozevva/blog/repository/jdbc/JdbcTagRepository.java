package ru.practicum.kobozevva.blog.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.kobozevva.blog.model.Tag;
import ru.practicum.kobozevva.blog.repository.TagRepository;
import ru.practicum.kobozevva.blog.repository.rowmapper.TagRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class JdbcTagRepository implements TagRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper rowMapper = new TagRowMapper();

    public JdbcTagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> findByPostId(long postId) {
        String sql = """
            SELECT t.id, t.name
            FROM tags t
            INNER JOIN post_tags pt on t.id = pt.tag_id
            WHERE pt.post_id = ?
            ORDER BY t.name
        """;

        return jdbcTemplate.query(sql, rowMapper, postId);
    }

    @Override
    public List<Tag> findOrCreate(Collection<String> tagNames) {
        if (tagNames.isEmpty()) {
            return List.of();
        }

        List<Tag> result = new ArrayList<>();

        for (String name : tagNames) {
            result.add(findOrCreateSingle(name));
        }

        return result;
    }

    private Tag findOrCreateSingle(String name) {
        String selectSql = """
            SELECT id, name
            FROM tags
            WHERE name = ?
        """;

        List<Tag> existing = jdbcTemplate.query(selectSql, rowMapper, name);
        if (!existing.isEmpty()) {
            return existing.getFirst();
        }

        String insertSql = """
            INSERT INTO tags (name)
            VALUES (?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        return new Tag(((Number) Objects.requireNonNull(keys).get("id")).longValue(), name);
    }

    @Override
    public void bindTagsToPost(long postId, List<Tag> tags) {
        if (tags.isEmpty()) {
            return;
        }

        String sql = """
            INSERT INTO post_tags (post_id, tag_id)
            VALUES (?, ?)
            ON CONFLICT DO NOTHING
        """;

        jdbcTemplate.batchUpdate(
                sql,
                tags,
                tags.size(),
                (ps, tag) -> {
                    ps.setLong(1, postId);
                    ps.setLong(2, tag.getId());
                }
        );
    }

    @Override
    public void deleteBindingsByPostId(long postId) {
        String sql = """
            DELETE FROM post_tags
            WHERE post_id = ?
        """;

        jdbcTemplate.update(sql, postId);
    }
}