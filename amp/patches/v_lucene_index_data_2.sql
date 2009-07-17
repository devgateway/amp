ALTER TABLE `amp_activity` MODIFY COLUMN `description` VARCHAR(255) ,
 MODIFY COLUMN `objectives` VARCHAR(255) ,
 MODIFY COLUMN `version` VARCHAR(255) ,
 MODIFY COLUMN `results` VARCHAR(255) ,
 MODIFY COLUMN `purpose` VARCHAR(255) ;


ALTER TABLE `amp_activity` ADD INDEX `amp_activity_description_idx`(`description`),
 ADD INDEX `amp_activity_objectives_idx`(`objectives`),
 ADD INDEX `amp_activity_purpose_idx`(`purpose`),
 ADD INDEX `amp_activity_results_idx`(`results`);



CREATE OR REPLACE VIEW  `v_description` AS select distinct(`amp_activity`.`amp_activity_id`) AS `amp_activity_id`,trim(`dg_editor`.`BODY`) AS `trim(dg_editor.body)` from (`amp_activity` join `dg_editor`) where (`amp_activity`.`description` = convert(`dg_editor`.`EDITOR_KEY` using utf8)) order by `amp_activity`.`amp_activity_id`;

DROP VIEW  `v_lucene_index_data`;

CREATE VIEW `v_purposes` AS select `amp_activity`.`amp_activity_id` AS `amp_activity_id`,trim(`dg_editor`.`BODY`) AS `trim(dg_editor.body)` from (`amp_activity` join `dg_editor`) where (`amp_activity`.`purpose` = convert(`dg_editor`.`EDITOR_KEY` using utf8)) order by `amp_activity`.`amp_activity_id`;

CREATE VIEW `v_results` AS select `amp_activity`.`amp_activity_id` AS `amp_activity_id`,trim(`dg_editor`.`BODY`) AS `trim(dg_editor.body)` from (`amp_activity` join `dg_editor`) where (`amp_activity`.`results` = convert(`dg_editor`.`EDITOR_KEY` using utf8)) order by `amp_activity`.`amp_activity_id`;

