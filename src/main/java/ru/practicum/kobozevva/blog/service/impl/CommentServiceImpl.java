package ru.practicum.kobozevva.blog.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.kobozevva.blog.dto.comment.CommentDto;
import ru.practicum.kobozevva.blog.dto.comment.NewCommentDto;
import ru.practicum.kobozevva.blog.dto.comment.UpdateCommentDto;
import ru.practicum.kobozevva.blog.mapper.CommentMapper;
import ru.practicum.kobozevva.blog.model.Comment;
import ru.practicum.kobozevva.blog.repository.CommentRepository;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.service.CommentService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto createComment(Long postId, NewCommentDto newCommentDto) {
        checkPostExistsById(postId);

        if (!Objects.equals(postId, newCommentDto.getPostId()))
            throw new IllegalArgumentException("PostId from path doesn't match postId in NewCommentDto");

        Comment comment = commentRepository.save(commentMapper.toEntity(newCommentDto));

        CommentDto commentDto = commentMapper.toDto(comment);
        log.info("Comment for post with id {} is created: {}", postId, commentDto);
        return commentDto;
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Comment comment = checkAndGetCommentByPostIdAndId(postId, commentId);

        CommentDto commentDto = commentMapper.toDto(comment);
        log.info("Comment for post with id {} is requested by id: {}", postId, commentId);
        return commentDto;
    }

    @Override
    public List<CommentDto> findComments(Long postId) {
        checkPostExistsById(postId);

        List<Comment> comments = commentRepository.findAllByPostId(postId);

        List<CommentDto> commentsDto = commentMapper.toDto(comments);
        log.info("Comments for post with id {} are requested: {}", postId, commentsDto);
        return commentsDto;
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long postId, Long commentId, UpdateCommentDto updateCommentDto) {
        Comment comment = checkAndGetCommentByPostIdAndId(postId, commentId);

        comment = commentMapper.update(comment, updateCommentDto);

        CommentDto commentDto = commentMapper.toDto(comment);
        log.info("Comment for post with id {} is updated: {}", postId, commentDto);
        return commentDto;
    }

    @Override
    @Transactional
    public void deleteCommentById(Long postId, Long commentId) {
        checkAndGetCommentByPostIdAndId(postId, commentId);

        commentRepository.deleteById(commentId);
        log.info("Comment for post with id {} is deleted by id: {}", postId,  commentId);
    }

    private void checkPostExistsById(Long postId) {
        if (!postRepository.existsById(postId))
            throw new RuntimeException("Post doesn't exist with id: %s".formatted(postId));
    }

    private Comment checkAndGetCommentByPostIdAndId(Long postId, Long commentId) {
        // TODO: проверка отключена из-за ошибки на frontend. При редактировании комментария неверный postId в path.
//        checkPostExistsById(postId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment doesn't exist with id: %s".formatted(commentId)));

        // TODO: проверка отключена из-за ошибки на frontend. При редактировании комментария неверный postId в path.
//        if (!Objects.equals(postId, comment.getPostId()))
//            throw new IllegalArgumentException("PostId from path doesn't match postId in comment");

        return comment;
    }
}