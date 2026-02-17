package ru.practicum.kobozevva.blog.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.kobozevva.blog.model.PostImage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostImageRowMapper implements RowMapper<PostImage> {

    @Override
    public PostImage mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PostImage(
                rs.getLong("post_id"),
                rs.getBytes("image"),
                rs.getString("content_type"),
                rs.getObject("updated_at", java.time.OffsetDateTime.class)
        );
    }
}