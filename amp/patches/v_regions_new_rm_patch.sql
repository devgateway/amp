CREATE OR REPLACE VIEW `v_regions` AS 
select `ra`.`amp_activity_id` AS `amp_activity_id`,
`cvl`.`location_name` AS `region`,`l`.`region_location_id` AS `region_id`,
sum(`ra`.`location_percentage`) AS `location_percentage` 
from ((`amp_activity_location` `ra` join `amp_location` `l` 
on((`ra`.`amp_location_id` = `l`.`amp_location_id`))) join amp_category_value_location cvl) 
where (`l`.`region_location_id` is not null AND l.region_location_id = cvl.id) 
group by `ra`.`amp_activity_id`,`l`.`region_location_id` 
order by `ra`.`amp_activity_id`,`cvl`.`location_name`;