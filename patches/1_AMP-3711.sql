update amp_team_member_roles set role  = 'Workspace Manager' , description = 'Workspace Manager ' where amp_team_mem_role_id = 1;
update amp_team_member_roles set role  = 'Workspace Member' , description = 'Workspace Member' where amp_team_mem_role_id = 2;
update amp_team_member set amp_member_role_id = 2 where amp_member_role_id > 2;
delete from amp_team_member_roles where amp_team_mem_role_id > 2;

