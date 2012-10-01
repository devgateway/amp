/*
 * National Planning Objectives
 */

CREATE OR REPLACE VIEW `v_nationalojectives` AS 
select `a`.`amp_activity_id` AS `amp_activity_id`,
       `t`.`name` AS `name`,`ap`.`amp_program_id` AS `amp_program_id` 
from ((`amp_activity` `a` 
join `amp_activity_program` `ap` on(((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 1))))  
join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) 
order by `a`.`amp_activity_id`,`t`.`name`;


DELETE FROM `amp_columns` where `columnName` = 'National Planning Objectives';
INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES           ('National Planning Objectives', 'nationalojectives', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_nationalojectives');


/*
 * Primary Program
 */

CREATE OR REPLACE VIEW `v_primaryprogram` AS 
select `a`.`amp_activity_id` AS `amp_activity_id`,
       `t`.`name` AS `name`,`ap`.`amp_program_id` AS `amp_program_id` 
from ((`amp_activity` `a` 
join `amp_activity_program` `ap` on(((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 2)))) 
join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) 
order by `a`.`amp_activity_id`,`t`.`name`;


DELETE FROM `amp_columns` where `columnName` = 'Primary Program';
INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`) 
VALUES           ('Primary Program', 'primaryprogram', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_primaryprogram');


/*
 * Secondary Program
 */

CREATE OR REPLACE VIEW `v_secondaryprogram` AS 
select `a`.`amp_activity_id` AS `amp_activity_id`,
       `t`.`name` AS `name`,`ap`.`amp_program_id` AS `amp_program_id` 
from ((`amp_activity` `a` 
join `amp_activity_program` `ap` on(((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 3)))) 
join `amp_theme` `t` on((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) 
order by `a`.`amp_activity_id`,`t`.`name`;


DELETE FROM `amp_columns` where `columnName` = 'Secondary Program';
INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`) 
VALUES           ('Secondary Program', 'secondaryprogram', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_secondaryprogram');


/*
 * Program
 */

DELETE FROM `amp_columns_order` where `columnName` = 'Program';

INSERT INTO `amp_columns_order`(`columnName`,`indexOrder`)
SELECT 'Program', max(`indexOrder`) + 1 from `amp_columns_order`;
