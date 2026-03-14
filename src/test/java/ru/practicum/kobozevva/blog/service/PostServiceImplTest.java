package ru.practicum.kobozevva.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.kobozevva.blog.dto.post.*;
import ru.practicum.kobozevva.blog.exception.PostNotFoundException;
import ru.practicum.kobozevva.blog.mapper.PostMapper;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.repository.CommentRepository;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.repository.TagRepository;
import ru.practicum.kobozevva.blog.service.impl.PostServiceImpl;
import ru.practicum.kobozevva.blog.testdata.PostTestData;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.kobozevva.blog.testdata.PostTestData.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostServiceImpl Tests")
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;

    private Post defaultPost;
    private PostDto defaultPostDto;
    private NewPostDto defaultNewPostDto;
    private UpdatePostDto defaultUpdatePostDto;

    @BeforeEach
    void setUp() {
        defaultPost = PostTestData.aDefaultPost();
        defaultPostDto = PostTestData.aDefaultPostDto();
        defaultNewPostDto = PostTestData.aDefaultNewPostDto();
        defaultUpdatePostDto = PostTestData.aDefaultUpdatePostDto();
    }

    private void mockPostExists(Long id) {
        when(postRepository.findById(id)).thenReturn(Optional.of(defaultPost));
    }

    private void mockPostTags(Long postId, List<String> tags) {
        when(tagRepository.findByPostId(postId)).thenReturn(tags);
    }

    private void mockPostComments(Long postId, long count) {
        when(commentRepository.countByPostId(postId)).thenReturn(count);
    }

    @Nested
    class CreatePostTests {

        @Test
        @DisplayName("Should create post successfully")
        void shouldCreatePost() {

            when(postMapper.toEntity(defaultNewPostDto))
                    .thenReturn(defaultPost);

            when(postRepository.save(defaultPost))
                    .thenReturn(defaultPost);

            when(postMapper.toDto(defaultPost, 0L, defaultNewPostDto.getTags()))
                    .thenReturn(defaultPostDto);

            PostDto result = postService.createPost(defaultNewPostDto);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(defaultPostDto.getId(), result.getId()),
                    () -> assertEquals(defaultPostDto.getTitle(), result.getTitle()),
                    () -> assertEquals(defaultPostDto.getText(), result.getText()),
                    () -> assertEquals(defaultPostDto.getTags(), result.getTags())
            );

            verify(postMapper).toEntity(defaultNewPostDto);
            verify(postRepository).save(defaultPost);
            verify(postMapper).toDto(defaultPost, 0L, defaultNewPostDto.getTags());
        }
    }

    @Nested
    class GetPostTests {

        @Test
        @DisplayName("Should return post by id when exists")
        void shouldReturnPostById() {

            Long postId = 1L;

            mockPostExists(postId);
            mockPostTags(postId, defaultPostDto.getTags());

            when(postMapper.toDto(defaultPost, 0L, defaultPostDto.getTags()))
                    .thenReturn(defaultPostDto);

            PostDto result = postService.getPostById(postId);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(postId, result.getId()),
                    () -> assertEquals(defaultPostDto.getTitle(), result.getTitle())
            );

            verify(postRepository).findById(postId);
            verify(tagRepository).findByPostId(postId);
            verify(postMapper).toDto(defaultPost, 0L, defaultPostDto.getTags());
        }

        @Test
        @DisplayName("Should throw PostNotFoundException when post not found")
        void shouldThrowExceptionWhenPostNotFound() {

            Long postId = 999L;

            when(postRepository.findById(postId))
                    .thenReturn(Optional.empty());

            assertThrows(
                    PostNotFoundException.class,
                    () -> postService.getPostById(postId)
            );

            verify(postRepository).findById(postId);
            verifyNoInteractions(postMapper, tagRepository, commentRepository);
        }
    }

    @Nested
    class UpdatePostTests {

        @Test
        @DisplayName("Should update post successfully")
        void shouldUpdatePost() {

            Long postId = 1L;

            mockPostExists(postId);
            mockPostComments(postId, 0L);

            when(postMapper.update(defaultPost, defaultUpdatePostDto))
                    .thenReturn(defaultPost);

            when(postRepository.save(defaultPost))
                    .thenReturn(defaultPost);

            when(postMapper.toDto(defaultPost, 0L, defaultUpdatePostDto.getTags()))
                    .thenReturn(defaultPostDto);

            PostDto result = postService.updatePost(postId, defaultUpdatePostDto);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(postId, result.getId())
            );

            verify(postMapper).update(defaultPost, defaultUpdatePostDto);
            verify(postRepository).save(defaultPost);
            verify(tagRepository).upsertTagsAndAssignToPost(postId, defaultUpdatePostDto.getTags());
            verify(commentRepository).countByPostId(postId);
            verify(postMapper).toDto(defaultPost, 0L, defaultUpdatePostDto.getTags());
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent post")
        void shouldThrowExceptionWhenUpdatingNonExistentPost() {

            Long postId = 999L;

            UpdatePostDto  updatePostDto = PostTestData.anUpdatePostDtoWithId(postId);

            when(postRepository.findById(postId))
                    .thenReturn(Optional.empty());

            assertThrows(
                    PostNotFoundException.class,
                    () -> postService.updatePost(postId, updatePostDto)
            );

            verify(postRepository).findById(postId);
            verifyNoInteractions(postMapper, tagRepository, commentRepository);
        }

        @Test
        @DisplayName("Should update post title only")
        void shouldUpdatePostTitleOnly() {

            Long postId = 1L;

            UpdatePostDto updateWithNewTitle = updatePostDto()
                    .withId(postId)
                    .withTitle("New Title Only")
                    .withText(defaultUpdatePostDto.getText())
                    .withTags(defaultUpdatePostDto.getTags())
                    .build();

            mockPostExists(postId);
            mockPostComments(postId, 0L);

            when(postMapper.update(defaultPost, updateWithNewTitle))
                    .thenReturn(defaultPost);

            when(postRepository.save(defaultPost))
                    .thenReturn(defaultPost);

            when(postMapper.toDto(defaultPost, 0L, updateWithNewTitle.getTags()))
                    .thenReturn(defaultPostDto);

            PostDto result = postService.updatePost(postId, updateWithNewTitle);

            assertNotNull(result);

            verify(postMapper).update(defaultPost, updateWithNewTitle);
            verify(postRepository).save(defaultPost);
        }
    }

    @Nested
    class DeletePostTests {

        @Test
        @DisplayName("Should delete post when exists")
        void shouldDeletePost() {

            Long postId = 1L;

            mockPostExists(postId);

            postService.deletePostById(postId);

            verify(postRepository).findById(postId);
            verify(postRepository).deleteById(postId);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent post")
        void shouldThrowExceptionWhenDeletingNonExistentPost() {

            Long postId = 999L;

            when(postRepository.findById(postId)).thenReturn(Optional.empty());

            assertThrows(
                    PostNotFoundException.class,
                    () -> postService.deletePostById(postId)
            );

            verify(postRepository).findById(postId);
            verify(postRepository, never()).deleteById(any());
        }
    }
}