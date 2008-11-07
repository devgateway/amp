CREATE OR REPLACE VIEW `v_donor_date_hierarchy` AS (select `fd`.`amp_fund_detail_id` AS `amp_fund_detail_id`,`fd`.`transaction_date` 
AS `full_date`,year(`fd`.`transaction_date`) AS `year`,month(`fd`.`transaction_date`) AS `month`,
cast(monthname(`fd`.`transaction_date`) as char charset latin1) AS `month_name`,quarter(`fd`.`transaction_date`) 
AS `quarter`,concat(_latin1'Q',cast(quarter(`fd`.`transaction_date`) as char charset latin1)) AS `quarter_name` 
from ((`amp_activity` `a` join `amp_funding` `f`) join `amp_funding_detail` `fd`) where ((`a`.`amp_activity_id` = `f`.`amp_activity_id`) 
and (`f`.`amp_funding_id` = `fd`.`AMP_FUNDING_ID`)));
