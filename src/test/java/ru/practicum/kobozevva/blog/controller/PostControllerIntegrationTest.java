package ru.practicum.kobozevva.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.kobozevva.blog.configuration.TestWebApplicationConfiguration;
import ru.practicum.kobozevva.blog.dto.request.PostCreateRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.kobozevva.blog.dto.request.PostUpdateRequestDto;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestWebApplicationConfiguration.class)
@ActiveProfiles("test")
class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Should create post")
    void shouldCreatePost() throws Exception {

        PostCreateRequestDto request =
                new PostCreateRequestDto(
                        "Integration Title",
                        "Integration Content",
                        null
                );

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Title"))
                .andExpect(jsonPath("$.text").value("Integration Content"));
    }

    @Test
    @DisplayName("Should return post by id")
    void shouldReturnPostById() throws Exception {

        PostCreateRequestDto request =
                new PostCreateRequestDto("Title", "Content", null);

        String response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/posts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.text").value("Content"));
    }

    @Test
    @DisplayName("Should return empty list when no posts")
    void shouldReturnNoPosts() throws Exception {

        mockMvc.perform(get("/api/posts")
                        .param("pageNumber", "1")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").isArray());
    }

    @Test
    @DisplayName("Should update post")
    void shouldUpdatePost() throws Exception {

        PostCreateRequestDto requestCreate =
                new PostCreateRequestDto("Updated Title", "Updated Content", null);

        String responseCreate = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCreate)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long postId = objectMapper.readTree(responseCreate).get("id").asLong();

        PostUpdateRequestDto requestUpdate =
                new PostUpdateRequestDto(postId, "Updated Title", "Updated Content", null);

        mockMvc.perform(put("/api/posts")
                        .param("postId", String.valueOf(postId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUpdate)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(put("/api/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.text").value("Updated Content"));
    }

    @Test
    @DisplayName("Should delete post")
    void shouldDeletePost() throws Exception {

        PostCreateRequestDto request =
                new PostCreateRequestDto("Deleted Title", "Deleted Content", null);

        String response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/posts/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/posts/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when post not found")
    void shouldReturn400WhenNotFound() throws Exception {

        mockMvc.perform(get("/api/posts/99999"))
                .andExpect(status().isBadRequest());
    }
}