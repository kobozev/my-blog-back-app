package ru.practicum.kobozevva.blog.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long postId) {
        super("Post not found with id " + postId);
    }

    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
