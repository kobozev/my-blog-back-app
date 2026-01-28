package ru.practicum.kobozevva.blog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.kobozevva.blog.model.Comment;
import ru.practicum.kobozevva.blog.repository.CommentRepository;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.service.CommentService;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Comment> getCommentsByPost(long postId) {
        ensurePostExists(postId);
        return commentRepository.findByPostId(postId);
    }

    @Override
    public Comment getComment(long postId, long commentId) {
        ensurePostExists(postId);

        return commentRepository.findById(postId, commentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Comment not found. postId=" + postId + ", commentId=" + commentId
                        ));
    }

    @Transactional
    @Override
    public Comment createComment(long postId, String text) {
        ensurePostExists(postId);

        Comment comment = new Comment(
                null,
                postId,
                text,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment updateComment(long postId, long commentId, String text) {
        ensurePostExists(postId);

        Comment existing = commentRepository.findById(postId, commentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Comment not found. postId=" + postId + ", commentId=" + commentId
                        ));

        existing.setText(text);
        existing.setUpdatedAt(OffsetDateTime.now());

        return commentRepository.update(existing);
    }

    @Transactional
    @Override
    public void deleteComment(long postId, long commentId) {
        ensurePostExists(postId);

        Comment existing = commentRepository.findById(postId, commentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Comment not found. postId=" + postId + ", commentId=" + commentId
                        ));

        commentRepository.delete(existing.getPostId(), existing.getId());
    }


    private void ensurePostExists(long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }
    }
}