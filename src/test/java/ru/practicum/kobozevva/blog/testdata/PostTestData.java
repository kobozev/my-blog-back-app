package ru.practicum.kobozevva.blog.testdata;

import ru.practicum.kobozevva.blog.dto.post.*;
import ru.practicum.kobozevva.blog.model.Post;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class PostTestData {

    private PostTestData() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a builder for PostDto
     */
    public static PostBuilder post() {
        return new PostBuilder();
    }

    /**
     * Creates a builder for PostDto
     */
    public static PostDtoBuilder postDto() {
        return new PostDtoBuilder();
    }

    /**
     * Creates a builder for NewPostDto
     */
    public static NewPostDtoBuilder newPostDto() {
        return new NewPostDtoBuilder();
    }

    /**
     * Creates a builder for UpdatePostDto
     */
    public static UpdatePostDtoBuilder updatePostDto() {
        return new UpdatePostDtoBuilder();
    }

    /**
     * Creates a builder for PostPreviewDto
     */
    public static PostPreviewDtoBuilder postPreviewDto() {
        return new PostPreviewDtoBuilder();
    }

    /**
     * Creates a builder for PostsDto
     */
    public static PostsDtoBuilder postsDto() {
        return new PostsDtoBuilder();
    }

    // ==================== BUILDER CLASSES ====================

    public static class PostBuilder {
        private Long id = 1L;
        private String title = "Test Post Title";
        private String text = "Test post content with some meaningful text for testing purposes.";
        private Integer likesCount = 0;

        public PostBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PostBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public PostBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public PostBuilder withLikesCount(Integer likesCount) {
            this.likesCount = likesCount;
            return this;
        }

        public Post build() {
            return Post.builder()
                    .id(id)
                    .title(title)
                    .text(text)
                    .likesCount(likesCount)
                    .build();
        }
    }

    public static class PostDtoBuilder {
        private Long id = 1L;
        private String title = "Test Post Title";
        private String text = "Test post content with some meaningful text for testing purposes.";
        private List<String> tags = new ArrayList<>(List.of("java", "spring", "testing"));
        private Long likesCount = 0L;
        private Long commentsCount = 0L;

        public PostDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PostDtoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public PostDtoBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public PostDtoBuilder withTags(String... tags) {
            this.tags = Arrays.asList(tags);
            return this;
        }

        public PostDtoBuilder withTags(List<String> tags) {
            this.tags = new ArrayList<>(tags);
            return this;
        }

        public PostDtoBuilder withLikesCount(Long likesCount) {
            this.likesCount = likesCount;
            return this;
        }

        public PostDtoBuilder withCommentsCount(Long commentsCount) {
            this.commentsCount = commentsCount;
            return this;
        }

        public PostDto build() {
            return PostDto.builder()
                    .id(id)
                    .title(title)
                    .text(text)
                    .tags(tags)
                    .likesCount(likesCount)
                    .commentsCount(commentsCount)
                    .build();
        }
    }

    public static class NewPostDtoBuilder {
        private String title = "Test Post Title";
        private String text = "Test post content with some meaningful text for testing purposes.";
        private List<String> tags = new ArrayList<>(List.of("java", "spring", "testing"));

        public NewPostDtoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public NewPostDtoBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public NewPostDtoBuilder withTags(String... tags) {
            this.tags = Arrays.asList(tags);
            return this;
        }

        public NewPostDtoBuilder withTags(List<String> tags) {
            this.tags = new ArrayList<>(tags);
            return this;
        }

        public NewPostDto build() {
            return NewPostDto.builder()
                    .title(title)
                    .text(text)
                    .tags(tags)
                    .build();
        }
    }

    public static class UpdatePostDtoBuilder extends NewPostDtoBuilder {
        private Long id = 1L;

        public UpdatePostDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public UpdatePostDtoBuilder withTitle(String title) {
            super.withTitle(title);
            return this;
        }

        @Override
        public UpdatePostDtoBuilder withText(String text) {
            super.withText(text);
            return this;
        }

        @Override
        public UpdatePostDtoBuilder withTags(String... tags) {
            super.withTags(tags);
            return this;
        }

        @Override
        public UpdatePostDtoBuilder withTags(List<String> tags) {
            super.withTags(tags);
            return this;
        }

        public UpdatePostDto build() {
            return UpdatePostDto.builder()
                    .id(id)
                    .title(super.title)
                    .text(super.text)
                    .tags(super.tags)
                    .build();
        }
    }

    public static class PostPreviewDtoBuilder extends PostDtoBuilder {

        @Override
        public PostPreviewDtoBuilder withId(Long id) {
            super.withId(id);
            return this;
        }

        @Override
        public PostPreviewDtoBuilder withTitle(String title) {
            super.withTitle(title);
            return this;
        }

        @Override
        public PostPreviewDtoBuilder withText(String text) {
            super.withText(text);
            return this;
        }

        @Override
        public PostPreviewDtoBuilder withTags(String... tags) {
            super.withTags(tags);
            return this;
        }

        @Override
        public PostPreviewDtoBuilder withTags(List<String> tags) {
            super.withTags(tags);
            return this;
        }

        @Override
        public PostPreviewDtoBuilder withLikesCount(Long likesCount) {
            super.withLikesCount(likesCount);
            return this;
        }

        @Override
        public PostPreviewDtoBuilder withCommentsCount(Long commentsCount) {
            super.withCommentsCount(commentsCount);
            return this;
        }

        public PostPreviewDto build() {
            return PostPreviewDto.builder()
                    .id(super.id)
                    .title(super.title)
                    .text(super.text)
                    .tags(super.tags)
                    .likesCount(super.likesCount)
                    .commentsCount(super.commentsCount)
                    .build();
        }
    }

    public static class PostsDtoBuilder {
        private List<PostPreviewDto> posts = new ArrayList<>();
        private Boolean hasPrev = false;
        private Boolean hasNext = true;
        private Integer lastPage = 1;

        public PostsDtoBuilder withPosts(PostPreviewDto... posts) {
            this.posts = Arrays.asList(posts);
            return this;
        }

        public PostsDtoBuilder withPosts(List<PostPreviewDto> posts) {
            this.posts = new ArrayList<>(posts);
            return this;
        }

        public PostsDtoBuilder withHasPrev(Boolean hasPrev) {
            this.hasPrev = hasPrev;
            return this;
        }

        public PostsDtoBuilder withHasNext(Boolean hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        public PostsDtoBuilder withLastPage(Integer lastPage) {
            this.lastPage = lastPage;
            return this;
        }

        public PostsDto build() {
            return PostsDto.builder()
                    .posts(posts)
                    .hasPrev(hasPrev)
                    .hasNext(hasNext)
                    .lastPage(lastPage)
                    .build();
        }
    }

    // ==================== PREDEFINED FIXTURES ====================

    /**
     * Creates a default Post with standard test data
     */
    public static Post aDefaultPost() {
        return post().build();
    }

    /**
     * Creates a default PostDto with standard test data
     */
    public static PostDto aDefaultPostDto() {
        return postDto().build();
    }

    /**
     * Creates a popular PostDto with many likes and comments
     */
    public static PostDto aPopularPostDto() {
        return postDto()
                .withTitle("Popular Post")
                .withText("This is a very popular post with lots of engagement!")
                .withLikesCount(999L)
                .withCommentsCount(456L)
                .withTags("trending", "viral", "popular")
                .build();
    }

    /**
     * Creates a post with no tags
     */
    public static PostDto aPostWithoutTags() {
        return postDto()
                .withTags(new ArrayList<>())
                .build();
    }

    /**
     * Creates a default NewPostDto for creation operations
     */
    public static NewPostDto aDefaultNewPostDto() {
        return newPostDto().build();
    }

    /**
     * Creates a NewPostDto with minimal valid data
     */
    public static NewPostDto aMinimalNewPostDto() {
        return newPostDto()
                .withTitle("Minimal")
                .withText("A")
                .withTags()
                .build();
    }

    /**
     * Creates a NewPostDto with maximum length data (for boundary testing)
     */
    public static NewPostDto aMaximalNewPostDto() {
        return newPostDto()
                .withTitle("A".repeat(255))
                .withText("A".repeat(10000))
                .withTags("tag1", "tag2", "tag3")
                .build();
    }

    /**
     * Creates a default UpdatePostDto
     */
    public static UpdatePostDto aDefaultUpdatePostDto() {
        return updatePostDto().build();
    }

    /**
     * Creates an UpdatePostDto with custom ID
     */
    public static UpdatePostDto anUpdatePostDtoWithId(Long id) {
        return updatePostDto()
                .withId(id)
                .withTitle("Updated Title")
                .withText("Updated content with new information.")
                .withTags("updated", "modified")
                .build();
    }

    /**
     * Creates a default PostPreviewDto
     */
    public static PostPreviewDto aDefaultPostPreviewDto() {
        return postPreviewDto().build();
    }

    /**
     * Creates a PostPreviewDto with long text (to test preview truncation)
     */
    public static PostPreviewDto aPostPreviewWithLongText() {
        return postPreviewDto()
                .withTitle("Long Post")
                .withText("A".repeat(200))
                .build();
    }

    /**
     * Creates a PostsDto with a single post
     */
    public static PostsDto aSinglePostPage() {
        return postsDto()
                .withPosts(aDefaultPostPreviewDto())
                .withHasPrev(false)
                .withHasNext(false)
                .withLastPage(1)
                .build();
    }

    /**
     * Creates a PostsDto with multiple posts (first page)
     */
    public static PostsDto aFirstPageOfPosts(int totalPages) {
        List<PostPreviewDto> posts = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            posts.add(postPreviewDto()
                    .withId((long) i)
                    .withTitle("Post " + i)
                    .withText("Content for post " + i)
                    .build());
        }

        return postsDto()
                .withPosts(posts)
                .withHasPrev(false)
                .withHasNext(totalPages > 1)
                .withLastPage(totalPages)
                .build();
    }

    /**
     * Creates a collection of posts for testing
     */
    public static List<PostDto> aListOfPosts(int count) {
        List<PostDto> posts = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            posts.add(postDto()
                    .withId((long) i)
                    .withTitle("Post " + i)
                    .withText("Content for post " + i)
                    .withTags("tag" + i)
                    .build());
        }
        return posts;
    }
}