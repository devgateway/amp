CREATE OR REPLACE VIEW `v_measures_taken` AS 
select `ai`.`amp_activity_id` AS `amp_activity_id`,
`m`.`name` AS `name`,`m`.`amp_measure_id` AS `amp_measure_id` 
from ((`amp_activity` `a` join `amp_measure` `m`) join `amp_issues` `ai`) 
where ((`ai`.`amp_activity_id` = `a`.`amp_activity_id`) and 
(`ai`.`amp_issue_id` = `m`.`amp_issue_id`)) order by `ai`.`amp_activity_id`;