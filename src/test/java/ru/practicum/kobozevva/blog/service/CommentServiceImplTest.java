package ru.practicum.kobozevva.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.kobozevva.blog.dto.comment.CommentDto;
import ru.practicum.kobozevva.blog.dto.comment.NewCommentDto;
import ru.practicum.kobozevva.blog.exception.CommentNotFoundException;
import ru.practicum.kobozevva.blog.mapper.CommentMapper;
import ru.practicum.kobozevva.blog.model.Comment;
import ru.practicum.kobozevva.blog.repository.CommentRepository;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.service.impl.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.kobozevva.blog.testdata.CommentTestData.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentServiceImpl Tests")
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment defaultComment;
    private CommentDto defaultCommentDto;
    private NewCommentDto defaultNewCommentDto;

    @BeforeEach
    void setUp() {
        defaultComment = aDefaultComment();
        defaultCommentDto = aDefaultCommentDto();
        defaultNewCommentDto = aDefaultNewCommentDto();
    }

    private void mockCommentExists(Long id) {
        when(commentRepository.findById(id))
                .thenReturn(Optional.of(defaultComment));
    }

    private void mockPostExists(Long postId) {
        when(postRepository.existsById(postId))
                .thenReturn(true);
    }

    @Nested
    class CreateCommentTests {

        @Test
        @DisplayName("Should create comment")
        void shouldCreateComment() {

            Long postId = defaultComment.getPostId();

            mockPostExists(postId);

            when(commentMapper.toEntity(defaultNewCommentDto))
                    .thenReturn(defaultComment);

            when(commentRepository.save(defaultComment))
                    .thenReturn(defaultComment);

            when(commentMapper.toDto(defaultComment))
                    .thenReturn(defaultCommentDto);

            CommentDto result = commentService.createComment(postId, defaultNewCommentDto);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(defaultCommentDto.getId(), result.getId()),
                    () -> assertEquals(defaultCommentDto.getText(), result.getText())
            );

            verify(commentMapper).toEntity(defaultNewCommentDto);
            verify(commentRepository).save(defaultComment);
            verify(commentMapper).toDto(defaultComment);
        }
    }

    @Nested
    class GetCommentsTests {

        @Test
        @DisplayName("Should return comments by post id")
        void shouldReturnCommentsByPostId() {

            Long postId = 1L;

            mockPostExists(postId);

            List<Comment> comments = List.of(defaultComment);

            when(commentRepository.findAllByPostId(postId))
                    .thenReturn(comments);

            when(commentMapper.toDto(comments))
                    .thenReturn(List.of(defaultCommentDto));

            List<CommentDto> result = commentService.findComments(postId);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals(defaultCommentDto.getId(), result.getFirst().getId())
            );

            verify(postRepository).existsById(postId);
            verify(commentRepository).findAllByPostId(postId);
            verify(commentMapper).toDto(comments);
        }
    }

    @Nested
    class DeleteCommentTests {

        @Test
        @DisplayName("Should delete comment")
        void shouldDeleteComment() {

            Long postId = defaultComment.getPostId();
            Long commentId = 1L;

            mockPostExists(postId);
            mockCommentExists(commentId);

            commentService.deleteCommentById(postId, commentId);

            verify(postRepository).existsById(postId);
            verify(commentRepository).findById(commentId);
            verify(commentRepository).deleteById(commentId);
        }

        @Test
        @DisplayName("Should throw exception when comment not found")
        void shouldThrowExceptionWhenCommentNotFound() {

            Long postId = defaultComment.getPostId();
            Long commentId = 999L;

            mockPostExists(postId);

            when(commentRepository.findById(commentId))
                    .thenReturn(Optional.empty());

            assertThrows(
                    CommentNotFoundException.class,
                    () -> commentService.deleteCommentById(postId, commentId)
            );

            verify(postRepository).existsById(postId);
            verify(commentRepository).findById(commentId);
            verify(commentRepository, never()).deleteById(any());
        }
    }
}