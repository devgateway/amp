
drop table if exists dg_message_dup;  

drop table if exists dg_message_temp; 

drop table if exists dg_message_lower;

create table if not exists dg_message_lower  
(
select * from dg_message as m 
group by lower(lower(m.message_key)), m.lang_iso, m.site_id having count(m.message_key) =1 
) ;   

update  dg_message_lower set message_key=trim(message_key) where message_key like ' %' or message_key like '% ';

delete m from dg_message_lower as m, dg_site as s where (m.site_id=s.id) and (s.site_id not like 'amp');   

delete  from dg_message_lower where length(message_key) >= 254; 

delete FROM dg_message_lower where trim(message_key) like ''; 

drop table if exists dg_message;  
 
create table dg_message (SELECT * FROM dg_message_lower d ) ; 

