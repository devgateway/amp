CREATE OR REPLACE  VIEW `v_regional_funding` AS 
 select `f`.`activity_id` AS `amp_activity_id`,
 `f`.`amp_regional_funding_id` AS `amp_regional_funding_id`,
 `f`.`amp_regional_funding_id` AS `amp_fund_detail_id`,
 `r`.`location_name` AS `region_name`,
 `f`.`transaction_type` AS `transaction_type`,
 `f`.`adjustment_type` AS `adjustment_type`,`f`.`transaction_date` AS `transaction_date`,
 `f`.`transaction_amount` AS `transaction_amount`,
 `c`.`currency_code` AS `currency_code`,`f`.`region_location_id` AS `region_id` 
 from (((`amp_regional_funding` `f` join `amp_category_value_location` `r`) join `amp_currency` `c`) 
   join amp_category_value v) 
 where ((`c`.`amp_currency_id` = `f`.`currency_id`) and (`f`.`region_location_id` = `r`.`id`) and 
   (r.parent_category_value = v.id) and (v.category_value = "Region") ) 
 order by `f`.`activity_id`;