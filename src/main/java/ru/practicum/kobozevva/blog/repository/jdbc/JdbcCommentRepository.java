package ru.practicum.kobozevva.blog.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.kobozevva.blog.model.Comment;
import ru.practicum.kobozevva.blog.repository.CommentRepository;
import ru.practicum.kobozevva.blog.repository.rowmapper.CommentRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class JdbcCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CommentRowMapper rowMapper = new CommentRowMapper();
    private static final Logger log =
            LoggerFactory.getLogger(JdbcCommentRepository.class);

    public JdbcCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findByPostId(long postId) {
        log.debug("DB: find comments by postId={}", postId);

        String sql = """
                    SELECT id, post_id, text, created_at, updated_at
                    FROM comments
                    WHERE post_id = ?
                    ORDER BY created_at
                """;

        List<Comment> comments = jdbcTemplate.query(sql, rowMapper, postId);
        log.debug("DB: found {} comments for postId={}", comments.size(), postId);
        return comments;
    }

    @Override
    public Optional<Comment> findById(long postId, long commentId) {
        log.debug("DB: find comment id={} for postId={}", commentId, postId);

        String sql = """
                    SELECT id, post_id, text, created_at, updated_at
                    FROM comments
                    WHERE post_id = ? and id = ?
                """;

        Optional<Comment> comment = jdbcTemplate.query(sql, rowMapper, postId, commentId)
                .stream()
                .findFirst();

        log.debug("DB: comment id={} for postId{} -> found={}", commentId, postId, !comment.isEmpty());
        return comment;
    }

    @Override
    public Comment save(Comment comment) {
        log.debug("DB: save comment for postId={}", comment.getPostId());

        String sql = """
                    INSERT INTO comments (post_id, text)
                    VALUES (?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, comment.getPostId());
            ps.setString(2, comment.getText());
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        comment.setId(((Number) keys.get("id")).longValue());

        log.debug("DB: saved comment id={} for postId{}", comment.getId(), comment.getPostId());
        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        log.debug(
                "DB: update comment id={} for postId='{}' with text='{}'",
                comment.getId(), comment.getPostId(), comment.getText()
        );

        String sql = """
                    UPDATE comments
                    SET text = ?, updated_at = now()
                    WHERE id = ? and post_id = ?
                """;

        jdbcTemplate.update(
                sql,
                comment.getText(),
                comment.getId(),
                comment.getPostId()
        );

        log.debug("DB: updated comment id={} for postId{}", comment.getId(), comment.getPostId());
        return comment;
    }

    @Override
    public void delete(long postId, long commentId) {
        log.debug("DB: delete comment id={} for postId{}", commentId, postId);

        String sql = """
                    DELETE FROM comments
                    WHERE post_id = ? and id = ?
                """;

        jdbcTemplate.update(sql, postId, commentId);
        log.debug("DB: deleted comment id={} for postId{}", commentId, postId);
    }
}