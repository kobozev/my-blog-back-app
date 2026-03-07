package ru.practicum.kobozevva.blog.repository;

import org.springframework.data.util.Pair;
import ru.practicum.kobozevva.blog.model.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findPosts(ParsedSearch parsedSearch, int offset, int limit);

    Long countPosts(ParsedSearch parsedSearch);

    ParsedSearch parseSearch(String search);

    default Pair<List<Post>, Long> findAndCountPosts(String search, int offset, int limit) {
        ParsedSearch parsedSearch = parseSearch(search);
        return Pair.of(findPosts(parsedSearch, offset, limit), countPosts(parsedSearch));
    }

    record ParsedSearch(List<String> tags, String titleSubstring) {}
}