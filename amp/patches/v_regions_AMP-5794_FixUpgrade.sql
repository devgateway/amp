CREATE OR REPLACE VIEW `v_regions` AS 
select
    `ra`.`amp_activity_id` AS `amp_activity_id`,
    `l`.`region` AS `region`,
    `l`.`region_location_id` AS `region_id`,
    sum(`ra`.`location_percentage`) AS `location_percentage`,
    `l`.`location_id` AS `location_id`,
    (case when (`l`.`woreda_id` is not null) then (
  select
    `amp_category_value_location`.`parent_location` AS `parent_location`
  from
    `amp_category_value_location`
  where
    (`amp_category_value_location`.`id` = `l`.`location_id`)) when (`l`.`zone_id` is not null) then `l`.`location_id` end) AS `zone_id`
  from
    ((`amp_activity_location` `ra` join `amp_location` `l` on((`ra`.`amp_location_id` = `l`.`amp_location_id`))) join `amp_category_value_location` `cvl` on((`l`.`location_id` = `cvl`.`id`)))
  where
    (`l`.`region_location_id` is not null)
  group by
    `ra`.`amp_activity_id`,`l`.`region_id`
  order by
    `ra`.`amp_activity_id`,`l`.`name`;
   