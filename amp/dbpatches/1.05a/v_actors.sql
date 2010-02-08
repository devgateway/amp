CREATE OR REPLACE VIEW `v_actors`  AS 
select `ai`.`amp_activity_id` AS `amp_activity_id`,`act`.`name` AS `name`,
`act`.`amp_actor_id` AS `amp_actor_id` from 
(((`amp_activity` `a` join `amp_measure` `m`) join `amp_issues` `ai`) 
join `amp_actor` `act`) where ((`ai`.`amp_activity_id` = `a`.`amp_activity_id`) and
 (`ai`.`amp_issue_id` = `m`.`amp_issue_id`) and 
 (`act`.`amp_measure_id` = `m`.`amp_measure_id`)) order by `ai`.`amp_activity_id`;