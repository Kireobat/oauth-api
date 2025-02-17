create table if not exists oauth_api.groups (
    id serial primary key not null,
    name varchar not null,
    created_time timestamptz not null,
    edited_time timestamptz not null,
    created_by integer references oauth_api.users(id)
)