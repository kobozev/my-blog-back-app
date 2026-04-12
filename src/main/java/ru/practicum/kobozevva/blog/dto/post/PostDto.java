package ru.practicum.kobozevva.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PostDto {
    private Long id;
    private String title;
    protected String text;
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    @Builder.Default
    private Long likesCount = 0L;
    @Builder.Default
    private Long commentsCount = 0L;
}