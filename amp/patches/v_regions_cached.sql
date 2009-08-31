CREATE OR REPLACE VIEW `v_regions_cached` AS 
select `aa`.`amp_activity_id` AS `amp_activity_id`,
`al`.`region_location_id` AS `region_id`,
sum(`lp`.`location_percentage`) AS `location_percentage`,
`acvl`.`location_name` AS `Region` 
from (((`amp_activity` `aa` left join `amp_activity_location` `lp` on((`aa`.`amp_activity_id` = `lp`.`amp_activity_id`))) left join `amp_location` `al` on((`lp`.`amp_location_id` = `al`.`amp_location_id`))) left join `amp_category_value_location` `acvl` on((`al`.`region_location_id` = `acvl`.`id`))) 
group by 
`aa`.`amp_activity_id`,`al`.`region_location_id`,`acvl`.`location_name` 
 order by 
`aa`.`amp_activity_id`,`al`.`region_location_id`;