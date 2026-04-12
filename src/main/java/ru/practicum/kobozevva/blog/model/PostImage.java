package ru.practicum.kobozevva.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("post_images")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PostImage {
    @Id
    private Long id;

    private Long postId;

    private byte[] imageData;
}