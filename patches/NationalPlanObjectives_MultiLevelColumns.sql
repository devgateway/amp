drop view if exists v_nationalobjectives_parent;
drop view  if exists  `v_nationalojectives`;
drop view  if exists  `v_nationalobjectives`;
CREATE OR REPLACE VIEW `v_nationalobjectives_all_level_helper` AS
  select `a`.`amp_activity_id` AS `amp_activity_id`,
         `a`.`program_percentage` AS `program_percentage`,
         `a`.`amp_program_id` AS `amp_program_id`,
         `b`.`name` AS `n1`,
         `b`.`level` AS `l1`,
         `b1`.`name` AS `n2`,
         `b1`.`level` AS `l2`,
         `b2`.`name` AS `n3`,
         `b2`.`level` AS `l3`,
         `b3`.`name` AS `n4`,
         `b3`.`level` AS `l4`,
         `b4`.`name` AS `n5`,
         `b4`.`level` AS `l5`,
         `b5`.`name` AS `n6`,
         `b5`.`level` AS `l6`,
         `b6`.`name` AS `n7`,
         `b6`.`level` AS `l7`,
         `b7`.`name` AS `n8`,
         `b7`.`level` AS `l8`
  from ((((((((`amp_activity_program` `a`
       join `amp_theme` `b` on ((`a`.`amp_program_id` = `b`.`amp_theme_id`)))
       left join `amp_theme` `b1` on ((`b1`.`amp_theme_id` = `b`.`parent_theme_id`)))
       left join `amp_theme` `b2` on ((`b2`.`amp_theme_id` = `b1`.`parent_theme_id`)))
       left join `amp_theme` `b3` on ((`b3`.`amp_theme_id` = `b2`.`parent_theme_id`)))
       left join `amp_theme` `b4` on ((`b4`.`amp_theme_id` = `b3`.`parent_theme_id`)))
       left join `amp_theme` `b5` on ((`b5`.`amp_theme_id` = `b4`.`parent_theme_id`)))
       left join `amp_theme` `b6` on ((`b6`.`amp_theme_id` = `b5`.`parent_theme_id`)))
       left join `amp_theme` `b7` on ((`b7`.`amp_theme_id` = `b6`.`parent_theme_id`)));

