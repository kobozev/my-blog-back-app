package ru.practicum.kobozevva.blog.configuration;

import org.springframework.context.annotation.*;

@Configuration
@Import({TestDataSourceConfiguration.class, RestConfiguration.class, MultipartConfiguration.class, WebConfiguration.class})
@ComponentScan(
        basePackages = "ru.practicum.kobozevva.blog",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = DataSourceConfiguration.class
        )
)
public class TestWebApplicationConfiguration {
}