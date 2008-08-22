CREATE OR REPLACE VIEW `v_global_settings_showcomponentfundingbyyear` AS select `util_global_settings_possible_values`.`value_id` AS `id`, `util_global_settings_possible_values`.`value_shown` AS `value` from `util_global_settings_possible_values` where (`util_global_settings_possible_values`.`setting_name` = 'Show Component Funding by Year');

INSERT INTO `amp_global_settings` (`settingsName`, `settingsValue`, `possibleValues`, `description`, `section`) VALUES ('Show Component Funding by Year', 'Off', 'v_global_settings_showComponentFundingByYear', 'View component funding data sorted by year', 'user');

INSERT INTO `util_global_settings_possible_values` (`setting_name`, `value_id`, `value_shown`) VALUES ('Show Component Funding by Year', 'On', 'On');
  
INSERT INTO `util_global_settings_possible_values` (`setting_name`, `value_id`, `value_shown`) VALUES ('Show Component Funding by Year', 'Off', 'Off');

