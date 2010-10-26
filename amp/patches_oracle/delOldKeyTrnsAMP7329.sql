drop table dg_message_backup; 



create table dg_message_backup as select * from dg_message;

delete from dg_message where not regexp_like(message_key,'^-?[0-9]+$');

