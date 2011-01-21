CREATE OR REPLACE VIEW `v_primaryprogram_cached` AS 
select `a`.`amp_activity_id` AS `amp_activity_id`,
if((`ap`.`program_setting` = 3),`t`.`name`,NULL) AS `name`,
`ap`.`amp_program_id` AS `amp_program_id`,
`ap`.`program_percentage` AS `program_percentage`,
`ap`.`program_setting` AS `program_setting` 
from ((`amp_activity` `a` left join `amp_activity_program` `ap` on((`a`.`amp_activity_id` = `ap`.`amp_activity_id`))) left join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) 
order by 
`a`.`amp_activity_id`,`t`.`name`;