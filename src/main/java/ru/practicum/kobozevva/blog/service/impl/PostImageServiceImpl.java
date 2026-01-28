package ru.practicum.kobozevva.blog.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.service.PostImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class PostImageServiceImpl implements PostImageService {

    private final PostRepository postRepository;
    private final Path uploadRoot;

    public PostImageServiceImpl(PostRepository postRepository,
                                @Value("${upload.root}") String uploadRoot) {
        this.postRepository = postRepository;
        this.uploadRoot = Path.of(uploadRoot);
    }

    @Transactional
    @Override
    public void updatePostImage(long postId, MultipartFile image) {
        ensurePostExists(postId);

        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        try {
            Files.createDirectories(uploadRoot);

            Path imagePath = resolveImagePath(postId);

            Files.write(
                    imagePath,
                    image.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            // content-type сохраняем рядом
            Files.writeString(
                    resolveContentTypePath(postId),
                    image.getContentType(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new IllegalStateException("Failed to store image for postId=" + postId, e);
        }
    }

    @Override
    public byte[] getPostImage(long postId) {
        ensurePostExists(postId);

        Path imagePath = resolveImagePath(postId);

        if (!Files.exists(imagePath)) {
            throw new IllegalArgumentException("Image not found for postId=" + postId);
        }

        try {
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read image for postId=" + postId, e);
        }
    }

    @Override
    public String getContentType(long postId) {
        Path contentTypePath = resolveContentTypePath(postId);

        if (!Files.exists(contentTypePath)) {
            return "application/octet-stream";
        }

        try {
            return Files.readString(contentTypePath);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read content type for postId=" + postId, e);
        }
    }

    private Path resolveImagePath(long postId) {
        return uploadRoot.resolve(postId + ".img");
    }

    private Path resolveContentTypePath(long postId) {
        return uploadRoot.resolve(postId + ".ct");
    }

    private void ensurePostExists(long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }
    }
}