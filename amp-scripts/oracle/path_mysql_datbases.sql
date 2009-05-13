--Run it using -f parameter
alter table amp_activity_referencedoc change column comment comment_ text;
alter table amp_calendar_event_organisations rename to amp_calendar_event_organisation;
alter table amp_comments change column comment comment_ text;
alter table amp_funding drop foreign key  FK36860F4228AD2CDC;
alter table amp_funding drop foreign key FK36860F42C7C1FE6;
alter table amp_funding change column  type_of_assistance_category_value_id type_of_assistance_category_va bigint(20);
alter table amp_funding  add  constraint FK36860F4228AD2CDC foreign key (type_of_assistance_category_va) references amp_category_value (id);
alter table amp_funding drop foreign key  FK36860F42C263880F;
alter table amp_funding drop foreign key  FK36860F42DE949505;
alter table amp_funding change column  financing_instr_category_value_id   financing_instr_category_value bigint(20);
alter table amp_funding  add  constraint FK36860F42C263880F foreign key (financing_instr_category_value) references amp_category_value (id);
alter table amp_indicator_values change column comment comment_ text;
alter table amp_message_settings change column  days_for_advance_alerts_warnings   days_for_advance_alerts_warnin bigint(20);
alter table amp_pledges change column  date   date_ datetime;
alter table amp_report_physical_performance rename to amp_report_physical_performanc;
alter table amp_team_member drop foreign key FKE40054A136EBCB;
alter table amp_team_member drop foreign key FKE40054A1F0C1E583;
alter table amp_team_member change column user user_ bigint(20);
alter table amp_team_member add  constraint FKE40054A136EBCB foreign key (user_) references dg_user (id);
alter table amp_theme change column level level_ int(11);
alter table dg_cms_content_item change column date date_ varchar(255);
alter table ipa_contract change column total_private_contrib_amount_date tot_private_contr_date datetime;
alter table ipa_contract change column total_national_contrib_regional_amount_date  tot_national_contr_reg_date datetime;
alter table ipa_contract change column total_national_contrib_ifi_amount_date tot_national_contr_ifi_date datetime;
alter table ipa_contract change column total_national_contrib_central_amount_date  tot_national_contr_cent_date datetime;
alter table ipa_contract change column total_ec_contrib_inv_amount_date  tot_ec_contr_inv_date datetime;
alter table ipa_contract change column total_ec_contrib_ib_amount_date  tot_ec_contr_ib_date datetime;
alter table ipa_contract change column total_national_contrib_central_amount  tot_national_contr_central double;
alter table ipa_contract change column total_national_contrib_regional_amount  tot_national_contr_regional double;
alter table ipa_contract change column total_national_contrib_ifi_amount  tot_national_contrib_ifi double;
alter table ipa_contract_disbursement change column   date   date_ datetime;
alter table amp_ipa_contracting_organizations rename to  amp_ipa_contracting_organizati ;
alter table amp_eu_activity_contributions drop foreign key FK39D506A7DE949505;
alter table amp_eu_activity_contributions drop foreign key FK39D506A7C263880F;
alter table amp_eu_activity_contributions change column financing_instr_category_value_id financing_instr_category_value bigint(20);
alter table amp_eu_activity_contributions add constraint FK39D506A7C263880F foreign key (financing_instr_category_value) references amp_category_value (id);
alter table util_global_settings_possible_values rename to util_global_settings_possible_;
alter table v_global_settings_countries rename to v_g_settings_countries;
alter table v_global_settings_current_fiscal_year rename to v_g_settings_curr_fiscal_year;
alter table v_global_settings_default_calendar rename to v_g_settings_def_comp_type;
alter table v_global_settings_default_component_type rename to v_g_settings_default_calendar;
alter table v_global_settings_feature_templates rename to v_g_settings_feature_templates;
alter table v_global_settings_filter_reports rename to v_g_settings_filter_reports;
alter table v_global_settings_public_view rename to v_g_settings_public_view;
alter table v_global_settings_pv_budget_filter rename to v_g_settings_pv_budget_filter;
alter table v_global_settings_sector_schemes rename to v_g_settings_sector_schemes;
alter table v_global_settings_showcomponentfundingbyyear rename to v_g_settings_sh_comp_fund_year;
alter table v_global_settings_templates_visibility rename to v_g_settings_templ_visibility;
alter table v_nationalobjectives_all_level_helper rename to v_nationalobjectives_all_level;
alter table v_responsible_organisation_groups rename to v_responsible_org_groups;


