CREATE OR REPLACE VIEW `v_componente` AS 
select `sa`.`amp_activity_id` AS `amp_activity_id`,`s`.`name` AS `name`,`sa`.`amp_sector_id` 
AS `amp_sector_id`, sa.percentage as `percentage` from (`amp_activity_componente` `sa` join 
`amp_sector` `s` on((`sa`.`amp_sector_id` = `s`.`amp_sector_id`))) order by 
`sa`.`amp_activity_id`,`s`.`name`;