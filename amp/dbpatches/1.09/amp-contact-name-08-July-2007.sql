CREATE OR REPLACE view `v_contact_name` AS
 (select `amp_activity`.`amp_activity_id`  
AS `amp_activity_id`, concat_ws(' ',`amp_activity`.`cont_first_name`
, `amp_activity`.`cont_last_name` ) as contact, 
`amp_activity`.`amp_activity_id` as contact_id  
from `amp_activity` 
where  trim(concat_ws(' ',`amp_activity`.`cont_first_name`
, `amp_activity`.`cont_last_name` )) != ''
 )
UNION (
select `amp_activity`.`amp_activity_id` 
AS `amp_activity_id`, concat_ws(' ',`amp_activity`.`mofed_cnt_first_name`
, `amp_activity`.`mofed_cnt_last_name` ) as contact, 
`amp_activity`.`amp_activity_id` as contact_id
from `amp_activity`
where  trim(concat_ws(' ',`amp_activity`.`mofed_cnt_first_name`
, `amp_activity`.`mofed_cnt_last_name` )) != ''
);
