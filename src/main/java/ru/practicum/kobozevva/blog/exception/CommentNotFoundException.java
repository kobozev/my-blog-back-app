package ru.practicum.kobozevva.blog.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(Long commentId) {
        super("Comment not found with id " + commentId);
    }

    public CommentNotFoundException(Long postId, Long commentId) {
        super("Comment with id " + commentId + " not found for post " + postId);
    }

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}