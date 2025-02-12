create table if not exists oauth_api.reactions (
    id serial primary key not null,
    user_id integer not null references oauth_api.users(id),
    blog_id integer not null references oauth_api.blogs(id),
    reaction varchar,
    created_time timestamptz not null
);

create sequence if not exists oauth_api.reactions_seq increment by 1 start with 1;