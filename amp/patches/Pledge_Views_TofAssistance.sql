CREATE OR REPLACE VIEW v_pledges_type_of_assistance AS 

select `p`.`id` AS `pledge_id`,`val`.`category_value` AS `terms_assist_name`,`val`.`id` AS `terms_assist_code`
from `amp_funding_pledges` `p` join `amp_funding_pledges_details` `fund` on p.`id` = fund.`pledge_id` join `amp_category_value` `val` on fund.`type_of_assistance` = val.`id`
group by `p`.`id`,`val`.`id` order by `p`.`id`,`val`.`category_value`;