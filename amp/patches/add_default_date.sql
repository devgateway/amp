CREATE OR REPLACE VIEW `v_date_formats` AS
  select
    `util_global_settings_possible_values`.`value_id` AS `id`,
    `util_global_settings_possible_values`.`value_shown` AS `value`
  from
    `util_global_settings_possible_values`
  where
    (`util_global_settings_possible_values`.`setting_name` = _latin1'Default Date Format');

/* This statement avoid overwriting previous configurations */
INSERT INTO `amp_global_settings`
(`settingsName`, `settingsValue`, `possibleValues`, `description`)
SELECT DISTINCT 'Default Date Format', 'MM/dd/yyyy', 'v_date_formats', ''
FROM DUAL 
WHERE not exists
(SELECT * from `amp_global_settings` where `settingsName` = 'Default Date Format');

DELETE FROM `util_global_settings_possible_values` WHERE `setting_name` = 'Default Date Format';

INSERT INTO `util_global_settings_possible_values` (`setting_name`, `value_id`, `value_shown`) VALUES
	  ('Default Date Format', 'dd/MMM/yyyy', 'dd/MMM/yyyy'),
	  ('Default Date Format', 'MMM/dd/yyyy', 'MMM/dd/yyyy'),
	  ('Default Date Format', 'dd/MM/yyyy', 'dd/MM/yyyy'),
	  ('Default Date Format', 'MM/dd/yyyy', 'MM/dd/yyyy');
