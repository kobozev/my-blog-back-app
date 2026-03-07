package ru.practicum.kobozevva.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostsDto {
    private List<PostPreviewDto> posts;
    private Boolean hasPrev;
    private Boolean hasNext;
    private Integer lastPage;
}