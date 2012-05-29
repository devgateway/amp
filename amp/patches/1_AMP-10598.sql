create or replace view v_mode_of_payment  as select `a`.`amp_activity_id` AS `amp_activity_id`,
IF(`val`.`category_value` is not null, val.category_value, 'Mode of Payment Unallocated' ) AS `mode_of_payment_name`,
if(`val`.`id` is not null, val.id, 0) AS `mode_of_payment_code` 
from (((`amp_activity` `a` join `amp_funding` `fund` on (`fund`.`amp_activity_id` = `a`.`amp_activity_id`) ) 
left join `amp_category_value` `val` on (`val`.`id` = `fund`.`mode_of_payment_category_va`) ) ) 
where   true 
group by `a`.`amp_activity_id`,`val`.`id` order by `a`.`amp_activity_id`,`val`.`category_value`