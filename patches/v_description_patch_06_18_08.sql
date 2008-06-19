CREATE OR REPLACE VIEW `v_description` AS 
select distinct `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
trim(`dg_editor`.`BODY`) AS `trim(dg_editor.body)` 
from (`amp_activity` join `dg_editor`) 
where (`amp_activity`.`description` = `dg_editor`.`EDITOR_KEY`) 
order by `amp_activity`.`amp_activity_id`