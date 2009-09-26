/*delete odd data*/
delete from amp_message_state where member_Id not in (select amp_team_mem_id from amp_team_member);
/*fill new field with necessary data*/
update amp_message_state s 
set s.receiver_id=(select amp_team_mem_id from amp_team_member tm where tm.amp_team_mem_id=s.member_id) 
where receiver_id is null;
/*drop member_id column*/
alter table amp_message_state drop column member_id;