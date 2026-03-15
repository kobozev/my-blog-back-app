package ru.practicum.kobozevva.blog.testdata;

import ru.practicum.kobozevva.blog.dto.comment.CommentDto;
import ru.practicum.kobozevva.blog.dto.comment.NewCommentDto;
import ru.practicum.kobozevva.blog.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentTestData {

    private CommentTestData() {}

    public static CommentBuilder comment() {
        return new CommentBuilder();
    }

    public static CommentDtoBuilder commentDto() {
        return new CommentDtoBuilder();
    }

    public static NewCommentDtoBuilder newCommentDto() {
        return new NewCommentDtoBuilder();
    }

    public static class CommentBuilder {

        private Long id = 1L;
        private Long postId = 1L;
        private String text = "Test comment text";

        public CommentBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CommentBuilder withPostId(Long postId) {
            this.postId = postId;
            return this;
        }

        public CommentBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public Comment build() {
            return Comment.builder()
                    .id(id)
                    .postId(postId)
                    .text(text)
                    .build();
        }
    }

    public static class CommentDtoBuilder {

        private Long id = 1L;
        private Long postId = 1L;
        private String text = "Test comment text";

        public CommentDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CommentDtoBuilder withPostId(Long postId) {
            this.postId = postId;
            return this;
        }

        public CommentDtoBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public CommentDto build() {
            return CommentDto.builder()
                    .id(id)
                    .postId(postId)
                    .text(text)
                    .build();
        }
    }

    public static class NewCommentDtoBuilder {

        private Long postId = 1L;
        private String text = "Test comment text";

        public NewCommentDtoBuilder withPostId(Long postId) {
            this.postId = postId;
            return this;
        }

        public NewCommentDtoBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public NewCommentDto build() {
            return NewCommentDto.builder()
                    .postId(postId)
                    .text(text)
                    .build();
        }
    }

    public static Comment aDefaultComment() {
        return comment().build();
    }

    public static CommentDto aDefaultCommentDto() {
        return commentDto().build();
    }

    public static NewCommentDto aDefaultNewCommentDto() {
        return newCommentDto().build();
    }

    public static List<CommentDto> aListOfComments(int count) {

        List<CommentDto> comments = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            comments.add(
                    commentDto()
                            .withId((long) i)
                            .withText("Comment " + i)
                            .build()
            );
        }

        return comments;
    }
}