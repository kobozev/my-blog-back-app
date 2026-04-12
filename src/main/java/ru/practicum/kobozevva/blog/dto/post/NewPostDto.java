package ru.practicum.kobozevva.blog.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDto {
    @NotBlank
    @Size(min = 1, max = 255)
    private String title;
    @NotBlank
    @Size(min = 1, max = 10000)
    private String text;
    @NotNull
    @Builder.Default
    private List<@NotBlank @Size(min = 1, max = 255) String> tags =  new ArrayList<>();
}