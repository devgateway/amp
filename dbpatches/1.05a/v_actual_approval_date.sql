CREATE OR REPLACE VIEW `v_actual_approval_date` AS 
select `amp_activity`.`amp_activity_id` AS 
`amp_activity_id`,`amp_activity`.`actual_approval_date` AS 
`actual_approval_date` from `amp_activity` order by `amp_activity`.`amp_activity_id`;