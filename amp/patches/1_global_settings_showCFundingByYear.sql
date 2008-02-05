CREATE OR REPLACE VIEW `v_global_settings_showComponentFundingByYear` AS select `util_global_settings_possible_values`.`value_id` AS `id`,`util_global_settings_possible_values`.`value_shown` AS `value` from `util_global_settings_possible_values` where (`util_global_settings_possible_values`.`setting_name` = 'Show Component Funding by Year');

DELETE from amp_global_settings where settingsName = 'Show Component Funding by Year';
INSERT INTO amp_global_settings(settingsName,settingsValue,possibleValues) values('Show Component Funding by Year','Off','v_global_settings_showComponentFundingByYear');

DELETE from util_global_settings_possible_values where setting_name = 'Show Component Funding by Year';
INSERT INTO util_global_settings_possible_values(setting_name,value_id,value_shown) VALUES('Show Component Funding by Year','On','On');
INSERT INTO util_global_settings_possible_values(setting_name,value_id,value_shown) VALUES('Show Component Funding by Year','Off','Off');
