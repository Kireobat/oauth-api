alter table oauth_api.blogs add if not exists topic_id integer references oauth_api.topics(id);