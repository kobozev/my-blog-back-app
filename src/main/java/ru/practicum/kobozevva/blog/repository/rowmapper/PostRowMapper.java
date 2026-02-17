package ru.practicum.kobozevva.blog.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.kobozevva.blog.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRowMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Post(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("text"),
                rs.getInt("likes_count"),
                rs.getInt("comments_count"),
                rs.getObject("created_at", java.time.OffsetDateTime.class),
                rs.getObject("updated_at", java.time.OffsetDateTime.class)
        );
    }
}