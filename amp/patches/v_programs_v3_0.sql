/*
 * Primary Program
 */

CREATE OR REPLACE VIEW `v_primaryprogram` AS select `a`.`amp_activity_id` AS `amp_activity_id`,`t`.`name` AS `name`,`ap`.`amp_program_id` AS `amp_program_id`,ap.program_percentage as program_percentage from ((`amp_activity` `a` join `amp_activity_program` `ap` on(((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 2)))) join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) order by `a`.`amp_activity_id`,`t`.`name`;



/*
 * Secondary Program
 */

CREATE OR REPLACE VIEW `v_secondaryprogram` AS select `a`.`amp_activity_id` AS `amp_activity_id`,`t`.`name` AS `name`,`ap`.`amp_program_id` AS `amp_program_id`,ap.program_percentage as program_percentage from ((`amp_activity` `a` join `amp_activity_program` `ap` on(((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 3)))) join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) order by `a`.`amp_activity_id`,`t`.`name`;

/*
 * Change cell type to metaTextCell, so it will be able to hold metadata (percentage)
 */
update amp_columns set cellType='org.dgfoundation.amp.ar.cell.MetaTextCell' WHERE columnName in ('Primary Program', 'Secondary Program');
