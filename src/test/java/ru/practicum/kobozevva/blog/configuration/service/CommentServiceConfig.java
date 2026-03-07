package ru.practicum.kobozevva.blog.configuration.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.kobozevva.blog.mapper.CommentMapper;
import ru.practicum.kobozevva.blog.service.CommentService;

@TestConfiguration
@ComponentScan(basePackageClasses = {CommentService.class, CommentMapper.class})
public class CommentServiceConfig {
}
