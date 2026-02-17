package ru.practicum.kobozevva.blog.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.kobozevva.blog.model.PostImage;
import ru.practicum.kobozevva.blog.repository.PostImageRepository;
import ru.practicum.kobozevva.blog.repository.rowmapper.PostImageRowMapper;

import java.util.Optional;

@Repository
public class JdbcPostImageRepository implements PostImageRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PostImageRowMapper rowMapper = new PostImageRowMapper();

    public JdbcPostImageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<PostImage> findByPostId(long postId) {
        String sql = """
            SELECT post_id, image, content_type, updated_at
            FROM post_images
            WHERE post_id = ?
            """;

        return jdbcTemplate.query(sql, rowMapper, postId)
                .stream()
                .findFirst();
    }

    @Override
    public void saveOrUpdate(PostImage postImage) {
        String sql = """
            INSERT INTO post_images (post_id, image, content_type, updated_at)
            VALUES (?, ?, ?, ?)
            ON CONFLICT (post_id)
            DO UPDATE SET
                image = excluded.image,
                content_type = excluded.content_type,
                updated_at = excluded.updated_at
            """;

        jdbcTemplate.update(
                sql,
                postImage.getPostId(),
                postImage.getImage(),
                postImage.getContentType(),
                postImage.getUpdatedAt()
        );
    }
}