CREATE OR REPLACE VIEW `v_activity_changed_by` AS
  select
    `a`.`amp_activity_id` AS `amp_activity_id`,
    `u`.`EMAIL` AS `name`,
    `atm`.`user_` AS `user_id`
  from
    ((`amp_activity` `a` join `amp_team_member` `atm`) join `dg_user` `u`)
  where
    ((`atm`.`amp_team_mem_id` = `a`.`updated_by`) and (`atm`.`user_` = `u`.`ID`))
  order by
    `a`.`amp_activity_id`;

CREATE OR REPLACE VIEW
 `v_activity_creator` AS
  select `a`.`amp_activity_id` AS `amp_activity_id`,
         concat(`u`.`FIRST_NAMES`, _latin1'
         ',`u`.`LAST_NAME`) AS `name`,`atm`.`user_` AS `user_id` from ((`amp_activity` `a` join `amp_team_member` `atm`) join `dg_user` `u`) where ((`atm`.`amp_team_mem_id` = `a`.`activity_creator`) and (`atm`.`user_` = `u`.`ID`)) order by `a`.`amp_activity_id`;

         CREATE or REPLACE VIEW
 `v_contribution_funding` AS
  select `eu`.`amp_activity_id` AS `amp_activity_id`,
         `eu`.`id` AS `amp_funding_id`,
         `euc`.`id` AS `amp_funding_detail_id`,
         `o`.`name` AS `donor_name`,
         `euc`.`amount` AS `transaction_amount`,
         `euc`.`transaction_date` AS `transaction_date`,
         `c`.`currency_code` AS `currency_code`,
         `acv_term`.`category_value` AS `terms_assist_name`,
         `acv_mod`.`category_value` AS `financing_instrument_name`,
         `o`.`amp_org_id` AS `amp_org_id`,
         `o`.`org_grp_id` AS `org_grp_id`,
         `acv_term`.`id` AS `terms_assist_id`
  from (((((`amp_eu_activity` `eu`
       join `amp_eu_activity_contributions` `euc`)
       join `amp_currency` `c`)
       join `amp_category_value` `acv_term`)
       join `amp_category_value` `acv_mod`)
       join `amp_organisation` `o`)
  where ((`eu`.`id` = `euc`.`eu_activity_id`) and
        (`euc`.`amount_currency` = `c`.`amp_currency_id`) and
        (`acv_term`.`id` = `euc`.`financing_type_categ_val_id`) and
        (`acv_mod`.`id` = `euc`.`financing_instr_category_value`) and
        (`o`.`amp_org_id` = `euc`.`donor_id`))
  order by `eu`.`amp_activity_id`;

CREATE OR REPLACE VIEW `v_date_formats` AS
  select `util_global_settings_possible_`.`value_id` AS `id`,
         `util_global_settings_possible_`.`value_shown` AS `value`
  from `util_global_settings_possible_`
  where (`util_global_settings_possible_`.`setting_name` = _latin1'Default Date Format');


  CREATE or REPLACE VIEW `v_default_number_format` AS
  select
    `util_global_settings_possible_`.`value_id` AS `id`,
    `util_global_settings_possible_`.`value_shown` AS `value`
  from
    `util_global_settings_possible_`
  where
    (`util_global_settings_possible_`.`setting_name` = _latin1'Default Number Format');

    CREATE OR REPLACE VIEW `v_donor_funding` AS
  select
    `f`.`amp_activity_id` AS `amp_activity_id`,
    `f`.`amp_funding_id` AS `amp_funding_id`,
    `fd`.`amp_fund_detail_id` AS `amp_fund_detail_id`,
    `d`.`name` AS `donor_name`,
    `fd`.`transaction_type` AS `transaction_type`,
    `fd`.`adjustment_type` AS `adjustment_type`,
    `fd`.`transaction_date` AS `transaction_date`,
    `fd`.`transaction_amount` AS `transaction_amount`,
    `c`.`currency_code` AS `currency_code`,
    `cval`.`category_value` AS `terms_assist_name`,
    `fd`.`fixed_exchange_rate` AS `fixed_exchange_rate`,
    `b`.`org_grp_name` AS `org_grp_name`,
    `ot`.`org_type` AS `donor_type_name`,
    `cval2`.`category_value` AS `financing_instrument_name`,
    `b`.`amp_org_grp_id` AS `org_grp_id`,
    `ot`.`amp_org_type_id` AS `org_type_id`,
    `cval2`.`id` AS `financing_instrument_id`,
    `cval`.`id` AS `terms_assist_id`
  from
    (((((((`amp_funding` `f` join `amp_funding_detail` `fd`) join `amp_category_value` `cval`) join `amp_currency` `c`) join `amp_organisation` `d`) join `amp_org_group` `b`) join `amp_org_type` `ot`) join `amp_category_value` `cval2`)
  where
    ((`cval2`.`id` = `f`.`financing_instr_category_value`) and (`c`.`amp_currency_id` = `fd`.`amp_currency_id`) and (`f`.`amp_funding_id` = `fd`.`amp_funding_id`) and (`cval`.`id` = `f`.`type_of_assistance_category_va`) and (`d`.`amp_org_id` = `f`.`amp_donor_org_id`) and (`d`.`org_grp_id` = `b`.`amp_org_grp_id`) and (`ot`.`amp_org_type_id` = `d`.`org_type_id`))
  order by
    `f`.`amp_activity_id`;


CREATE OR REPLACE  VIEW `v_financing_instrument` AS (
  select `f`.`amp_activity_id` AS `amp_activity_id`,
         `val`.`category_value` AS `name`,
         `f`.`financing_instr_category_value` AS `amp_modality_id`
  from (`amp_funding` `f`
       join `amp_category_value` `val`)
  where (`f`.`financing_instr_category_value` = `val`.`id`)) union (
  select `eu`.`amp_activity_id` AS `amp_activity_id`,
         `val`.`category_value` AS `category_value`,
         `eu_con`.`financing_instr_category_value` AS `financing_instr_category_value`
  from ((`amp_eu_activity` `eu`
       join `amp_eu_activity_contributions` `eu_con`)
       join `amp_category_value` `val`)
  where ((`eu_con`.`eu_activity_id` = `eu`.`id`) and
        (`eu_con`.`financing_instr_category_value` = `val`.`id`)))
  order by
    `amp_activity_id`,`name`;

    CREATE OR REPLACE VIEW `v_g_settings_countries` AS
  select
    `dg_countries`.`ISO` AS `id`,
    `dg_countries`.`COUNTRY_NAME` AS `value`
  from
    `dg_countries`
  order by
    `dg_countries`.`COUNTRY_NAME`;


    CREATE OR REPLACE VIEW
 `v_g_settings_curr_fiscal_year` AS
  select `util_global_settings_possible_`.`value_id` AS `id`,
         `util_global_settings_possible_`.`value_shown` AS `value`
  from `util_global_settings_possible_`
  where (`util_global_settings_possible_`.`setting_name` = _latin1'Current Fiscal Year');


  CREATE OR REPLACE VIEW `v_g_settings_filter_reports` AS
  select
    `util_global_settings_possible_`.`value_id` AS `id`,
    `util_global_settings_possible_`.`value_shown` AS `value`
  from
    `util_global_settings_possible_`
  where
    (`util_global_settings_possible_`.`setting_name` = _latin1'Filter reports by month');
    
    CREATE OR REPLACE VIEW
 `v_g_settings_public_view` AS
  select `util_global_settings_possible_`.`value_id` AS `id`,
         `util_global_settings_possible_`.`value_shown` AS `value`
  from `util_global_settings_possible_`
  where (`util_global_settings_possible_`.`setting_name` = _latin1'Public View');
  
  create or replace view  `v_g_settings_pv_budget_filter` AS
  select
    `util_global_settings_possible_`.`value_id` AS `id`,
    `util_global_settings_possible_`.`value_shown` AS `value`
  from
    `util_global_settings_possible_`
  where
    (`util_global_settings_possible_`.`setting_name` = _latin1'Public View Budget Filter');
  
  
    
CREATE  OR REPLACE VIEW `v_g_settings_sh_comp_fund_year` AS
  select `util_global_settings_possible_`.`value_id` AS `id`,`util_global_settings_possible_`.`value_shown` AS `value`
  from   `util_global_settings_possible_`
  where   (`util_global_settings_possible_`.`setting_name` = _latin1'Show Component Funding by Year');
  
  
  CREATE OR REPLACE  VIEW
 `v_nationalobjectives` AS
  select `a`.`amp_activity_id` AS `amp_activity_id`,
         `t`.`name` AS `name`,
         `ap`.`amp_program_id` AS `amp_program_id`,
         `ap`.`program_percentage` AS `program_percentage`
  from ((`amp_activity` `a`
       join `amp_activity_program` `ap` on (((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (
       `ap`.`program_setting` = 1))))
       join `amp_theme` `t` on ((`t`.`amp_theme_id` = `ap`.`amp_program_id`)))
  where ((`t`.`parent_theme_id` is not null) and
        (`t`.`level_` = 2))
  order by `a`.`amp_activity_id`,
           `t`.`name`;
           
           
CREATE OR REPLACE VIEW
 `v_nationalobjectives_all_level` AS
  select `a`.`amp_activity_id` AS `amp_activity_id`,
         `a`.`program_percentage` AS `program_percentage`,
         `a`.`amp_program_id` AS `amp_program_id`,
         `b`.`name` AS `n1`,
         `b`.`level_` AS `l1`,
         `b1`.`name` AS `n2`,
         `b1`.`level_` AS `l2`,
         `b2`.`name` AS `n3`,
         `b2`.`level_` AS `l3`,
         `b3`.`name` AS `n4`,
         `b3`.`level_` AS `l4`,
         `b4`.`name` AS `n5`,
         `b4`.`level_` AS `l5`,
         `b5`.`name` AS `n6`,
         `b5`.`level_` AS `l6`,
         `b6`.`name` AS `n7`,
         `b6`.`level_` AS `l7`,
         `b7`.`name` AS `n8`,
         `b7`.`level_` AS `l8`
  from ((((((((`amp_activity_program` `a`
       join `amp_theme` `b` on ((`a`.`amp_program_id` = `b`.`amp_theme_id`)))
       left join `amp_theme` `b1` on ((`b1`.`amp_theme_id` = `b`.`parent_theme_id`)))
       left join `amp_theme` `b2` on ((`b2`.`amp_theme_id` = `b1`.`parent_theme_id`)))
       left join `amp_theme` `b3` on ((`b3`.`amp_theme_id` = `b2`.`parent_theme_id`)))
       left join `amp_theme` `b4` on ((`b4`.`amp_theme_id` = `b3`.`parent_theme_id`)))
       left join `amp_theme` `b5` on ((`b5`.`amp_theme_id` = `b4`.`parent_theme_id`)))
       left join `amp_theme` `b6` on ((`b6`.`amp_theme_id` = `b5`.`parent_theme_id`)))
       left join `amp_theme` `b7` on ((`b7`.`amp_theme_id` = `b6`.`parent_theme_id`)));
       
       
       
       CREATE OR REPLACE VIEW
 `v_nationalobjectives_level_0` AS
  select `v_nationalobjectives_all_level`.`amp_activity_id` AS `amp_activity_id`,
         (case 0
            when `v_nationalobjectives_all_level`.`l1` then
            `v_nationalobjectives_all_level`.`n1`
            when `v_nationalobjectives_all_level`.`l2` then
            `v_nationalobjectives_all_level`.`n2`
            when `v_nationalobjectives_all_level`.`l3` then
            `v_nationalobjectives_all_level`.`n3`
            when `v_nationalobjectives_all_level`.`l4` then
            `v_nationalobjectives_all_level`.`n4`
            when `v_nationalobjectives_all_level`.`l5` then
            `v_nationalobjectives_all_level`.`n5`
            when `v_nationalobjectives_all_level`.`l6` then
            `v_nationalobjectives_all_level`.`n6`
            when `v_nationalobjectives_all_level`.`l7` then
            `v_nationalobjectives_all_level`.`n7`
            when `v_nationalobjectives_all_level`.`l8` then
            `v_nationalobjectives_all_level`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level`
  having (`name` is not null);
  CREATE OR REPLACE  VIEW
 `v_nationalobjectives_level_1` AS
  select `v_nationalobjectives_all_level`.`amp_activity_id` AS `amp_activity_id`,
         (case 1
            when `v_nationalobjectives_all_level`.`l1` then `v_nationalobjectives_all_level`.`n1`
            when `v_nationalobjectives_all_level`.`l2` then `v_nationalobjectives_all_level`.`n2`
            when `v_nationalobjectives_all_level`.`l3` then `v_nationalobjectives_all_level`.`n3`
            when `v_nationalobjectives_all_level`.`l4` then `v_nationalobjectives_all_level`.`n4`
            when `v_nationalobjectives_all_level`.`l5` then `v_nationalobjectives_all_level`.`n5`
            when `v_nationalobjectives_all_level`.`l6` then `v_nationalobjectives_all_level`.`n6`
            when `v_nationalobjectives_all_level`.`l7` then `v_nationalobjectives_all_level`.`n7`
            when `v_nationalobjectives_all_level`.`l8` then `v_nationalobjectives_all_level`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level`
  having (`name` is not null);

    CREATE OR REPLACE  VIEW
    `v_nationalobjectives_level_2` AS
  select `v_nationalobjectives_all_level`.`amp_activity_id` AS `amp_activity_id`,
         (case 2
            when `v_nationalobjectives_all_level`.`l1` then `v_nationalobjectives_all_level`.`n1`
            when `v_nationalobjectives_all_level`.`l2` then `v_nationalobjectives_all_level`.`n2`
            when `v_nationalobjectives_all_level`.`l3` then `v_nationalobjectives_all_level`.`n3`
            when `v_nationalobjectives_all_level`.`l4` then `v_nationalobjectives_all_level`.`n4`
            when `v_nationalobjectives_all_level`.`l5` then `v_nationalobjectives_all_level`.`n5`
            when `v_nationalobjectives_all_level`.`l6` then `v_nationalobjectives_all_level`.`n6`
            when `v_nationalobjectives_all_level`.`l7` then `v_nationalobjectives_all_level`.`n7`
            when `v_nationalobjectives_all_level`.`l8` then `v_nationalobjectives_all_level`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level`
  having (`name` is not null);

    CREATE  OR REPLACE  VIEW
    `v_nationalobjectives_level_3` AS
  select `v_nationalobjectives_all_level`.`amp_activity_id` AS `amp_activity_id`,
         (case 3
            when `v_nationalobjectives_all_level`.`l1` then `v_nationalobjectives_all_level`.`n1`
            when `v_nationalobjectives_all_level`.`l2` then `v_nationalobjectives_all_level`.`n2`
            when `v_nationalobjectives_all_level`.`l3` then `v_nationalobjectives_all_level`.`n3`
            when `v_nationalobjectives_all_level`.`l4` then `v_nationalobjectives_all_level`.`n4`
            when `v_nationalobjectives_all_level`.`l5` then `v_nationalobjectives_all_level`.`n5`
            when `v_nationalobjectives_all_level`.`l6` then `v_nationalobjectives_all_level`.`n6`
            when `v_nationalobjectives_all_level`.`l7` then `v_nationalobjectives_all_level`.`n7`
            when `v_nationalobjectives_all_level`.`l8` then `v_nationalobjectives_all_level`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level`
  having (`name` is not null);

    CREATE OR REPLACE  VIEW
    `v_nationalobjectives_level_4` AS
  select `v_nationalobjectives_all_level`.`amp_activity_id` AS `amp_activity_id`,
         (case 4
            when `v_nationalobjectives_all_level`.`l1` then `v_nationalobjectives_all_level`.`n1`
            when `v_nationalobjectives_all_level`.`l2` then `v_nationalobjectives_all_level`.`n2`
            when `v_nationalobjectives_all_level`.`l3` then `v_nationalobjectives_all_level`.`n3`
            when `v_nationalobjectives_all_level`.`l4` then `v_nationalobjectives_all_level`.`n4`
            when `v_nationalobjectives_all_level`.`l5` then `v_nationalobjectives_all_level`.`n5`
            when `v_nationalobjectives_all_level`.`l6` then `v_nationalobjectives_all_level`.`n6`
            when `v_nationalobjectives_all_level`.`l7` then `v_nationalobjectives_all_level`.`n7`
            when `v_nationalobjectives_all_level`.`l8` then `v_nationalobjectives_all_level`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level`
  having (`name` is not null);

    CREATE OR REPLACE  VIEW
    `v_nationalobjectives_level_5` AS
  select `v_nationalobjectives_all_level`.`amp_activity_id` AS `amp_activity_id`,
         (case 5
            when `v_nationalobjectives_all_level`.`l1` then `v_nationalobjectives_all_level`.`n1`
            when `v_nationalobjectives_all_level`.`l2` then `v_nationalobjectives_all_level`.`n2`
            when `v_nationalobjectives_all_level`.`l3` then `v_nationalobjectives_all_level`.`n3`
            when `v_nationalobjectives_all_level`.`l4` then `v_nationalobjectives_all_level`.`n4`
            when `v_nationalobjectives_all_level`.`l5` then `v_nationalobjectives_all_level`.`n5`
            when `v_nationalobjectives_all_level`.`l6` then `v_nationalobjectives_all_level`.`n6`
            when `v_nationalobjectives_all_level`.`l7` then `v_nationalobjectives_all_level`.`n7`
            when `v_nationalobjectives_all_level`.`l8` then `v_nationalobjectives_all_level`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level`
  having (`name` is not null);

    CREATE OR REPLACE  VIEW
    `v_nationalobjectives_level_6` AS
  select `v_nationalobjectives_all_level`.`amp_activity_id` AS `amp_activity_id`,
         (case 6
            when `v_nationalobjectives_all_level`.`l1` then `v_nationalobjectives_all_level`.`n1`
            when `v_nationalobjectives_all_level`.`l2` then `v_nationalobjectives_all_level`.`n2`
            when `v_nationalobjectives_all_level`.`l3` then `v_nationalobjectives_all_level`.`n3`
            when `v_nationalobjectives_all_level`.`l4` then `v_nationalobjectives_all_level`.`n4`
            when `v_nationalobjectives_all_level`.`l5` then `v_nationalobjectives_all_level`.`n5`
            when `v_nationalobjectives_all_level`.`l6` then `v_nationalobjectives_all_level`.`n6`
            when `v_nationalobjectives_all_level`.`l7` then `v_nationalobjectives_all_level`.`n7`
            when `v_nationalobjectives_all_level`.`l8` then `v_nationalobjectives_all_level`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level`
  having (`name` is not null);
    CREATE OR REPLACE  VIEW
    `v_nationalobjectives_level_7` AS
  select `v_nationalobjectives_all_level`.`amp_activity_id` AS `amp_activity_id`,
         (case 7
            when `v_nationalobjectives_all_level`.`l1` then `v_nationalobjectives_all_level`.`n1`
            when `v_nationalobjectives_all_level`.`l2` then `v_nationalobjectives_all_level`.`n2`
            when `v_nationalobjectives_all_level`.`l3` then `v_nationalobjectives_all_level`.`n3`
            when `v_nationalobjectives_all_level`.`l4` then `v_nationalobjectives_all_level`.`n4`
            when `v_nationalobjectives_all_level`.`l5` then `v_nationalobjectives_all_level`.`n5`
            when `v_nationalobjectives_all_level`.`l6` then `v_nationalobjectives_all_level`.`n6`
            when `v_nationalobjectives_all_level`.`l7` then `v_nationalobjectives_all_level`.`n7`
            when `v_nationalobjectives_all_level`.`l8` then `v_nationalobjectives_all_level`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level`
  having (`name` is not null);

    CREATE OR REPLACE  VIEW
    `v_nationalobjectives_level_8` AS
  select `v_nationalobjectives_all_level`.`amp_activity_id` AS `amp_activity_id`,
         (case 8
            when `v_nationalobjectives_all_level`.`l1` then `v_nationalobjectives_all_level`.`n1`
            when `v_nationalobjectives_all_level`.`l2` then `v_nationalobjectives_all_level`.`n2`
            when `v_nationalobjectives_all_level`.`l3` then `v_nationalobjectives_all_level`.`n3`
            when `v_nationalobjectives_all_level`.`l4` then `v_nationalobjectives_all_level`.`n4`
            when `v_nationalobjectives_all_level`.`l5` then `v_nationalobjectives_all_level`.`n5`
            when `v_nationalobjectives_all_level`.`l6` then `v_nationalobjectives_all_level`.`n6`
            when `v_nationalobjectives_all_level`.`l7` then `v_nationalobjectives_all_level`.`n7`
            when `v_nationalobjectives_all_level`.`l8` then `v_nationalobjectives_all_level`.`n8`
          end) AS `name`,
         `v_nationalobjectives_all_level`.`amp_program_id` AS `amp_program_id`,
         `v_nationalobjectives_all_level`.`program_percentage` AS `program_percentage`
  from `v_nationalobjectives_all_level`
  having (`name` is not null);
  
  
  
  
