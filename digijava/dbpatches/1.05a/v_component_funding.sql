CREATE OR REPLACE VIEW `v_component_funding` AS select 
`f`.`activity_id` AS `amp_activity_id`,
`f`.`amp_component_funding_id` AS `amp_component_funding_id`,
`f`.`amp_component_funding_id` AS `amp_fund_detail_id`,
`c`.`title` AS `component_name`,
`f`.`transaction_type` AS `transaction_type`,
`f`.`adjustment_type` AS `adjustment_type`,
date_format(`f`.`transaction_date`,_latin1'%Y-%m-%d') AS `transaction_date`,
`f`.`transaction_amount` AS `transaction_amount`,
`f`.`currency_id` AS `currency_id`,
`cu`.`currency_code` AS `currency_code`,
`getExchange`(`cu`.`currency_code`,`f`.`transaction_date`) AS `exchange_rate` 
from ((`amp_component_funding` `f` join `amp_components` `c`) 
join `amp_currency` `cu`) where ((`cu`.`amp_currency_id` = `f`.`currency_id`) 
and (`f`.`amp_component_id` = `c`.`amp_component_id`)) order by `f`.`activity_id`;