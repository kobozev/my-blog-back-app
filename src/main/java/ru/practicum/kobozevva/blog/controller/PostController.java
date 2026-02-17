package ru.practicum.kobozevva.blog.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.kobozevva.blog.dto.request.PostCreateRequestDto;
import ru.practicum.kobozevva.blog.dto.request.PostUpdateRequestDto;
import ru.practicum.kobozevva.blog.dto.response.PostListResponseDto;
import ru.practicum.kobozevva.blog.dto.response.PostResponseDto;
import ru.practicum.kobozevva.blog.mapper.PostMapper;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.service.PostImageService;
import ru.practicum.kobozevva.blog.service.PostService;
import ru.practicum.kobozevva.blog.service.TagService;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final TagService tagService;
    private final PostImageService postImageService;
    private final PostMapper postMapper;
    private static final Logger log =
            LoggerFactory.getLogger(PostController.class);

    public PostController(PostService postService,
                          TagService tagService,
                          PostImageService postImageService,
                          PostMapper postMapper) {
        this.postService = postService;
        this.tagService = tagService;
        this.postImageService = postImageService;
        this.postMapper = postMapper;
    }

    // GET /api/posts?search=&pageNumber=&pageSize=&
    @GetMapping
    public PostListResponseDto getPosts(
            @RequestParam(required = false) String search,
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    ) {
        log.debug(
                "HTTP GET /api/posts search='{}', page={}, size={}",
                Optional.ofNullable(search).orElse(""), pageNumber, pageSize
        );

        List<Post> posts = postService.searchPosts(search, pageNumber, pageSize);
        int lastPage = postService.getLastPage(search, pageSize);

        PostListResponseDto postList = new PostListResponseDto(
                posts.stream().map(postMapper::toResponse).toList(),
                pageNumber > 1,
                pageNumber < lastPage,
                lastPage
        );

        log.debug(
                "HTTP GET /api/posts search='{}', page={}, size={} -> found posts={}, lastPage={}",
                Optional.ofNullable(search).orElse(""), pageNumber, pageSize, posts.size(), lastPage
        );

        return postList;
    }

    // GET /api/posts/{id}
    @GetMapping("/{postId}")
    public PostResponseDto getPost(@PathVariable long postId) {
        Post post = postService.getPostById(postId);
        post.setTags(tagService.getTagsByPost(postId));
        return postMapper.toResponse(post);
    }

    // POST /api/posts
    @PostMapping
    public PostResponseDto createPost(@RequestBody PostCreateRequestDto request) {
        log.debug("HTTP POST /api/posts title='{}'", request.title());

        Post post = postMapper.fromCreateRequest(request);
        Post saved = postService.createPost(post);

        tagService.replacePostTags(saved.getId(), request.tags());
        saved.setTags(tagService.getTagsByPost(saved.getId()));

        log.debug("Post created id={}", saved.getId());
        return postMapper.toResponse(saved);
    }

    // PUT /api/posts/{id}
    @PutMapping("/{postId}")
    public PostResponseDto updatePost(
            @PathVariable long postId,
            @RequestBody PostUpdateRequestDto request
    ) {
        log.debug("PUT /api/posts/{}", postId);
        Post post = postMapper.fromUpdateRequest(request);
        post.setId(postId);

        Post updated = postService.updatePost(post);

        tagService.replacePostTags(postId, request.tags());
        updated.setTags(tagService.getTagsByPost(postId));

        log.debug("Post updated id={}", updated.getId());
        return postMapper.toResponse(updated);
    }

    // DELETE /api/posts/{id}
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable long postId) {
        log.debug("DELETE /api/posts/{}", postId);
        postService.deletePost(postId);
        log.debug("Post deleted id={}", postId);
    }

    // POST /api/posts/{id}/likes
    @PostMapping("/{postId}/likes")
    public int addLike(@PathVariable long postId) {
        log.debug("POST /api/posts/{}/likes", postId);
        int likesCount = postService.addLike(postId);
        log.debug("Post id={} likesCount={}", postId, likesCount);
        return likesCount;
    }

    // PUT /api/posts/{id}/image
    @PutMapping("/{postId}/image")
    public void uploadImage(
            @PathVariable long postId,
            @RequestParam("image") MultipartFile image
    ) {
        log.debug(
                "HTTP PUT /api/posts/{}/image filename={}, size={}",
                postId, image.getOriginalFilename(), image.getSize()
        );
        postImageService.updatePostImage(postId, image);
        log.debug("Post id={} -> image added filename={}", postId, image.getOriginalFilename());
    }

    // GET /api/posts/{id}/image
    @GetMapping("/{postId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable long postId) {
        byte[] image = postImageService.getPostImage(postId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(postImageService.getContentType(postId)))
                .body(image);
    }
}