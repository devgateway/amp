CREATE or replace VIEW `v_nationalobjectives` AS
  select
    `a`.`amp_activity_id` AS `amp_activity_id`,
    `t`.`name` AS `name`,
    `ap`.`amp_program_id` AS `amp_program_id`,
    `ap`.`program_percentage` AS `program_percentage`
  from
    ((`amp_activity` `a` join `amp_activity_program` `ap` on(((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 1)))) join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`)))
  order by
    `a`.`amp_activity_id`,`t`.`name`;