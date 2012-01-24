DROP VIEW IF EXISTS `v_global_settings_current_fiscal_year`;
CREATE OR REPLACE  VIEW `v_global_settings_current_fiscal_year` AS select `util_global_settings_possible_values`.`value_id` AS `id`,`util_global_settings_possible_values`.`value_shown` AS `value` from `util_global_settings_possible_values` where (`util_global_settings_possible_values`.`setting_name` = _latin1'Current Fiscal Year');


insert into amp_global_settings (settingsName, settingsValue, possibleValues, description) values ('Current Fiscal Year', 2007,'v_global_settings_current_fiscal_year','the list of fiscal years');

insert into util_global_settings_possible_values (setting_name, value_id, value_shown) values('Current Fiscal Year', 2005, 2005);
insert into util_global_settings_possible_values (setting_name, value_id, value_shown) values('Current Fiscal Year', 2006, 2006);
insert into util_global_settings_possible_values (setting_name, value_id, value_shown) values('Current Fiscal Year', 2007, 2007);
insert into util_global_settings_possible_values (setting_name, value_id, value_shown) values('Current Fiscal Year', 2008, 2008);
insert into util_global_settings_possible_values (setting_name, value_id, value_shown) values('Current Fiscal Year', 2009, 2009);
insert into util_global_settings_possible_values (setting_name, value_id, value_shown) values('Current Fiscal Year', 2010, 2010);
insert into util_global_settings_possible_values (setting_name, value_id, value_shown) values('Current Fiscal Year', 2011, 2011);

