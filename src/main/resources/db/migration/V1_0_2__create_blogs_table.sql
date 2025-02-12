create table if not exists oauth_api.blogs (
    id serial primary key not null,
    user_id integer not null references oauth_api.users(id),
    title varchar,
    description varchar,
    created_time timestamptz not null,
    latest_edit_time timestamptz
);

create sequence if not exists oauth_api.blogs_seq increment by 1 start with 1;