CREATE OR REPLACE VIEW `v_issues` AS 
select `ai`.`amp_activity_id` AS `amp_activity_id`,`ai`.`name` AS `name`,
`ai`.`amp_issue_id` AS `amp_issue_id` from (`amp_issues` `ai` join `amp_activity` `a`) 
where (`ai`.`amp_activity_id` = `a`.`amp_activity_id`) order by `ai`.`amp_activity_id`;