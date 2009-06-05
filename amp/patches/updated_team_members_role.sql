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
 (1,'Team Leader','Team Leader',1,1,1,true),
 (2,'Team Member','Team Member',1,0,0,false),
 (3,'Top Management','Top Management',0,0,0,false),
 (4,'Bilat-Department','Head Department',0,0,0,false);
