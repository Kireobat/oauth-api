alter table oauth_api.blogs rename column latest_edit_time to edited_time;
alter table oauth_api.blogs rename column user_id to created_by;