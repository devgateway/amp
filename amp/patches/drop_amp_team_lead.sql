UPDATE amp_team_member, amp_team SET
amp_team_member.read_permission = 1,
amp_team_member.write_permission = 1,
amp_team_member.delete_permission = 1,
amp_team_member.amp_member_role_id = 1
WHERE amp_team_member.amp_team_id = amp_team.`amp_team_id`
AND amp_team_member.amp_team_mem_id = amp_team.`team_lead_id`
AND amp_team.team_lead_id is not null;
ALTER TABLE `amp_team` DROP FOREIGN KEY `FKD751DC18B6F06ABA`;
ALTER TABLE `amp_team` DROP COLUMN `team_lead_id`;
