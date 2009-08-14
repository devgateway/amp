update `amp_global_settings`  set possibleValues='v_g_settings_countries' where possibleValues='v_global_settings_countries';
update `amp_global_settings`  set possibleValues='v_g_settings_curr_fiscal_year' where possibleValues='v_global_settings_current_fiscal_year';
update `amp_global_settings`  set possibleValues='v_g_settings_default_calendar' where possibleValues='v_global_settings_default_calendar';
update `amp_global_settings`  set possibleValues='v_g_settings_def_comp_type' where possibleValues='v_global_settings_default_component_type';
update `amp_global_settings`  set possibleValues='v_g_settings_feature_templates' where possibleValues='v_global_settings_feature_templates';
update `amp_global_settings`  set possibleValues='v_g_settings_filter_reports' where possibleValues='v_global_settings_filter_reports';
update `amp_global_settings`  set possibleValues='v_g_settings_public_view' where possibleValues='v_global_settings_public_view';
update `amp_global_settings`  set possibleValues='v_g_settings_pv_budget_filter' where possibleValues='v_global_settings_pv_budget_filter';
update `amp_global_settings`  set possibleValues='v_g_settings_sector_schemes' where possibleValues='v_global_settings_sector_schemes';
update `amp_global_settings`  set possibleValues='v_g_settings_sh_comp_fund_year' where possibleValues='v_global_settings_showcomponentfundingbyyear';
update `amp_global_settings`  set possibleValues='v_g_settings_templ_visibility' where possibleValues='v_global_settings_templates_visibility';
CREATE OR REPLACE  VIEW `v_g_settings_default_calendar` AS
  select `amp_fiscal_calendar`.`amp_fiscal_cal_id` AS `id`,
         `amp_fiscal_calendar`.`name` AS `value`
  from `amp_fiscal_calendar`;


    CREATE OR REPLACE  VIEW `v_g_settings_def_comp_type` AS
  select `amp_component_type`.`type_id` AS `id`,
         `amp_component_type`.`name` AS `value`
  from `amp_component_type`
  where (`amp_component_type`.`enable` = 1);