CREATE OR REPLACE VIEW `v_financing_instrument` AS 
select `f`.`amp_activity_id` AS `amp_activity_id`,
`m`.`name` AS `name`,`f`.`amp_modality_id` AS `amp_modality_id` from 
(`amp_funding` `f` join `amp_modality` `m` on((`f`.`amp_modality_id` = `m`.`amp_modality_id`))) 
order by `f`.`amp_activity_id`,`m`.`name`;