CREATE OR REPLACE VIEW `v_nationalobjectives_level_0` AS
  select `v_nationalobjectives_all_level_helper`.`amp_activity_id` AS `amp_activity_id`,
         (case 0
            when `v_nationalobjectives_all_level_helper`.`l1` then
            `v_nationalobjectives_all_level_helper`.`n1`
            when `v_nationalobjectives_all_level_helper`.`l2` then
            `v_nationalobjectives_all_level_helper`.`n2`
            when `v_nationalobjectives_all_level_helper`.`l3` then
            `v_nationalobjectives_all_level_helper`.`n3`
            when `v_nationalobjectives_all_level_helper`.`l4` then
            `v_nationalobjectives_all_level_helper`.`n4`
            when `v_nationalobjectives_all_level_helper`.`l5` then
            `v_nationalobjectives_all_level_helper`.`n5`
            when `v_nationalobjectives_all_level_helper`.`l6` then
            `v_nationalobjectives_all_level_helper`.`n6`
            when `v_nationalobjectives_all_level_helper`.`l7` then
            `v_nationalobjectives_all_level_helper`.`n7`
            when `v_nationalobjectives_all_level_helper`.`l8` then
            `v_nationalobjectives_all_level_helper`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level_helper`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level_helper`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level_helper`
  having (`name` is not null);

CREATE OR REPLACE  VIEW `v_nationalobjectives_level_1` AS
  select `v_nationalobjectives_all_level_helper`.`amp_activity_id` AS `amp_activity_id`,
         (case 1
            when `v_nationalobjectives_all_level_helper`.`l1` then
            `v_nationalobjectives_all_level_helper`.`n1`
            when `v_nationalobjectives_all_level_helper`.`l2` then
            `v_nationalobjectives_all_level_helper`.`n2`
            when `v_nationalobjectives_all_level_helper`.`l3` then
            `v_nationalobjectives_all_level_helper`.`n3`
            when `v_nationalobjectives_all_level_helper`.`l4` then
            `v_nationalobjectives_all_level_helper`.`n4`
            when `v_nationalobjectives_all_level_helper`.`l5` then
            `v_nationalobjectives_all_level_helper`.`n5`
            when `v_nationalobjectives_all_level_helper`.`l6` then
            `v_nationalobjectives_all_level_helper`.`n6`
            when `v_nationalobjectives_all_level_helper`.`l7` then
            `v_nationalobjectives_all_level_helper`.`n7`
            when `v_nationalobjectives_all_level_helper`.`l8` then
            `v_nationalobjectives_all_level_helper`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level_helper`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level_helper`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level_helper`
  having (`name` is not null);

CREATE OR REPLACE  VIEW `v_nationalobjectives_level_2` AS
  select `v_nationalobjectives_all_level_helper`.`amp_activity_id` AS `amp_activity_id`,
         (case 2
            when `v_nationalobjectives_all_level_helper`.`l1` then
            `v_nationalobjectives_all_level_helper`.`n1`
            when `v_nationalobjectives_all_level_helper`.`l2` then
            `v_nationalobjectives_all_level_helper`.`n2`
            when `v_nationalobjectives_all_level_helper`.`l3` then
            `v_nationalobjectives_all_level_helper`.`n3`
            when `v_nationalobjectives_all_level_helper`.`l4` then
            `v_nationalobjectives_all_level_helper`.`n4`
            when `v_nationalobjectives_all_level_helper`.`l5` then
            `v_nationalobjectives_all_level_helper`.`n5`
            when `v_nationalobjectives_all_level_helper`.`l6` then
            `v_nationalobjectives_all_level_helper`.`n6`
            when `v_nationalobjectives_all_level_helper`.`l7` then
            `v_nationalobjectives_all_level_helper`.`n7`
            when `v_nationalobjectives_all_level_helper`.`l8` then
            `v_nationalobjectives_all_level_helper`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level_helper`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level_helper`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level_helper`
  having (`name` is not null);

CREATE OR REPLACE  VIEW `v_nationalobjectives_level_3` AS
  select `v_nationalobjectives_all_level_helper`.`amp_activity_id` AS `amp_activity_id`,
         (case 3
            when `v_nationalobjectives_all_level_helper`.`l1` then
            `v_nationalobjectives_all_level_helper`.`n1`
            when `v_nationalobjectives_all_level_helper`.`l2` then
            `v_nationalobjectives_all_level_helper`.`n2`
            when `v_nationalobjectives_all_level_helper`.`l3` then
            `v_nationalobjectives_all_level_helper`.`n3`
            when `v_nationalobjectives_all_level_helper`.`l4` then
            `v_nationalobjectives_all_level_helper`.`n4`
            when `v_nationalobjectives_all_level_helper`.`l5` then
            `v_nationalobjectives_all_level_helper`.`n5`
            when `v_nationalobjectives_all_level_helper`.`l6` then
            `v_nationalobjectives_all_level_helper`.`n6`
            when `v_nationalobjectives_all_level_helper`.`l7` then
            `v_nationalobjectives_all_level_helper`.`n7`
            when `v_nationalobjectives_all_level_helper`.`l8` then
            `v_nationalobjectives_all_level_helper`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level_helper`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level_helper`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level_helper`
  having (`name` is not null);

CREATE OR REPLACE  VIEW `v_nationalobjectives_level_4` AS
  select `v_nationalobjectives_all_level_helper`.`amp_activity_id` AS `amp_activity_id`,
         (case 4
            when `v_nationalobjectives_all_level_helper`.`l1` then
            `v_nationalobjectives_all_level_helper`.`n1`
            when `v_nationalobjectives_all_level_helper`.`l2` then
            `v_nationalobjectives_all_level_helper`.`n2`
            when `v_nationalobjectives_all_level_helper`.`l3` then
            `v_nationalobjectives_all_level_helper`.`n3`
            when `v_nationalobjectives_all_level_helper`.`l4` then
            `v_nationalobjectives_all_level_helper`.`n4`
            when `v_nationalobjectives_all_level_helper`.`l5` then
            `v_nationalobjectives_all_level_helper`.`n5`
            when `v_nationalobjectives_all_level_helper`.`l6` then
            `v_nationalobjectives_all_level_helper`.`n6`
            when `v_nationalobjectives_all_level_helper`.`l7` then
            `v_nationalobjectives_all_level_helper`.`n7`
            when `v_nationalobjectives_all_level_helper`.`l8` then
            `v_nationalobjectives_all_level_helper`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level_helper`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level_helper`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level_helper`
  having (`name` is not null);

CREATE OR REPLACE  VIEW `v_nationalobjectives_level_5` AS
  select `v_nationalobjectives_all_level_helper`.`amp_activity_id` AS `amp_activity_id`,
         (case 5
            when `v_nationalobjectives_all_level_helper`.`l1` then
            `v_nationalobjectives_all_level_helper`.`n1`
            when `v_nationalobjectives_all_level_helper`.`l2` then
            `v_nationalobjectives_all_level_helper`.`n2`
            when `v_nationalobjectives_all_level_helper`.`l3` then
            `v_nationalobjectives_all_level_helper`.`n3`
            when `v_nationalobjectives_all_level_helper`.`l4` then
            `v_nationalobjectives_all_level_helper`.`n4`
            when `v_nationalobjectives_all_level_helper`.`l5` then
            `v_nationalobjectives_all_level_helper`.`n5`
            when `v_nationalobjectives_all_level_helper`.`l6` then
            `v_nationalobjectives_all_level_helper`.`n6`
            when `v_nationalobjectives_all_level_helper`.`l7` then
            `v_nationalobjectives_all_level_helper`.`n7`
            when `v_nationalobjectives_all_level_helper`.`l8` then
            `v_nationalobjectives_all_level_helper`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level_helper`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level_helper`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level_helper`
  having (`name` is not null);


CREATE OR REPLACE  VIEW `v_nationalobjectives_level_6` AS
  select `v_nationalobjectives_all_level_helper`.`amp_activity_id` AS `amp_activity_id`,
         (case 6
            when `v_nationalobjectives_all_level_helper`.`l1` then
            `v_nationalobjectives_all_level_helper`.`n1`
            when `v_nationalobjectives_all_level_helper`.`l2` then
            `v_nationalobjectives_all_level_helper`.`n2`
            when `v_nationalobjectives_all_level_helper`.`l3` then
            `v_nationalobjectives_all_level_helper`.`n3`
            when `v_nationalobjectives_all_level_helper`.`l4` then
            `v_nationalobjectives_all_level_helper`.`n4`
            when `v_nationalobjectives_all_level_helper`.`l5` then
            `v_nationalobjectives_all_level_helper`.`n5`
            when `v_nationalobjectives_all_level_helper`.`l6` then
            `v_nationalobjectives_all_level_helper`.`n6`
            when `v_nationalobjectives_all_level_helper`.`l7` then
            `v_nationalobjectives_all_level_helper`.`n7`
            when `v_nationalobjectives_all_level_helper`.`l8` then
            `v_nationalobjectives_all_level_helper`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level_helper`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level_helper`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level_helper`
  having (`name` is not null);


CREATE OR REPLACE  VIEW `v_nationalobjectives_level_7` AS
  select `v_nationalobjectives_all_level_helper`.`amp_activity_id` AS `amp_activity_id`,
         (case 7
            when `v_nationalobjectives_all_level_helper`.`l1` then
            `v_nationalobjectives_all_level_helper`.`n1`
            when `v_nationalobjectives_all_level_helper`.`l2` then
            `v_nationalobjectives_all_level_helper`.`n2`
            when `v_nationalobjectives_all_level_helper`.`l3` then
            `v_nationalobjectives_all_level_helper`.`n3`
            when `v_nationalobjectives_all_level_helper`.`l4` then
            `v_nationalobjectives_all_level_helper`.`n4`
            when `v_nationalobjectives_all_level_helper`.`l5` then
            `v_nationalobjectives_all_level_helper`.`n5`
            when `v_nationalobjectives_all_level_helper`.`l6` then
            `v_nationalobjectives_all_level_helper`.`n6`
            when `v_nationalobjectives_all_level_helper`.`l7` then
            `v_nationalobjectives_all_level_helper`.`n7`
            when `v_nationalobjectives_all_level_helper`.`l8` then
            `v_nationalobjectives_all_level_helper`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level_helper`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level_helper`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level_helper`
  having (`name` is not null);

CREATE OR REPLACE  VIEW `v_nationalobjectives_level_8` AS
  select `v_nationalobjectives_all_level_helper`.`amp_activity_id` AS `amp_activity_id`,
         (case 8
            when `v_nationalobjectives_all_level_helper`.`l1` then
            `v_nationalobjectives_all_level_helper`.`n1`
            when `v_nationalobjectives_all_level_helper`.`l2` then
            `v_nationalobjectives_all_level_helper`.`n2`
            when `v_nationalobjectives_all_level_helper`.`l3` then
            `v_nationalobjectives_all_level_helper`.`n3`
            when `v_nationalobjectives_all_level_helper`.`l4` then
            `v_nationalobjectives_all_level_helper`.`n4`
            when `v_nationalobjectives_all_level_helper`.`l5` then
            `v_nationalobjectives_all_level_helper`.`n5`
            when `v_nationalobjectives_all_level_helper`.`l6` then
            `v_nationalobjectives_all_level_helper`.`n6`
            when `v_nationalobjectives_all_level_helper`.`l7` then
            `v_nationalobjectives_all_level_helper`.`n7`
            when `v_nationalobjectives_all_level_helper`.`l8` then
            `v_nationalobjectives_all_level_helper`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level_helper`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level_helper`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level_helper`
  having (`name` is not null);

update amp_columns set extractorView = 'v_nationalobjectives_level_0' where extractorView = 'v_nationalobjectives';

update amp_columns set aliasName = 'nationalojectives_0' where extractorView = 'v_nationalobjectives_level_0';


INSERT INTO `amp_columns`(`columnName`, `aliasName`, `cellType`, `extractorView`, `tokenExpression`, `relatedContentPersisterClass`, `filterRetrievable`)
VALUES ('National Planning Objectives Level 1','nationalojectives_1','org.dgfoundation.amp.ar.cell.MetaTextCell', 'v_nationalobjectives_level_1',NULL,NULL,True),
       ('National Planning Objectives Level 2','nationalojectives_2','org.dgfoundation.amp.ar.cell.MetaTextCell', 'v_nationalobjectives_level_2',NULL,NULL,True),
       ('National Planning Objectives Level 3','nationalojectives_3','org.dgfoundation.amp.ar.cell.MetaTextCell', 'v_nationalobjectives_level_3',NULL,NULL,True),
       ('National Planning Objectives Level 4','nationalojectives_4','org.dgfoundation.amp.ar.cell.MetaTextCell', 'v_nationalobjectives_level_4',NULL,NULL,True),
       ('National Planning Objectives Level 5','nationalojectives_5','org.dgfoundation.amp.ar.cell.MetaTextCell', 'v_nationalobjectives_level_5',NULL,NULL,True),
       ('National Planning Objectives Level 5','nationalojectives_6','org.dgfoundation.amp.ar.cell.MetaTextCell', 'v_nationalobjectives_level_6',NULL,NULL,True),
       ('National Planning Objectives Level 7','nationalojectives_7','org.dgfoundation.amp.ar.cell.MetaTextCell', 'v_nationalobjectives_level_7',NULL,NULL,True),
       ('National Planning Objectives Level 8','nationalojectives_8','org.dgfoundation.amp.ar.cell.MetaTextCell', 'v_nationalobjectives_level_8',NULL,NULL,True);