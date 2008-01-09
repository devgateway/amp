CREATE OR REPLACE VIEW  `v_titles` AS 
select `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
`amp_activity`.`name` AS `name`, `amp_activity`.`amp_activity_id` AS `title_id`,amp_activity.draft as draft from `amp_activity` 
order by `amp_activity`.`amp_activity_id`;

UPDATE amp_columns SET cellType='org.dgfoundation.amp.ar.cell.MetaTextCell' WHERE columnName='Project Title';