Update amp_team_member_roles set approver=false;
Insert into amp_team_member_roles (role, description,team_head,approver) values ('Workspace Approver','Workspace Approver',false,true);
Alter table AMP_TEAM_MEMBER_ROLES drop column read_permission;
Alter table AMP_TEAM_MEMBER_ROLES drop column write_permission;
Alter table AMP_TEAM_MEMBER_ROLES drop column delete_permission;
Alter table amp_team_member drop column read_permission;
Alter table amp_team_member drop column write_permission;
Alter table amp_team_member drop column delete_permission;