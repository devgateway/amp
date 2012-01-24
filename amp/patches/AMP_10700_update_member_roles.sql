UPDATE amp_team_member_roles set team_head=true where role='Workspace Manager';
UPDATE amp_team_member_roles set team_head=false where role!='Workspace Manager';