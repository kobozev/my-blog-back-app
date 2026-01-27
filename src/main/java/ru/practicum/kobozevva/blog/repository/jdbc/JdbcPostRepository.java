package ru.practicum.kobozevva.blog.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.repository.extractor.PostWithTagsExtractor;
import ru.practicum.kobozevva.blog.repository.rowmapper.PostRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PostRowMapper postRowMapper = new PostRowMapper();
    private final PostWithTagsExtractor postWithTagsExtractor = new PostWithTagsExtractor();
    private static final Logger log =
            LoggerFactory.getLogger(JdbcPostRepository.class);

    public JdbcPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Post> findById(long id) {
        log.debug("DB: find post by id={}", id);

        String sql = """
                    SELECT
                        p.id,
                        p.title,
                        p.text,
                        p.likes_count,
                        p.comments_count,
                        p.created_at,
                        p.updated_at,
                        t.id   AS tag_id,
                        t.name AS tag_name
                    FROM posts p
                    LEFT JOIN post_tags pt ON pt.post_id = p.id
                    LEFT JOIN tags t ON t.id = pt.tag_id
                    WHERE p.id = ?
                """;

        Optional<Post> post = Optional.ofNullable(
                jdbcTemplate.query(sql, postWithTagsExtractor, id)
        );

        log.debug("DB: find post by id={} -> found={}", id, !post.isEmpty());
        return post;
    }

    @Override
    public List<Post> search(String search, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;

        log.debug(
                "DB: search posts search='{}', page={}, size={}",
                search, pageNumber, pageSize
        );

        String sql = """
                    SELECT
                        p.id,
                        p.title,
                        p.text,
                        p.likes_count,
                        p.comments_count,
                        p.created_at,
                        p.updated_at
                    FROM posts p
                    WHERE
                        to_tsvector('simple', p.title || ' ' || p.text)
                        @@ plainto_tsquery('simple', ?)
                    ORDER BY p.created_at DESC
                    LIMIT ? OFFSET ?
                """;

        List<Post> posts = jdbcTemplate.query(
                sql,
                postRowMapper,
                search,
                pageSize,
                offset
        );

        log.debug("DB: search result size={}", posts.size());
        return posts;
    }

    @Override
    public int countBySearch(String search) {
        String sql = """
                    SELECT count(*)
                    FROM posts
                    WHERE
                        to_tsvector('simple', title || ' ' || text)
                        @@ plainto_tsquery('simple', ?)
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class, search);
    }

    @Override
    public Post save(Post post) {
        log.debug(
                "DB: save post with title='{}', text='{}', likes_count={}, comments_count={}",
                post.getTitle(),  post.getText(), post.getLikesCount(), post.getCommentsCount()
        );

        String sql = """
                    INSERT INTO posts (title, text, likes_count, comments_count)
                    VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setInt(3, post.getLikesCount());
            ps.setInt(4, post.getCommentsCount());
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        post.setId(((Number) keys.get("id")).longValue());

        log.debug("DB: saved post id={}", post.getId());
        return post;
    }

    @Override
    public void update(Post post) {
        log.debug(
                "DB: update post id={} with title='{}', text='{}'",
                post.getTitle(), post.getText()
        );

        String sql = """
                    UPDATE posts
                    SET title = ?, text = ?, updated_at = now()
                    WHERE id = ?
                """;

        jdbcTemplate.update(
                sql,
                post.getTitle(),
                post.getText(),
                post.getId()
        );

        log.debug("DB: updated post id={}", post.getId());
    }

    @Override
    public void deleteById(long id) {
        log.debug("DB: delete post id={}", id);

        jdbcTemplate.update(
                "DELETE FROM posts WHERE id = ?",
                id
        );

        log.debug("DB: deleted post id={}", id);
    }

    @Override
    public boolean existsById(long id) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(
                        "SELECT exists (SELECT 1 FROM posts WHERE id = ?)",
                        Boolean.class,
                        id
                )
        );
    }

    @Override
    public int incrementLikes(long postId) {
        String sql = """
                    UPDATE posts
                    SET likes_count = likes_count + 1
                    WHERE id = ?
                    RETURNING likes_count
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class, postId);
    }
}