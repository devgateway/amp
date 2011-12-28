CREATE OR REPLACE VIEW `v_teams` AS 
select `a`.`amp_activity_id` AS `amp_activity_id`,`t`.`name` AS 
`name`,`t`.`amp_team_id` AS `amp_team_id` from 
(`amp_activity` `a` join `amp_team` `t`) where 
(`a`.`amp_team_id` = `t`.`amp_team_id`) 
order by `a`.`amp_activity_id`,`t`.`amp_team_id`;