create table if not exists oauth_api.topics (
    id serial primary key not null,
    title varchar not null,
    description varchar,
    image_url varchar,
    created_time timestamptz not null,
    edited_time timestamptz,
    created_by integer references oauth_api.users(id)
);

create sequence if not exists oauth_api.topics_seq increment by 1 start with 1;