drop table if exists dg_msg_befor_hash;


create table dg_msg_befor_hash as select * from dg_message;


 delete m from dg_message as m  
 where m.message_key like '-%' or (substr(m.message_key,1,1) in ('1','2','3','4','5','6','7','8','9','0'));
 
 delete gs from amp_global_settings as gs where gs.settingsName like 'Translation hashcode patch';
 
 