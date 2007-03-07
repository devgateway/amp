CREATE OR REPLACE VIEW `v_sectors` AS 
select `sa`.`amp_activity_id` AS `amp_activity_id`,`s`.`name` AS `name`,`sa`.`amp_sector_id` 
AS `amp_sector_id`, sa.sector_percentage as `sector_percentage` from (`amp_activity_sector` `sa` join 
`amp_sector` `s` on((`sa`.`amp_sector_id` = `s`.`amp_sector_id`))) order by 
`sa`.`amp_activity_id`,`s`.`name`;