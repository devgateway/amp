CREATE OR REPLACE VIEW v_pledges_funding as
 select 
    `ap`.`id` AS `pledgeid`,
    `ap`.`title` AS `title`,
    ((((`apd`.`amount` * coalesce(`fps`.`sector_percentage`,
    100)) / 100) * coalesce(`fpl`.`location_percentage`,
    100)) / 100) AS `amount`,
    `apd`.`id` AS `amp_fund_detail_id`,
    `ac`.`currency_code` AS `currency_code`,
    `aorg`.`name` AS `org_name`,
    `catv`.`category_value` AS `aid_modality_name`,
    `catv1`.`category_value` AS `type_of_assistance_name`,
    `s`.`name` AS `p_sectorname`,
    `fps`.`sector_percentage` AS `spercenatage`,
    `l`.`region` AS `region_name` 
  from 
    ((((((((((`amp_funding_pledges` `ap` join `amp_funding_pledges_details` `apd` on((`ap`.`id` = `apd`.`pledge_id`))) join `amp_category_value` `catv` on((`apd`.`aid_modality` = `catv`.`id`))) join `amp_category_value` `catv1` on((`apd`.`type_of_assistance` = `catv1`.`id`))) join `amp_currency` `ac` on((`apd`.`currency` = `ac`.`amp_currency_id`))) join `amp_organisation` `aorg` on((`ap`.`amp_org_id` = `aorg`.`amp_org_id`))) join `amp_funding_pledges_sectors` `fps` on((`ap`.`id` = `fps`.`pledge_id`))) join `amp_sector` `s` on((`fps`.`amp_sector_id` = `s`.`amp_sector_id`))) join `amp_funding_pledges_location` `fpl` on((`ap`.`id` = `fpl`.`pledge_id`))) join `amp_location` `l` on((`fpl`.`amp_location_id` = `l`.`amp_location_id`))) join `amp_category_value_location` `cvl` on((`l`.`region_location_id` = `cvl`.`id`))) 
  group by 
    `ap`.`id`,`apd`.`id`,`aorg`.`amp_org_id`,`s`.`amp_sector_id`;