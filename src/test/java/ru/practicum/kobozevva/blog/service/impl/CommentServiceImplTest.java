package ru.practicum.kobozevva.blog.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.kobozevva.blog.model.Comment;
import ru.practicum.kobozevva.blog.repository.CommentRepository;
import ru.practicum.kobozevva.blog.repository.PostRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;

    @BeforeEach
    void setUp() {
        testComment = new Comment(
                1L,
                10L,
                "Test comment",
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    @Test
    @DisplayName("Should return comments when post exists")
    void shouldReturnComments() {
        when(postRepository.existsById(10L)).thenReturn(true);
        when(commentRepository.findByPostId(10L))
                .thenReturn(List.of(testComment));

        List<Comment> result = commentService.getCommentsByPost(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(commentRepository).findByPostId(10L);
    }

    @Test
    @DisplayName("Should throw exception when post does not exist")
    void shouldThrowWhenPostNotExists() {
        when(postRepository.existsById(10L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> commentService.getCommentsByPost(10L));

        verify(commentRepository, never()).findByPostId(anyLong());
    }

    @Test
    @DisplayName("Should return comment when exists")
    void shouldReturnSingleComment() {
        when(postRepository.existsById(10L)).thenReturn(true);
        when(commentRepository.findById(10L, 1L))
                .thenReturn(Optional.of(testComment));

        Comment result = commentService.getComment(10L, 1L);

        assertNotNull(result);
        assertEquals("Test comment", result.getText());
    }

    @Test
    @DisplayName("Should throw when comment not found")
    void shouldThrowWhenCommentNotFound() {
        when(postRepository.existsById(10L)).thenReturn(true);
        when(commentRepository.findById(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> commentService.getComment(10L, 1L));
    }

    @Test
    @DisplayName("Should create comment")
    void shouldCreateComment() {
        when(postRepository.existsById(10L)).thenReturn(true);
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(testComment);

        Comment result = commentService.createComment(10L, "Test comment");

        assertNotNull(result);
        assertEquals("Test comment", result.getText());

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("Should update comment")
    void shouldUpdateComment() {
        when(postRepository.existsById(10L)).thenReturn(true);
        when(commentRepository.findById(10L, 1L))
                .thenReturn(Optional.of(testComment));
        when(commentRepository.update(any(Comment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Comment result = commentService.updateComment(10L, 1L, "Updated");

        assertEquals("Updated", result.getText());
        verify(commentRepository).update(any(Comment.class));
    }

    @Test
    @DisplayName("Should throw when updating non-existing comment")
    void shouldThrowWhenUpdatingMissingComment() {
        when(postRepository.existsById(10L)).thenReturn(true);
        when(commentRepository.findById(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> commentService.updateComment(10L, 1L, "Updated"));

        verify(commentRepository, never()).update(any());
    }

    @Test
    @DisplayName("Should delete comment")
    void shouldDeleteComment() {
        when(postRepository.existsById(10L)).thenReturn(true);
        when(commentRepository.findById(10L, 1L))
                .thenReturn(Optional.of(testComment));

        commentService.deleteComment(10L, 1L);

        verify(commentRepository).delete(10L, 1L);
    }

    @Test
    @DisplayName("Should throw when deleting non-existing comment")
    void shouldThrowWhenDeletingMissingComment() {
        when(postRepository.existsById(10L)).thenReturn(true);
        when(commentRepository.findById(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> commentService.deleteComment(10L, 1L));

        verify(commentRepository, never()).delete(anyLong(), anyLong());
    }
}