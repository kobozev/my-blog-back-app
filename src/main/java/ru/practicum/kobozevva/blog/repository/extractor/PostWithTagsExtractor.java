package ru.practicum.kobozevva.blog.repository.extractor;

import org.springframework.jdbc.core.ResultSetExtractor;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class PostWithTagsExtractor implements ResultSetExtractor<Post> {

    @Override
    public Post extractData(ResultSet rs) throws SQLException {
        Post post = null;
        Set<Tag> tags = new LinkedHashSet<>();

        while (rs.next()) {
            if (post == null) {
                post = new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getInt("likes_count"),
                        rs.getInt("comments_count"),
                        rs.getObject("created_at", java.time.OffsetDateTime.class),
                        rs.getObject("updated_at", java.time.OffsetDateTime.class)
                );
            }

            Long tagId = rs.getObject("tag_id", Long.class);
            if (tagId != null) {
                tags.add(new Tag(tagId, rs.getString("tag_name")));
            }
        }

        if (post != null) {
            post.setTags(new ArrayList<>(tags));
        }

        return post;
    }
}