alter table oauth_api.users add if not exists group_id integer references oauth_api.groups(id);
alter table oauth_api.users add if not exists edited_time timestamptz;