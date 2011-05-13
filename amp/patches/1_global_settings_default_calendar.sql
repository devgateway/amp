insert into amp_global_settings(settingsName, settingsValue, possibleValues, description) values ('Default Calendar', 'Government Fiscal Calendar', 'v_global_settings_default_calendar', 'default fiscal calendar');

DROP VIEW IF EXISTS `v_global_settings_default_calendar`;
CREATE OR REPLACE  VIEW `v_global_settings_default_calendar` AS select `amp_fiscal_calendar`.`amp_fiscal_cal_id` AS `id`,`amp_fiscal_calendar`.`name` AS `value` from `amp_fiscal_calendar`;
