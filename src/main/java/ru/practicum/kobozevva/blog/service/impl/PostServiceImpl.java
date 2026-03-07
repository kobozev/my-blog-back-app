package ru.practicum.kobozevva.blog.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.kobozevva.blog.dto.post.NewPostDto;
import ru.practicum.kobozevva.blog.dto.post.PostDto;
import ru.practicum.kobozevva.blog.dto.post.PostPreviewDto;
import ru.practicum.kobozevva.blog.dto.post.PostsDto;
import ru.practicum.kobozevva.blog.dto.post.UpdatePostDto;
import ru.practicum.kobozevva.blog.mapper.PostMapper;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.model.PostImage;
import ru.practicum.kobozevva.blog.repository.CommentRepository;
import ru.practicum.kobozevva.blog.repository.PostImageRepository;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.repository.TagRepository;
import ru.practicum.kobozevva.blog.service.PostService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;

    private final PostMapper postMapper;

    @Override
    @Transactional
    public PostDto createPost(NewPostDto newPostDto) {
        Post post = postRepository.save(postMapper.toEntity(newPostDto));
        tagRepository.upsertTagsAndAssignToPost(post.getId(), newPostDto.getTags());

        PostDto postDto = postMapper.toDto(post, 0L,  newPostDto.getTags());
        log.info("Post is created: {}", postDto);
        return postDto;
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = checkAndGetPostById(postId);
        Long commentsCount = commentRepository.countByPostId(postId);

        PostDto postDto = postMapper.toDto(post, commentsCount, tagRepository.findByPostId(postId));
        log.info("Post is requested by id: {}", postDto);
        return postDto;
    }

    @Override
    public PostsDto findPosts(String search, Integer pageNumber, Integer pageSize) {
        int offset = (pageNumber - 1) * pageSize;

        Pair<List<Post>, Long> postsAndCount = postRepository.findAndCountPosts(search, offset, pageSize);

        List<Post> posts = postsAndCount.getFirst();
        long totalElements = postsAndCount.getSecond();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        if (totalPages == 0) totalPages = 1;   // если нет результатов

        boolean hasPrev = pageNumber > 1;
        boolean hasNext = pageNumber < totalPages;
        int lastPage = totalPages;

        List<PostPreviewDto> postsDto = postMapper.toPreviewDto(posts);

        enrichWithAdditionalFields(postsDto);

        return PostsDto.builder()
                .posts(postsDto)
                .hasPrev(hasPrev)
                .hasNext(hasNext)
                .lastPage(lastPage)
                .build();

    }

    @Override
    @Transactional
    public PostDto updatePost(Long postId, UpdatePostDto updatePostDto) {
        if (!Objects.equals(postId, updatePostDto.getId()))
            throw new IllegalArgumentException("PostId from path doesn't match postId in updatePostDto");

        Post post = checkAndGetPostById(postId);
        post = postRepository.save(postMapper.update(post, updatePostDto));
        tagRepository.upsertTagsAndAssignToPost(postId, updatePostDto.getTags());

        Long commentsCount = commentRepository.countByPostId(postId);

        PostDto postDto = postMapper.toDto(post, commentsCount, updatePostDto.getTags());
        log.info("Post is updated: {}", postDto);
        return postDto;
    }

    @Override
    @Transactional
    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
        log.info("Post is deleted by id: {}", postId);
    }

    @Override
    @Transactional
    public Integer likePost(Long postId) {
        Post post = checkAndGetPostById(postId);

        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);

        log.info("Post with id {} is liked, current number of likes is {}", post.getId(), post.getLikesCount());
        return post.getLikesCount();
    }

    @Override
    @SneakyThrows
    @Transactional
    public void updatePostImage(Long postId, MultipartFile imageFile) {
        checkAndGetPostById(postId);

        PostImage postImage = postImageRepository.findByPostId(postId)
                .orElse(PostImage.builder()
                        .postId(postId)
                        .build());
        postImage.setImageData(imageFile.getBytes());

        postImage = postImageRepository.save(postImage);
        log.info("Image with id {} is saved for post with id {}", postImage.getId(), postImage.getPostId());
    }

    @Override
    public byte[] getPostImage(Long postId) {
        checkAndGetPostById(postId);

        Optional<PostImage> postImage = postImageRepository.findByPostId(postId);

        postImage.ifPresentOrElse(image -> log.info("Image is found for post with id {}", image.getPostId()),
                () -> log.info("Image is not found for post with id {}", postId));
        return postImage
                .map(PostImage::getImageData)
                .orElse(null);
    }

    private void enrichWithAdditionalFields(List<? extends PostDto> postsDto) {
        if (postsDto.isEmpty()) return;

        List<Long> postIds = postsDto.stream().map(PostDto::getId).toList();

        Map<Long, Long> commentsCountByPostId = commentRepository.countGroupedByPostId(postIds);
        Map<Long, List<String>> tagsByPostId = tagRepository.findGroupedByPostId(postIds);

        postsDto
                .forEach(post -> {
                    post.setCommentsCount(commentsCountByPostId.getOrDefault(post.getId(), 0L));
                    post.setTags(tagsByPostId.getOrDefault(post.getId(), post.getTags()));
                });
    }

    private Post checkAndGetPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post doesn't exist with id: %s".formatted(postId)));
    }
}