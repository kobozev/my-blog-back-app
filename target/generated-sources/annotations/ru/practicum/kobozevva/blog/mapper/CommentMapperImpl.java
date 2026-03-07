package ru.practicum.kobozevva.blog.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.kobozevva.blog.dto.comment.CommentDto;
import ru.practicum.kobozevva.blog.dto.comment.NewCommentDto;
import ru.practicum.kobozevva.blog.dto.comment.UpdateCommentDto;
import ru.practicum.kobozevva.blog.model.Comment;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-07T18:39:08+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDto toDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto.CommentDtoBuilder<?, ?> commentDto = CommentDto.builder();

        commentDto.id( comment.getId() );
        commentDto.text( comment.getText() );
        commentDto.postId( comment.getPostId() );

        return commentDto.build();
    }

    @Override
    public List<CommentDto> toDto(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentDto> list = new ArrayList<CommentDto>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( toDto( comment ) );
        }

        return list;
    }

    @Override
    public Comment update(Comment comment, UpdateCommentDto updateCommentDto) {
        if ( updateCommentDto == null ) {
            return comment;
        }

        comment.setPostId( updateCommentDto.getPostId() );
        comment.setText( updateCommentDto.getText() );

        return comment;
    }

    @Override
    public Comment toEntity(NewCommentDto newCommentDto) {
        if ( newCommentDto == null ) {
            return null;
        }

        Comment.CommentBuilder comment = Comment.builder();

        comment.postId( newCommentDto.getPostId() );
        comment.text( newCommentDto.getText() );

        return comment.build();
    }
}
