CREATE OR REPLACE VIEW `v_secondaryprogram` AS 
  select 
    `a`.`amp_activity_id` AS `amp_activity_id`,
    `t`.`name` AS `name`,
    `ap`.`amp_program_id` AS `amp_program_id` 
  from 
    ((`amp_activity` `a` join `amp_activity_program` `ap` on(((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 3)))) join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) 
  order by 
    `a`.`amp_activity_id`,`t`.`name`;
    
    
    
 CREATE OR REPLACE VIEW `v_primaryprogram` AS 
  select 
    `a`.`amp_activity_id` AS `amp_activity_id`,
    `t`.`name` AS `name`,
    `ap`.`amp_program_id` AS `amp_program_id` 
  from 
    ((`amp_activity` `a` join `amp_activity_program` `ap` on(((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 2)))) join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) 
  order by 
    `a`.`amp_activity_id`,`t`.`name`;