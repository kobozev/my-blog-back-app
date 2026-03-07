package ru.practicum.kobozevva.blog.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.kobozevva.blog.dto.post.NewPostDto;
import ru.practicum.kobozevva.blog.dto.post.PostDto;
import ru.practicum.kobozevva.blog.dto.post.PostPreviewDto;
import ru.practicum.kobozevva.blog.dto.post.UpdatePostDto;
import ru.practicum.kobozevva.blog.model.Post;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-07T18:39:08+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDto toDto(Post post, Long commentsCount, List<String> tags) {
        if ( post == null && commentsCount == null && tags == null ) {
            return null;
        }

        PostDto.PostDtoBuilder<?, ?> postDto = PostDto.builder();

        if ( post != null ) {
            postDto.id( post.getId() );
            postDto.title( post.getTitle() );
            postDto.text( post.getText() );
            if ( post.getLikesCount() != null ) {
                postDto.likesCount( post.getLikesCount().longValue() );
            }
        }
        postDto.commentsCount( commentsCount );
        List<String> list = tags;
        if ( list != null ) {
            postDto.tags( new ArrayList<String>( list ) );
        }

        return postDto.build();
    }

    @Override
    public PostPreviewDto toPreviewDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PostPreviewDto.PostPreviewDtoBuilder<?, ?> postPreviewDto = PostPreviewDto.builder();

        postPreviewDto.id( post.getId() );
        postPreviewDto.title( post.getTitle() );
        postPreviewDto.text( post.getText() );
        if ( post.getLikesCount() != null ) {
            postPreviewDto.likesCount( post.getLikesCount().longValue() );
        }

        return postPreviewDto.build();
    }

    @Override
    public List<PostPreviewDto> toPreviewDto(List<Post> posts) {
        if ( posts == null ) {
            return null;
        }

        List<PostPreviewDto> list = new ArrayList<PostPreviewDto>( posts.size() );
        for ( Post post : posts ) {
            list.add( toPreviewDto( post ) );
        }

        return list;
    }

    @Override
    public Post update(Post post, UpdatePostDto updatePostDto) {
        if ( updatePostDto == null ) {
            return post;
        }

        post.setTitle( updatePostDto.getTitle() );
        post.setText( updatePostDto.getText() );

        return post;
    }

    @Override
    public Post toEntity(NewPostDto newPostDto) {
        if ( newPostDto == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        post.title( newPostDto.getTitle() );
        post.text( newPostDto.getText() );

        return post.build();
    }
}
