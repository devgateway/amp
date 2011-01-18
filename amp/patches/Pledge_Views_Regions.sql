CREATE OR REPLACE VIEW v_pledges_regions AS
 select `ra`.`pledge_id` AS `pledge_id`,
         `l`.`region` AS `region`,
         `l`.`region_location_id` AS `region_id`,
         sum(`ra`.`location_percentage`) AS `location_percentage`
  from `amp_funding_pledges_location` `ra`
       join `amp_category_value_location` `cvl` on ra.`location_id` = `cvl`.`id`
       join `amp_location` `l` on cvl.`id` = `l`.`location_id`
  where (`l`.`region_location_id` is not null)
  group by `ra`.`pledge_id`,
           `ra`.`location_id`
  order by `ra`.`pledge_id`, `l`.`name`;