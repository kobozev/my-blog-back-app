-- POSTS
create table if not exists posts
(
    id          bigserial primary key,
    title       varchar(255) not null,
    text        text         not null,
    likes_count integer      not null default 0
);

create index if not exists idx_posts_title
    on posts using gin (to_tsvector('simple', title));

create index if not exists idx_posts_text
    on posts using gin (to_tsvector('simple', text));

-- TAGS
create table if not exists tags
(
    id       bigserial primary key,
    tag_name varchar(64) not null unique
);

-- POST -> TAGS (M:N)
create table if not exists post_tags
(
    post_id bigint not null,
    tag_id  bigint not null,
    primary key (post_id, tag_id),
    constraint fk_post_tags_post
        foreign key (post_id)
            references posts (id)
            on delete cascade,
    constraint fk_post_tags_tag
        foreign key (tag_id)
            references tags (id)
            on delete cascade
);

create index if not exists idx_post_tags_post
    on post_tags (post_id);

create index if not exists idx_post_tags_tag
    on post_tags (tag_id);

-- POST COMMENTS
create table if not exists post_comments
(
    id      bigserial primary key,
    post_id bigint not null,
    text    text   not null,
    constraint fk_comments_post
        foreign key (post_id)
            references posts (id)
            on delete cascade
);

create index if not exists idx_comments_post_id
    on post_comments (post_id);


-- POST IMAGES
create table if not exists post_images
(
    post_id    bigint primary key,
    image_data bytea not null,
    constraint fk_post_images_post
        foreign key (post_id)
            references posts (id)
            on delete cascade
);