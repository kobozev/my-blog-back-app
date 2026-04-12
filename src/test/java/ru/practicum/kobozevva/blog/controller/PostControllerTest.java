package ru.practicum.kobozevva.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerTest extends BaseIntegrationTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        jdbc.update("DELETE FROM posts", Collections.emptyMap());
        jdbc.update("DELETE FROM tags", Collections.emptyMap());
    }

    @Test
    void shouldCreateAndSavePost() throws Exception {
        String title = "Название поста";
        String text = "Текст поста";
        List<String> tags = List.of("tag1", "tag2");

        String json = """
                    {
                    "title": "%s",
                    "text": "%s",
                    "tags": %s
                    }
                """.formatted(title,  text, objectMapper.writeValueAsString(tags));

        mockMvc.perform(
                        post("/api/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.tags", hasSize(2)))
                .andExpect(jsonPath("$.tags", containsInAnyOrder(tags.toArray())))
                .andExpect(jsonPath("$.likesCount").value(0))
                .andExpect(jsonPath("$.commentsCount").value(0));
    }
}
