package ru.practicum.kobozevva.blog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.kobozevva.blog.model.Post;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.service.PostService;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> searchPosts(String search, int pageNumber, int pageSize) {
        validatePageParams(pageNumber, pageSize);
        return postRepository.search(search, pageNumber, pageSize);
    }

    @Override
    public int getLastPage(String search, int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be positive");
        }

        int total = postRepository.countBySearch(search);
        return (int) Math.ceil((double) total / pageSize);
    }

    @Override
    public Post getPostById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Post not found: " + postId));
    }

    @Transactional
    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    @Override
    public Post updatePost(Post post) {
        Long postId = post.getId();
        if (postId == null || !postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        postRepository.update(post);
        return postRepository.findById(postId).orElseThrow();
    }

    @Transactional
    @Override
    public void deletePost(long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }
        postRepository.deleteById(postId);
    }

    @Transactional
    @Override
    public int addLike(long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }
        return postRepository.incrementLikes(postId);
    }

    private void validatePageParams(int pageNumber, int pageSize) {
        if (pageNumber <= 0) {
            throw new IllegalArgumentException("pageNumber must be >= 1");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be > 0");
        }
    }
}