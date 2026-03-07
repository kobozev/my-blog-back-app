package ru.practicum.kobozevva.blog.configuration.repository;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.kobozevva.blog.repository.CommentRepository;
import ru.practicum.kobozevva.blog.repository.PostImageRepository;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.repository.TagRepository;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class RepositoryMockConfig {
    @Bean
    public PostRepository postRepository()
    {
        return mock(PostRepository.class);
    }

    @Bean
    public PostImageRepository postImageRepository()
    {
        return mock(PostImageRepository.class);
    }

    @Bean
    public CommentRepository commentRepository()
    {
        return mock(CommentRepository.class);
    }

    @Bean
    public TagRepository tagRepository()
    {
        return mock(TagRepository.class);
    }
}