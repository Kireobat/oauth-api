create table if not exists oauth_api.users (
    id serial primary key not null,
    github_id varchar,
    username varchar not null,
    email varchar,
    avatar_url varchar,
    registration_date timestamptz not null
);

create sequence if not exists oauth_api.users_seq increment by 1 start with 1;