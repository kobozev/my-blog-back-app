package ru.practicum.kobozevva.blog.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.repository.PostRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostServiceImpl Tests")
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private Post testPost;

    @BeforeEach
    void setup() {
        testPost = new Post(1L, "Test Title", "Test Content", 1, 1, OffsetDateTime.now(), OffsetDateTime.now());
    }

    @Test
    @DisplayName("Should return posts response with correct pagination when getting test post")
    void searchExistingPost() {
        List<Post> posts = List.of(testPost);

        when(postRepository.search(anyString(), anyInt(), anyInt()))
                .thenReturn(posts);

        List<Post> result = postService.searchPosts("test", 1, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPost, result.getFirst());
        verify(postRepository, times(1)).search("test", 1, 5);
    }

    @Test
    @DisplayName("Should return posts response with empty list when no posts found")
    void searchNonExistentPost() {
        when(postRepository.search(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of());

        List<Post> result = postService.searchPosts("non-existent", 1, 5);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(postRepository, times(1)).search("non-existent", 1, 5);
    }

    @Test
    @DisplayName("Should return posts response with null search parameter")
    void searchWithNullText() {
        List<Post> posts = List.of(testPost);

        when(postRepository.search(isNull(), anyInt(), anyInt()))
                .thenReturn(posts);

        List<Post> result = postService.searchPosts(null, 1, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(postRepository, times(1)).search(null, 1, 5);
    }

    @Test
    @DisplayName("Should return post when found by id")
    void getExistingPostById() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        Post result = postService.getPostById(1L);

        assertNotNull(result);
        assertEquals(testPost, result);
        assertEquals(1L, result.getId());
        assertEquals("Test Title", result.getTitle());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty optional when post not found by id")
    void getNonExistentPostById() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> postService.getPostById(999L));
        verify(postRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should create post with valid data")
    void createPost() {
        when(postRepository.save(any(Post.class)))
                .thenReturn(testPost);

        Post result = postService.createPost(testPost);

        assertNotNull(result);
        assertEquals(testPost, result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getText());
        verify(postRepository, times(1)).save(testPost);
    }

    @Test
    @DisplayName("Should update post when post exists")
    void updatePost() {
        OffsetDateTime updatedAt = OffsetDateTime.now();
        Post updatedPost = new Post(1L, "Updated Title", "Updated Content", 2, 2, testPost.getCreatedAt(), updatedAt);

        when(postRepository.existsById(1L)).thenReturn(true);
        when(postRepository.findById(1L)).thenReturn(Optional.of(updatedPost));
        doNothing().when(postRepository).update(any(Post.class));

        Post result = postService.updatePost(updatedPost);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Content", result.getText());

        verify(postRepository, times(1)).existsById(1L);
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).update(updatedPost);
    }

    @Test
    @DisplayName("Should delete post when post exists")
    void deletePost() {
        when(postRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postRepository).deleteById(anyLong());

        postService.deletePost(1L);

        verify(postRepository, times(1)).existsById(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should add like and return updated post")
    void addLike() {
        when(postRepository.existsById(1L)).thenReturn(true);
        when(postRepository.incrementLikes(1L)).thenReturn(2);

        int result = postService.addLike(1L);

        assertEquals(2, result);
        verify(postRepository, times(1)).incrementLikes(1L);
    }
}