CREATE OR REPLACE VIEW `v_nationalobjectives` AS
  select
    `a`.`amp_activity_id` AS `amp_activity_id`,
    `t`.`name` AS `name`,
    `ap`.`amp_program_id` AS `amp_program_id`,
    `ap`.`program_percentage` AS `program_percentage`
  from
    ((`amp_activity` `a` join `amp_activity_program` `ap` on(((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 1)))) join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`)))
    where t.`parent_theme_id` is not null AND T.`level`=2
  order by
    `a`.`amp_activity_id`,`t`.`name`;



CREATE OR REPLACE VIEW `v_nationalobjectives_parent` AS

  select `a`.`amp_activity_id` AS `amp_activity_id`,
         `t`.`name` AS `name`,
         `ap`.`amp_program_id` AS `amp_program_id`,
         `ap`.`program_percentage` AS `program_percentage`
  from ((`amp_activity` `a`
       join `amp_activity_program` `ap` on (((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (
       `ap`.`program_setting` = 1))))
       join `amp_theme` `t` on ((`t`.`amp_theme_id` = `ap`.`amp_program_id`)))
  where t.`level` = 1

union


  select `a`.`amp_activity_id` AS `amp_activity_id`,
         `t2`.`name` AS `name`,
         `ap`.`amp_program_id` AS `amp_program_id`,
         `ap`.`program_percentage` AS `program_percentage`
  from ((`amp_activity` `a`
       join `amp_activity_program` `ap` on (((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (
       `ap`.`program_setting` = 1))))
       join `amp_theme` `t` on ((`t`.`amp_theme_id` = `ap`.`amp_program_id`))
       join `amp_theme` `t2` on ((`t`.`parent_theme_id` = `t2`.`amp_theme_id`)))
  where t2.`level` = 1
  order by amp_activity_id,name;