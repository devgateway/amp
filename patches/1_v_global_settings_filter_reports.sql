START TRANSACTION;
SET AUTOCOMMIT = 0;
CREATE OR REPLACE VIEW `v_global_settings_filter_reports` AS select `util_global_settings_possible_values`.`value_id` AS `id`,`util_global_settings_possible_values`.`value_shown` AS `value` from `util_global_settings_possible_values` where (`util_global_settings_possible_values`.`setting_name` = 'Filter reports by month');
INSERT INTO amp_global_settings(settingsName,settingsValue,possibleValues) values('Filter reports by month','On','v_global_settings_filter_reports');
INSERT INTO util_global_settings_possible_values(setting_name,value_id,value_shown) VALUES('Filter reports by month','On','On');
INSERT INTO util_global_settings_possible_values(setting_name,value_id,value_shown) VALUES('Filter reports by month','Off','Off');
COMMIT;