package ru.practicum.kobozevva.blog.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.kobozevva.blog.model.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<Comment> {

    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(
                rs.getLong("id"),
                rs.getLong("post_id"),
                rs.getString("text"),
                rs.getObject("created_at", java.time.OffsetDateTime.class),
                rs.getObject("updated_at", java.time.OffsetDateTime.class)
        );
    }
}