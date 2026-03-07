package ru.practicum.kobozevva.blog.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.kobozevva.blog.dto.post.NewPostDto;
import ru.practicum.kobozevva.blog.dto.post.PostDto;
import ru.practicum.kobozevva.blog.dto.post.PostsDto;
import ru.practicum.kobozevva.blog.dto.post.UpdatePostDto;
import ru.practicum.kobozevva.blog.service.PostService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@Valid @RequestBody NewPostDto newPostDto) {
        log.info("POST /api/posts with params(newPostDto {})", newPostDto);
        return postService.createPost(newPostDto);
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable("id") Long postId) {
        log.info("GET /api/posts/{id} with params(id {})", postId);
        return postService.getPostById(postId);
    }

    @GetMapping
    public PostsDto findPosts(@RequestParam("search") String search,
                              @PositiveOrZero @RequestParam("pageNumber") Integer pageNumber,
                              @Positive @RequestParam("pageSize") Integer pageSize) {
        log.info("GET /api/posts with params(search {}, pageNumber {}, pageSize {})", search, pageNumber, pageSize);
        return postService.findPosts(search, pageNumber, pageSize);
    }

    @PutMapping("/{id}")
    public PostDto updatePost(@PathVariable("id") Long postId, @Valid @RequestBody UpdatePostDto updatePostDto) {
        log.info("PUT /api/posts/{id} with params(id {}, newPostDto {})", postId, updatePostDto);
        return postService.updatePost(postId, updatePostDto);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable("id") Long postId) {
        log.info("DELETE /api/posts/{id} with params(id {})", postId);
        postService.deletePostById(postId);
    }

    @PostMapping("/{id}/likes")
    public Integer likePost(@PathVariable("id") Long postId) {
        log.info("POST /api/posts/{id}/likes with params(id {})", postId);
        return postService.likePost(postId);
    }

    @PutMapping("/{id}/image")
    public void updatePostImage(@PathVariable("id") Long postId, @RequestParam("image") MultipartFile imageFile) {
        log.info("PUT /api/posts/{id}/image with params(id {})", postId);
        postService.updatePostImage(postId, imageFile);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getPostImage(@PathVariable("id") Long postId) {
        log.info("GET /api/posts/{id}/image with params(id {})", postId);

        byte[] imageData = postService.getPostImage(postId);

        if (imageData == null || imageData.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(imageData.length)
                .body(imageData);
    }
}