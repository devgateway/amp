CREATE OR REPLACE  VIEW `v_financing_instrument` AS (  select `f`.`amp_activity_id` AS `amp_activity_id`,         `val`.`category_value` AS `name`,         `f`.`financing_instr_category_value` AS `amp_modality_id`  from (`amp_funding` `f`       join `amp_category_value` `val`)  where (`f`.`financing_instr_category_value` = `val`.`id`)) union (  select `eu`.`amp_activity_id` AS `amp_activity_id`,         `val`.`category_value` AS `category_value`,         `eu_con`.`financing_instr_category_value` AS `financing_instr_category_value`  from ((`amp_eu_activity` `eu`       join `amp_eu_activity_contributions` `eu_con`)       join `amp_category_value` `val`)  where ((`eu_con`.`eu_activity_id` = `eu`.`id`) and        (`eu_con`.`financing_instr_category_value` = `val`.`id`)))  order by    `amp_activity_id`,`name`;


