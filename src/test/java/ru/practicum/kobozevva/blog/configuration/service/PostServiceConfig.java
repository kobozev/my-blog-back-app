package ru.practicum.kobozevva.blog.configuration.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.kobozevva.blog.mapper.PostMapper;
import ru.practicum.kobozevva.blog.service.PostService;

@TestConfiguration
@ComponentScan(basePackageClasses = {PostService.class, PostMapper.class})
public class PostServiceConfig {
}
