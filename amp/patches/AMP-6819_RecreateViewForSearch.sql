DROP VIEW IF EXISTS `v_description`;

CREATE VIEW `v_description` AS 
  select 
    distinct `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
    trim(`dg_editor`.`BODY`) AS `ebody` 
  from 
    (`amp_activity` join `dg_editor`) 
  where 
    (convert(`amp_activity`.`description` using utf8) = convert(`dg_editor`.`EDITOR_KEY` using utf8)) 
  order by 
    `amp_activity`.`amp_activity_id`;