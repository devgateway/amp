CREATE OR REPLACE VIEW `v_project_comments` AS 
select distinct `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
trim(`dg_editor`.`BODY`) AS `trim(dg_editor.body)` 
from (`amp_activity` join `dg_editor`) 
where (`amp_activity`.`projectComments` = `dg_editor`.`EDITOR_KEY`) 
order by `amp_activity`.`amp_activity_id`;


insert into amp_columns (columnName,cellType,extractorView) values ('Project Comments', 'org.dgfoundation.amp.ar.cell.TextCell','v_project_comments');