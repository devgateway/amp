CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` 
SQL SECURITY DEFINER VIEW `v_results` AS 
select `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
trim(`dg_editor`.`BODY`) AS `trim(dg_editor.body)` 
from (`amp_activity` join `dg_editor`) 
where (`amp_activity`.`results` = `dg_editor`.`EDITOR_KEY`) 
order by `amp_activity`.`amp_activity_id`;