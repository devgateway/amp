CREATE OR REPLACE VIEW `v_g_settings_sh_comp_fund_year` AS select `util_global_settings_possible_`.`value_id` AS `id`, `util_global_settings_possible_`.`value_shown` AS `value` from `util_global_settings_possible_` where (`util_global_settings_possible_`.`setting_name` = 'Show Component Funding by Year');

INSERT INTO `amp_global_settings` (`settingsName`, `settingsValue`, `possibleValues`, `description`, `section`) VALUES ('Show Component Funding by Year', 'Off', 'v_g_settings_sh_comp_fund_year', 'View component funding data sorted by year', 'user');

INSERT INTO `util_global_settings_possible_` (`setting_name`, `value_id`, `value_shown`) VALUES ('Show Component Funding by Year', 'On', 'On');
  
INSERT INTO `util_global_settings_possible_` (`setting_name`, `value_id`, `value_shown`) VALUES ('Show Component Funding by Year', 'Off', 'Off');

