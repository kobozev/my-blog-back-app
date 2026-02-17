package ru.practicum.kobozevva.blog.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.kobozevva.blog.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}