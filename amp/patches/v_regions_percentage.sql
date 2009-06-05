CREATE OR REPLACE VIEW `v_regions`  AS select `ra`.`amp_activity_id` AS `amp_activity_id`,`l`.`region` AS `region`,`l`.`region_id` AS `region_id`,`ra`.`location_percentage` AS `location_percentage` from (`amp_activity_location` `ra` join `amp_location` `l` on((`ra`.`amp_location_id` = `l`.`amp_location_id`)))  order by `ra`.`amp_activity_id`,`l`.`name`;