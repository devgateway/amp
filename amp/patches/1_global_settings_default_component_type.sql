DROP VIEW IF EXISTS `v_global_settings_default_component_type`; 

CREATE OR REPLACE VIEW `v_global_settings_default_component_type` AS 
SELECT `type_id` as `id`, `name` as `value` FROM `amp_component_type` 
where `enable` = 1; 

delete from `amp_global_settings` where `settingsValue` = 'Default Component Type'; 

insert into `amp_global_settings`(settingsName, settingsValue, possibleValues, description) values ('Default Component Type', 1, 'v_global_settings_default_component_type', 'Component Type by default when Component Type is disabled in FM');