-- POSTS
create table if not exists posts
(
    id             bigserial primary key,
    title          varchar(255)             not null,
    text           text                     not null,
    likes_count    integer                  not null default 0,
    comments_count integer                  not null default 0,
    created_at     timestamp with time zone not null default now(),
    updated_at     timestamp with time zone not null default now()
);

create index if not exists idx_posts_created_at
    on posts (created_at desc);

create index if not exists idx_posts_title
    on posts using gin (to_tsvector('simple', title));

create index if not exists idx_posts_text
    on posts using gin (to_tsvector('simple', text));

-- TAGS
create table if not exists tags
(
    id   bigserial primary key,
    name varchar(64) not null unique
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

-- COMMENTS
create table if not exists comments
(
    id         bigserial primary key,
    post_id    bigint                   not null,
    text       text                     not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    constraint fk_comments_post
        foreign key (post_id)
            references posts (id)
            on delete cascade
);

create index if not exists idx_comments_post_id
    on comments (post_id);

-- LIKES
create table if not exists likes
(
    id         bigserial primary key,
    post_id    bigint                   not null,
    created_at timestamp with time zone not null default now(),
    constraint fk_likes_post
        foreign key (post_id)
            references posts (id)
            on delete cascade
);

create index if not exists idx_likes_post_id
    on likes (post_id);

-- POST IMAGES
create table if not exists post_images
(
    post_id      bigint primary key,
    image        bytea                    not null,
    content_type varchar(64)              not null,
    updated_at   timestamp with time zone not null default now(),
    constraint fk_post_images_post
        foreign key (post_id)
            references posts (id)
            on delete cascade
);