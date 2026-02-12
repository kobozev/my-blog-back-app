package ru.practicum.kobozevva.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.kobozevva.blog.configuration.TestWebApplicationConfiguration;
import ru.practicum.kobozevva.blog.model.Comment;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.repository.PostRepository;

import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestWebApplicationConfiguration.class)
@ActiveProfiles("test")
class CommentControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PostRepository postRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Post createTestPost() {
        Post post = new Post(
                null,
                "Test Post",
                "Test Content",
                0,
                0,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        return postRepository.save(post);
    }

    @Test
    @DisplayName("Should create comment")
    void shouldCreateComment() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Post post = createTestPost();

        mockMvc.perform(post("/api/posts/{postId}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "text": "Test comment"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Test comment"))
                .andExpect(jsonPath("$.postId").value(post.getId()));
    }

    @Test
    @DisplayName("Should return comments list")
    void shouldReturnComments() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Post post = createTestPost();

        mockMvc.perform(post("/api/posts/{postId}/comments", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": "First comment"
                        }
                        """));

        mockMvc.perform(get("/api/posts/{postId}/comments", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("First comment"));
    }

    @Test
    @DisplayName("Should update comment")
    void shouldUpdateComment() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Post post = createTestPost();

        String response = mockMvc.perform(post("/api/posts/{postId}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "text": "Old comment"
                                }
                                """))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Comment created = objectMapper.readValue(response, Comment.class);

        mockMvc.perform(put("/api/posts/{postId}/comments/{commentId}",
                        post.getId(), created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "text": "Updated comment"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated comment"));
    }

    @Test
    @DisplayName("Should delete comment")
    void shouldDeleteComment() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Post post = createTestPost();

        String response = mockMvc.perform(post("/api/posts/{postId}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "text": "To be deleted"
                                }
                                """))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Comment created = objectMapper.readValue(response, Comment.class);

        mockMvc.perform(delete("/api/posts/{postId}/comments/{commentId}",
                        post.getId(), created.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/posts/{postId}/comments", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should return 400 when post not found")
    void shouldReturnBadRequestWhenPostNotFound() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(get("/api/posts/{postId}/comments", 9999))
                .andExpect(status().isBadRequest());
    }
}