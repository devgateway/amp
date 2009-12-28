 

drop table if exists dg_message_backup; 

create table if not exists dg_message_backup as select * from dg_message; 

delete m from dg_message as m where m.message_key not regexp '^-?[0-9]+$'; 


