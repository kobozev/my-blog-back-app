package ru.practicum.kobozevva.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("posts")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Post {
    @Id
    private Long id;

    private String title;

    private String text;

    @Builder.Default
    private Integer likesCount = 0;
}