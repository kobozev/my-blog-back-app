package ru.practicum.kobozevva.blog.service;

import ru.practicum.kobozevva.blog.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getCommentsByPost(long postId);

    Comment getComment(long postId, long commentId);

    Comment createComment(long postId, String text);

    Comment updateComment(long postId, long commentId, String text);

    void deleteComment(long postId, long commentId);
}