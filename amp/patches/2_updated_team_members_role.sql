TRUNCATE `amp_team_member_roles`;
INSERT INTO `amp_team_member_roles` (
       `amp_team_mem_role_id`,
       `role`,
       `description`,
       `read_permission`,
       `write_permission`,
       `delete_permission`,
       `team_head`
       )
VALUES
 (1,'Workspace Manager','Workspace Manager',1,1,1,1),
 (2,'Workspace Member','Workspace Member',1,0,0,2),
 (3,'Top Management','Top Management',0,0,0,3),
 (4,'Bilat-Department','Head Department',0,0,0,4);
