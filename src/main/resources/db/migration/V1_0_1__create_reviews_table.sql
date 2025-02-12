create table if not exists oauth_api.reviews (
    id serial primary key not null,
    user_id integer not null references oauth_api.users(id),
    title varchar,
    description varchar,
    rating integer not null,
    created_time timestamptz not null
);

create sequence if not exists oauth_api.reviews_seq increment by 1 start with 1;