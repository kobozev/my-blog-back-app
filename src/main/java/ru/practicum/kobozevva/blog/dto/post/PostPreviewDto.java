package ru.practicum.kobozevva.blog.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class PostPreviewDto extends PostDto {
    @JsonProperty("text")
    public String getPreview() {
        if (text == null) return "";
        int maxLength = 128;
        return text.length() <= maxLength ? text : text.substring(0, maxLength) + "…";
    }
}