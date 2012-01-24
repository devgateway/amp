START TRANSACTION;
SET AUTOCOMMIT = 0;
CREATE OR REPLACE VIEW `v_global_settings_perspective` AS select `util_global_settings_possible_values`.`value_id` AS `id`,`util_global_settings_possible_values`.`value_shown` AS `value` from `util_global_settings_possible_values` where (`util_global_settings_possible_values`.`setting_name` = 'Perspective');
INSERT INTO amp_global_settings(settingsName,settingsValue,possibleValues) values('Perspective','On','v_global_settings_perspective');
INSERT INTO util_global_settings_possible_values(setting_name,value_id,value_shown) VALUES('Perspective','On','On');
INSERT INTO util_global_settings_possible_values(setting_name,value_id,value_shown) VALUES('Perspective','Off','Off');
COMMIT;