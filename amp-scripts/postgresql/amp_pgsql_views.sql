-- views
CREATE OR REPLACE VIEW  v_ac_chapters AS
select acc.amp_activity_id AS amp_activity_id,acv.category_value AS name
from amp_activities_categoryvalues acc, amp_category_value acv, amp_category_class ac where ((acv.id = acc.amp_categoryvalue_id) and (ac.id = acv.amp_category_class_id) and (ac.keyName = 'acchapter'));


CREATE OR REPLACE VIEW  v_accession_instruments AS
select acc.amp_activity_id AS amp_activity_id,acv.category_value AS name from
amp_activities_categoryvalues acc , amp_category_value acv , amp_category_class ac where ((acv.id = acc.amp_categoryvalue_id) and (ac.id = acv.amp_category_class_id) and (ac.keyName = 'accessioninstr'));


CREATE OR REPLACE VIEW  v_activity_changed_by AS
select a.amp_activity_id AS amp_activity_id,u.EMAIL AS name,atm.user_ AS user_id from
amp_activity a , amp_team_member atm , dg_user u where ((atm.amp_team_mem_id = a.updated_by) and (atm.user_ = u.ID)) order by a.amp_activity_id;


CREATE OR REPLACE VIEW  v_activity_creator AS
select a.amp_activity_id AS amp_activity_id,u.FIRST_NAMES || E'\r\n         ' || u.LAST_NAME AS name,atm.user_ AS user_id from
amp_activity a, amp_team_member atm, dg_user u where ((atm.amp_team_mem_id = a.activity_creator) and (atm.user_ = u.ID)) order by a.amp_activity_id;


CREATE OR REPLACE VIEW  v_actors AS
select ai.amp_activity_id AS amp_activity_id,act.name AS name,act.amp_actor_id AS amp_actor_id from
amp_activity a , amp_measure m, amp_issues ai, amp_actor act where ((ai.amp_activity_id = a.amp_activity_id) and (ai.amp_issue_id = m.amp_issue_id) and (act.amp_measure_id = m.amp_measure_id)) order by ai.amp_activity_id;

CREATE OR REPLACE VIEW v_actual_approval_date AS
select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.actual_approval_date AS actual_approval_date from
amp_activity order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW v_actual_completion_date AS select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.actual_completion_date AS actual_completion_date 
from amp_activity order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_actual_proposed_date AS select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.proposed_approval_date AS proposed_approval_date
from amp_activity order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_actual_start_date AS select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.actual_start_date AS actual_start_date
from amp_activity order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_amp_id AS select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.amp_id AS amp_id from amp_activity;

CREATE OR REPLACE VIEW  v_amp_theme AS select amp_theme.amp_theme_id AS id,amp_theme.name AS value from amp_theme;

CREATE OR REPLACE VIEW  v_ampid AS select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.amp_id AS amp_id from
amp_activity order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_beneficiary_agency AS select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id
from amp_org_role f,amp_organisation o, amp_role r WHERE (f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'BA') order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_beneficiary_agency_groups AS select f.activity AS amp_activity_id,b.org_grp_name AS name,b.amp_org_grp_id AS amp_org_grp_id
from amp_org_role f ,amp_organisation o , amp_role r, amp_org_group b WHERE (f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'BA') and (o.org_grp_id = b.amp_org_grp_id) order by f.activity,o.name;

CREATE OR REPLACE VIEW v_bolivia_component_code AS select a.amp_activity_id AS amp_activity_id,c.code AS code from
amp_activity_components a, amp_components c WHERE (a.amp_component_id = c.amp_component_id) order by a.amp_activity_id;

CREATE OR REPLACE VIEW v_component_description AS select distinct a.amp_activity_id AS amp_activity_id,b.description AS title,b.amp_component_id AS amp_component_id from
amp_activity a , amp_components b, amp_component_funding c where ((a.amp_activity_id = c.activity_id) and (b.amp_component_id = c.amp_component_id));

CREATE OR REPLACE VIEW  v_component_funding AS select f.activity_id AS amp_activity_id,f.amp_component_funding_id AS amp_component_funding_id,f.amp_component_funding_id AS amp_fund_detail_id,c.title AS component_name,f.transaction_type AS transaction_type,f.adjustment_type AS adjustment_type,f.transaction_date AS transaction_date,f.transaction_amount AS transaction_amount,f.currency_id AS currency_id,cu.currency_code AS currency_code
from amp_component_funding f ,amp_components c, amp_currency cu where ((cu.amp_currency_id = f.currency_id) and (f.amp_component_id = c.amp_component_id)) order by f.activity_id;

CREATE OR REPLACE VIEW v_component_type AS select aac.amp_activity_id AS amp_activity_id,ct.name AS component_type,ct.type_id AS component_type_id from
amp_components c, amp_component_type ct, amp_activity_components aac where ((c.type = ct.type_id) and (aac.amp_component_id = c.amp_component_id));

CREATE OR REPLACE VIEW  v_componente AS select sa.amp_activity_id AS amp_activity_id,s.name AS name,sa.amp_sector_id AS amp_sector_id,sa.percentage AS percentage
from amp_activity_componente sa , amp_sector s WHERE ((sa.amp_sector_id = s.amp_sector_id)) order by sa.amp_activity_id,s.name;

CREATE OR REPLACE VIEW  v_components AS select distinct a.amp_activity_id AS amp_activity_id,b.title AS title,b.amp_component_id AS amp_component_id from
amp_activity a ,amp_components b,  amp_component_funding c where ((a.amp_activity_id = c.activity_id) and (b.amp_component_id = c.amp_component_id));

CREATE OR REPLACE VIEW  v_contact_name AS select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.cont_first_name||' '||amp_activity.cont_last_name AS contact,amp_activity.amp_activity_id AS contact_id
from amp_activity where trim(amp_activity.cont_first_name||' '||amp_activity.cont_last_name) <> '' UNION
select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.mofed_cnt_first_name||' '||amp_activity.mofed_cnt_last_name AS contact,amp_activity.amp_activity_id AS contact_id
from amp_activity where (trim(amp_activity.mofed_cnt_first_name||' '||amp_activity.mofed_cnt_last_name)) <> '';

CREATE OR REPLACE VIEW  v_contracting_agency AS select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id
from amp_org_role f , amp_organisation o, amp_role r WHERE (((f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'CA'))) order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_contribution_funding AS select eu.amp_activity_id AS amp_activity_id,eu.id AS amp_funding_id,euc.id AS amp_funding_detail_id,o.name AS donor_name,euc.amount AS transaction_amount,euc.transaction_date AS transaction_date,c.currency_code AS currency_code,acv_term.category_value AS terms_assist_name,acv_mod.category_value AS financing_instrument_name,o.amp_org_id AS amp_org_id,o.org_grp_id AS org_grp_id,acv_term.id AS terms_assist_id 
from amp_eu_activity eu, amp_eu_activity_contributions euc, amp_currency c,amp_category_value acv_term, amp_category_value acv_mod,amp_organisation o
where ((eu.id = euc.eu_activity_id) and (euc.amount_currency = c.amp_currency_id) and (acv_term.id = euc.financing_type_categ_val_id) and (acv_mod.id = euc.FINANCING_INSTR_CATEGORY_VALUE) and (o.amp_org_id = euc.donor_id)) order by eu.amp_activity_id;

CREATE OR REPLACE VIEW  v_convenio_numcont AS select amp_activity.amp_activity_id AS amp_activity_id,
(case amp_activity.convenio_numcont when '-' then NULL when '.' then NULL else amp_activity.convenio_numcont end) AS numcont from amp_activity order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_costing_donors AS select eu.amp_activity_id AS amp_activity_id,o.name AS name,euc.donor_id AS donor_id from
amp_activity a , amp_eu_activity eu, amp_eu_activity_contributions euc,amp_organisation o where ((a.amp_activity_id = eu.amp_activity_id) and (eu.id = euc.eu_activity_id) and (euc.donor_id = o.amp_org_id)) order by o.name;

CREATE OR REPLACE VIEW  v_creation_date AS select a.amp_activity_id AS amp_activity_id,a.date_created AS creation_date from amp_activity a order by a.amp_activity_id;

CREATE OR REPLACE VIEW  v_date_formats AS select util_global_settings_possible_.value_id AS id,util_global_settings_possible_.value_shown AS value from util_global_settings_possible_ where (util_global_settings_possible_.setting_name = 'Default Date Format');

CREATE OR REPLACE VIEW  v_default_number_format AS select util_global_settings_possible_.value_id AS id,util_global_settings_possible_.value_shown AS value from util_global_settings_possible_ where (util_global_settings_possible_.setting_name = 'Default Number Format');

CREATE OR REPLACE VIEW  v_description AS select distinct amp_activity.amp_activity_id AS amp_activity_id,trim(dg_editor.BODY) AS ebody
from amp_activity, dg_editor where (amp_activity.description = dg_editor.EDITOR_KEY) order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_donor_commitment_date AS select f.amp_activity_id AS amp_activity_id,fd.transaction_date AS transaction_date
from amp_funding f, amp_funding_detail fd where ((f.amp_funding_id = fd.amp_funding_id) and (fd.transaction_type = 0)) order by f.amp_activity_id,fd.transaction_date;

CREATE OR REPLACE VIEW  v_donor_date_hierarchy AS select a.amp_activity_id AS amp_activity_id,fd.amp_fund_detail_id AS amp_fund_detail_id,fd.transaction_date AS full_date,extract (year from fd.transaction_date) AS year,extract (month from fd.transaction_date) AS month,
to_char(fd.transaction_date,'TMMonth') AS month_name,extract (quarter from fd.transaction_date) AS quarter,'Q'||extract( quarter from fd.transaction_date) AS quarter_name
from amp_activity a, amp_funding f, amp_funding_detail fd where ((a.amp_activity_id = f.amp_activity_id) and (f.amp_funding_id = fd.amp_funding_id));

CREATE OR REPLACE VIEW  v_donor_funding AS select f.amp_activity_id AS amp_activity_id,f.amp_funding_id AS amp_funding_id,
fd.amp_fund_detail_id AS amp_fund_detail_id,d.name AS donor_name,fd.transaction_type AS transaction_type,
fd.adjustment_type AS adjustment_type,fd.transaction_date AS transaction_date,fd.transaction_amount AS transaction_amount,
c.currency_code AS currency_code,cval.id AS terms_assist_id,cval.category_value AS terms_assist_name,fd.fixed_exchange_rate AS fixed_exchange_rate,
b.org_grp_name AS org_grp_name,ot.org_type AS donor_type_name,cval2.category_value AS financing_instrument_name,cval2.id AS financing_instrument_id,
d.amp_org_id AS org_grp_id,ot.amp_org_type_id AS org_type_id
from amp_funding f, amp_funding_detail fd, amp_category_value cval, amp_currency c, amp_organisation d, amp_org_group b, amp_org_type ot, amp_category_value cval2
where ((cval2.id = f.financing_instr_category_value) and (c.amp_currency_id = fd.amp_currency_id) and (f.amp_funding_id = fd.amp_funding_id) and (cval.id = f.type_of_assistance_category_va) and (d.amp_org_id = f.amp_donor_org_id) and (d.org_grp_id = b.amp_org_grp_id) and (ot.amp_org_type_id = d.org_type_id)) order by f.amp_activity_id;

CREATE OR REPLACE VIEW v_donor_groups AS select a.amp_activity_id AS amp_activity_id,b.org_grp_name AS name,b.amp_org_grp_id AS amp_org_grp_id
from amp_funding a, amp_organisation c, amp_org_group b where ((a.amp_donor_org_id = c.amp_org_id) and (c.org_grp_id = b.amp_org_grp_id)) order by a.amp_activity_id;

CREATE OR REPLACE VIEW  v_donor_type AS select f.amp_activity_id AS amp_activity_id,ot.org_type AS org_type,ot.amp_org_type_id AS org_type_id from amp_funding f , amp_organisation o,amp_org_type ot where ((f.amp_donor_org_id = o.amp_org_id) and (ot.amp_org_type_id = o.org_type_id)) order by f.amp_activity_id,ot.org_type;

CREATE OR REPLACE VIEW  v_donors AS select f.amp_activity_id AS amp_activity_id,o.name AS name,f.amp_donor_org_id AS amp_donor_org_id,o.org_grp_id AS org_grp_id,o.org_type_id AS org_type_id
from amp_funding f , amp_organisation o where (f.amp_donor_org_id = o.amp_org_id) order by f.amp_activity_id,o.name;

CREATE OR REPLACE VIEW  v_executing_agency AS select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id,f.percentage AS percentage from amp_org_role f , amp_organisation o, amp_role r WHERE (f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'EA') order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_executing_agency_groups AS select f.activity AS amp_activity_id,b.org_grp_name AS name,b.amp_org_grp_id AS amp_org_grp_id
from amp_org_role f , amp_organisation o, amp_role r, amp_org_group b WHERE (((f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'EA') and (o.org_grp_id = b.amp_org_grp_id))) order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_financing_instrument AS (select f.amp_activity_id AS amp_activity_id,val.category_value AS name,f.financing_instr_category_value AS amp_modality_id from amp_funding f , amp_category_value val where (f.financing_instr_category_value = val.id)) union (select eu.amp_activity_id AS amp_activity_id,val.category_value AS category_value,eu_con.FINANCING_INSTR_CATEGORY_VALUE AS financing_instr_category_value from amp_eu_activity eu , amp_eu_activity_contributions eu_con, amp_category_value val where ((eu_con.eu_activity_id = eu.id) and (eu_con.FINANCING_INSTR_CATEGORY_VALUE = val.id))) order by amp_activity_id,name;

CREATE OR REPLACE VIEW  v_g_settings_countries AS select dg_countries.ISO AS id,dg_countries.COUNTRY_NAME AS value from dg_countries order by dg_countries.COUNTRY_NAME;

CREATE OR REPLACE VIEW  v_g_settings_curr_fiscal_year AS select util_global_settings_possible_.value_id AS id,util_global_settings_possible_.value_shown AS value from util_global_settings_possible_ where (util_global_settings_possible_.setting_name = 'Current Fiscal Year');

CREATE OR REPLACE VIEW  v_g_settings_def_comp_type AS select amp_component_type.type_id AS id,amp_component_type.name AS value from amp_component_type where (amp_component_type.enable = true);

CREATE OR REPLACE VIEW  v_g_settings_default_calendar AS select amp_fiscal_calendar.amp_fiscal_cal_id AS id,amp_fiscal_calendar.name AS value from amp_fiscal_calendar;

CREATE OR REPLACE VIEW  v_g_settings_feature_templates AS select amp_feature_templates.id AS id,amp_feature_templates.featureTemplateName AS value from amp_feature_templates;

CREATE OR REPLACE VIEW  v_g_settings_filter_reports AS select util_global_settings_possible_.value_id AS id,util_global_settings_possible_.value_shown AS value from util_global_settings_possible_ where (util_global_settings_possible_.setting_name = 'Filter reports by month');

CREATE OR REPLACE VIEW  v_g_settings_public_view AS select util_global_settings_possible_.value_id AS id,util_global_settings_possible_.value_shown AS value from util_global_settings_possible_ where (util_global_settings_possible_.setting_name = 'Public View');

CREATE OR REPLACE VIEW  v_g_settings_pv_budget_filter AS select util_global_settings_possible_.value_id AS id,util_global_settings_possible_.value_shown AS value from util_global_settings_possible_ where (util_global_settings_possible_.setting_name = 'Public View Budget Filter');

CREATE OR REPLACE VIEW  v_g_settings_sector_schemes AS select amp_sector_scheme.amp_sec_scheme_id AS id,amp_sector_scheme.sec_scheme_name AS value from amp_sector_scheme order by amp_sector_scheme.sec_scheme_name;

CREATE OR REPLACE VIEW  v_g_settings_sh_comp_fund_year AS select util_global_settings_possible_.value_id AS id,util_global_settings_possible_.value_shown AS value from util_global_settings_possible_ where (util_global_settings_possible_.setting_name = 'Show Component Funding by Year');

CREATE OR REPLACE VIEW  v_g_settings_templ_visibility AS select amp_templates_visibility.id AS id,amp_templates_visibility.name AS value from amp_templates_visibility;

CREATE OR REPLACE VIEW  v_implementation_level AS select aac.amp_activity_id AS amp_activity_id,acv.category_value AS name,aac.amp_categoryvalue_id AS level_code
from amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac where ((acv.id = aac.amp_categoryvalue_id) and (acc.id = acv.amp_category_class_id) and (acc.keyName = 'implementation_level'));

CREATE OR REPLACE VIEW  v_implementation_location AS select aac.amp_activity_id AS amp_activity_id,acv.category_value AS name,aac.amp_categoryvalue_id AS amp_status_id
from amp_category_value acv,amp_category_class acc, amp_activities_categoryvalues aac where ((acv.id = aac.amp_categoryvalue_id) and (acc.id = acv.amp_category_class_id) and (acc.keyName = 'implementation_location'));

CREATE OR REPLACE VIEW  v_implementing_agency AS select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id
from amp_org_role f,amp_organisation o, amp_role r WHERE (f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'IA') order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_implementing_agency_groups AS select f.activity AS amp_activity_id,b.org_grp_name AS name,b.amp_org_grp_id AS amp_org_grp_id
from amp_org_role f , amp_organisation o, amp_role r, amp_org_group b WHERE (f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'IA') and (o.org_grp_id = b.amp_org_grp_id) order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_issues AS select ai.amp_activity_id AS amp_activity_id,ai.name AS name,ai.amp_issue_id AS amp_issue_id
from amp_issues ai , amp_activity a where (ai.amp_activity_id = a.amp_activity_id) order by ai.amp_activity_id;

CREATE OR REPLACE VIEW  v_measures_taken AS select ai.amp_activity_id AS amp_activity_id,m.name AS name,m.amp_measure_id AS amp_measure_id
from amp_activity a, amp_measure m, amp_issues ai where (ai.amp_activity_id = a.amp_activity_id) and (ai.amp_issue_id = m.amp_issue_id) order by ai.amp_activity_id;

CREATE OR REPLACE VIEW  v_nationalobjectives_all_level AS select a.amp_activity_id AS amp_activity_id,a.program_percentage AS program_percentage,a.amp_program_id AS amp_program_id,b.name AS n1,b.level_ AS l1,b1.name AS n2,b1.level_ AS l2,b2.name AS n3,b2.level_ AS l3,b3.name AS n4,b3.level_ AS l4,b4.name AS n5,b4.level_ AS l5,b5.name AS n6,b5.level_ AS l6,b6.name AS n7,b6.level_ AS l7,b7.name AS n8,b7.level_ AS l8
from ((((((((amp_activity_program a inner join amp_theme b on((a.amp_program_id = b.amp_theme_id))) left outer join amp_theme b1 on((b1.amp_theme_id = b.parent_theme_id))) left outer join amp_theme b2 on((b2.amp_theme_id = b1.parent_theme_id))) left outer join amp_theme b3 on((b3.amp_theme_id = b2.parent_theme_id))) left outer join amp_theme b4 on((b4.amp_theme_id = b3.parent_theme_id))) left outer join amp_theme b5 on((b5.amp_theme_id = b4.parent_theme_id))) left outer join amp_theme b6 on((b6.amp_theme_id = b5.parent_theme_id))) left outer join amp_theme b7 on((b7.amp_theme_id = b6.parent_theme_id))) where (a.program_setting = 1);

CREATE OR REPLACE VIEW  v_nationalobjectives_level_0 AS select v_nationalobjectives_all_level.amp_activity_id AS amp_activity_id,(case 0 when v_nationalobjectives_all_level.l1 then v_nationalobjectives_all_level.n1 when v_nationalobjectives_all_level.l2 then v_nationalobjectives_all_level.n2 when v_nationalobjectives_all_level.l3 then v_nationalobjectives_all_level.n3 when v_nationalobjectives_all_level.l4 then v_nationalobjectives_all_level.n4 when v_nationalobjectives_all_level.l5 then v_nationalobjectives_all_level.n5 when v_nationalobjectives_all_level.l6 then v_nationalobjectives_all_level.n6 when v_nationalobjectives_all_level.l7 then v_nationalobjectives_all_level.n7 when v_nationalobjectives_all_level.l8 then v_nationalobjectives_all_level.n8 end) AS name,v_nationalobjectives_all_level.amp_program_id AS amp_program_id,v_nationalobjectives_all_level.program_percentage AS program_percentage from v_nationalobjectives_all_level;

CREATE OR REPLACE VIEW  v_nationalobjectives_level_1 AS select v_nationalobjectives_all_level.amp_activity_id AS amp_activity_id,(case 1 when v_nationalobjectives_all_level.l1 then v_nationalobjectives_all_level.n1 when v_nationalobjectives_all_level.l2 then v_nationalobjectives_all_level.n2 when v_nationalobjectives_all_level.l3 then v_nationalobjectives_all_level.n3 when v_nationalobjectives_all_level.l4 then v_nationalobjectives_all_level.n4 when v_nationalobjectives_all_level.l5 then v_nationalobjectives_all_level.n5 when v_nationalobjectives_all_level.l6 then v_nationalobjectives_all_level.n6 when v_nationalobjectives_all_level.l7 then v_nationalobjectives_all_level.n7 when v_nationalobjectives_all_level.l8 then v_nationalobjectives_all_level.n8 end) AS name,v_nationalobjectives_all_level.amp_program_id AS amp_program_id,v_nationalobjectives_all_level.program_percentage AS program_percentage from v_nationalobjectives_all_level;

CREATE OR REPLACE VIEW  v_nationalobjectives_level_2 AS select v_nationalobjectives_all_level.amp_activity_id AS amp_activity_id,(case 2 when v_nationalobjectives_all_level.l1 then v_nationalobjectives_all_level.n1 when v_nationalobjectives_all_level.l2 then v_nationalobjectives_all_level.n2 when v_nationalobjectives_all_level.l3 then v_nationalobjectives_all_level.n3 when v_nationalobjectives_all_level.l4 then v_nationalobjectives_all_level.n4 when v_nationalobjectives_all_level.l5 then v_nationalobjectives_all_level.n5 when v_nationalobjectives_all_level.l6 then v_nationalobjectives_all_level.n6 when v_nationalobjectives_all_level.l7 then v_nationalobjectives_all_level.n7 when v_nationalobjectives_all_level.l8 then v_nationalobjectives_all_level.n8 end) AS name,v_nationalobjectives_all_level.amp_program_id AS amp_program_id,v_nationalobjectives_all_level.program_percentage AS program_percentage from v_nationalobjectives_all_level;

CREATE OR REPLACE VIEW  v_nationalobjectives_level_3 AS select v_nationalobjectives_all_level.amp_activity_id AS amp_activity_id,(case 3 when v_nationalobjectives_all_level.l1 then v_nationalobjectives_all_level.n1 when v_nationalobjectives_all_level.l2 then v_nationalobjectives_all_level.n2 when v_nationalobjectives_all_level.l3 then v_nationalobjectives_all_level.n3 when v_nationalobjectives_all_level.l4 then v_nationalobjectives_all_level.n4 when v_nationalobjectives_all_level.l5 then v_nationalobjectives_all_level.n5 when v_nationalobjectives_all_level.l6 then v_nationalobjectives_all_level.n6 when v_nationalobjectives_all_level.l7 then v_nationalobjectives_all_level.n7 when v_nationalobjectives_all_level.l8 then v_nationalobjectives_all_level.n8 end) AS name,v_nationalobjectives_all_level.amp_program_id AS amp_program_id,v_nationalobjectives_all_level.program_percentage AS program_percentage from v_nationalobjectives_all_level;

CREATE OR REPLACE VIEW  v_nationalobjectives_level_4 AS select v_nationalobjectives_all_level.amp_activity_id AS amp_activity_id,(case 4 when v_nationalobjectives_all_level.l1 then v_nationalobjectives_all_level.n1 when v_nationalobjectives_all_level.l2 then v_nationalobjectives_all_level.n2 when v_nationalobjectives_all_level.l3 then v_nationalobjectives_all_level.n3 when v_nationalobjectives_all_level.l4 then v_nationalobjectives_all_level.n4 when v_nationalobjectives_all_level.l5 then v_nationalobjectives_all_level.n5 when v_nationalobjectives_all_level.l6 then v_nationalobjectives_all_level.n6 when v_nationalobjectives_all_level.l7 then v_nationalobjectives_all_level.n7 when v_nationalobjectives_all_level.l8 then v_nationalobjectives_all_level.n8 end) AS name,v_nationalobjectives_all_level.amp_program_id AS amp_program_id,v_nationalobjectives_all_level.program_percentage AS program_percentage from v_nationalobjectives_all_level;

CREATE OR REPLACE VIEW  v_nationalobjectives_level_5 AS select v_nationalobjectives_all_level.amp_activity_id AS amp_activity_id,(case 5 when v_nationalobjectives_all_level.l1 then v_nationalobjectives_all_level.n1 when v_nationalobjectives_all_level.l2 then v_nationalobjectives_all_level.n2 when v_nationalobjectives_all_level.l3 then v_nationalobjectives_all_level.n3 when v_nationalobjectives_all_level.l4 then v_nationalobjectives_all_level.n4 when v_nationalobjectives_all_level.l5 then v_nationalobjectives_all_level.n5 when v_nationalobjectives_all_level.l6 then v_nationalobjectives_all_level.n6 when v_nationalobjectives_all_level.l7 then v_nationalobjectives_all_level.n7 when v_nationalobjectives_all_level.l8 then v_nationalobjectives_all_level.n8 end) AS name,v_nationalobjectives_all_level.amp_program_id AS amp_program_id,v_nationalobjectives_all_level.program_percentage AS program_percentage from v_nationalobjectives_all_level;

CREATE OR REPLACE VIEW  v_nationalobjectives_level_6 AS select v_nationalobjectives_all_level.amp_activity_id AS amp_activity_id,(case 6 when v_nationalobjectives_all_level.l1 then v_nationalobjectives_all_level.n1 when v_nationalobjectives_all_level.l2 then v_nationalobjectives_all_level.n2 when v_nationalobjectives_all_level.l3 then v_nationalobjectives_all_level.n3 when v_nationalobjectives_all_level.l4 then v_nationalobjectives_all_level.n4 when v_nationalobjectives_all_level.l5 then v_nationalobjectives_all_level.n5 when v_nationalobjectives_all_level.l6 then v_nationalobjectives_all_level.n6 when v_nationalobjectives_all_level.l7 then v_nationalobjectives_all_level.n7 when v_nationalobjectives_all_level.l8 then v_nationalobjectives_all_level.n8 end) AS name,v_nationalobjectives_all_level.amp_program_id AS amp_program_id,v_nationalobjectives_all_level.program_percentage AS program_percentage from v_nationalobjectives_all_level;

CREATE OR REPLACE VIEW  v_nationalobjectives_level_7 AS select v_nationalobjectives_all_level.amp_activity_id AS amp_activity_id,(case 7 when v_nationalobjectives_all_level.l1 then v_nationalobjectives_all_level.n1 when v_nationalobjectives_all_level.l2 then v_nationalobjectives_all_level.n2 when v_nationalobjectives_all_level.l3 then v_nationalobjectives_all_level.n3 when v_nationalobjectives_all_level.l4 then v_nationalobjectives_all_level.n4 when v_nationalobjectives_all_level.l5 then v_nationalobjectives_all_level.n5 when v_nationalobjectives_all_level.l6 then v_nationalobjectives_all_level.n6 when v_nationalobjectives_all_level.l7 then v_nationalobjectives_all_level.n7 when v_nationalobjectives_all_level.l8 then v_nationalobjectives_all_level.n8 end) AS name,v_nationalobjectives_all_level.amp_program_id AS amp_program_id,v_nationalobjectives_all_level.program_percentage AS program_percentage from v_nationalobjectives_all_level;

CREATE OR REPLACE VIEW  v_nationalobjectives_level_8 AS select v_nationalobjectives_all_level.amp_activity_id AS amp_activity_id,(case 8 when v_nationalobjectives_all_level.l1 then v_nationalobjectives_all_level.n1 when v_nationalobjectives_all_level.l2 then v_nationalobjectives_all_level.n2 when v_nationalobjectives_all_level.l3 then v_nationalobjectives_all_level.n3 when v_nationalobjectives_all_level.l4 then v_nationalobjectives_all_level.n4 when v_nationalobjectives_all_level.l5 then v_nationalobjectives_all_level.n5 when v_nationalobjectives_all_level.l6 then v_nationalobjectives_all_level.n6 when v_nationalobjectives_all_level.l7 then v_nationalobjectives_all_level.n7 when v_nationalobjectives_all_level.l8 then v_nationalobjectives_all_level.n8 end) AS name,v_nationalobjectives_all_level.amp_program_id AS amp_program_id,v_nationalobjectives_all_level.program_percentage AS program_percentage from v_nationalobjectives_all_level;

CREATE OR REPLACE VIEW  v_objectives AS select amp_activity.amp_activity_id AS amp_activity_id,trim(dg_editor.BODY) AS ebody from amp_activity , dg_editor where (amp_activity.objectives = dg_editor.EDITOR_KEY) order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_on_off_budget AS select a.amp_activity_id AS amp_activity_id,case when(a.budget = true) then 'yes' else 'no' end AS budget from amp_activity a order by a.amp_activity_id;

CREATE OR REPLACE VIEW  v_organization_projectid AS select aaii.amp_activity_id AS amp_activity_id,org.name||' -- '||aaii.internal_id AS name,aaii.amp_org_id AS amp_org_id from amp_activity_internal_id aaii , amp_organisation org where (aaii.amp_org_id = org.amp_org_id);

CREATE OR REPLACE VIEW  v_physical_description AS select a.amp_activity_id AS amp_activity_id,a.description AS description from amp_physical_performance a order by a.amp_activity_id;

CREATE OR REPLACE VIEW  v_physical_progress AS select p.amp_activity_id AS amp_activity_id,p.description AS description,p.amp_pp_id AS amp_pp_id from amp_physical_performance p order by p.reporting_date;

CREATE OR REPLACE VIEW  v_physical_title AS select a.amp_activity_id AS amp_activity_id,a.description AS description from amp_physical_performance a order by a.amp_activity_id;

CREATE OR REPLACE VIEW  v_primaryprogram AS select a.amp_activity_id AS amp_activity_id,t.name AS name,ap.amp_program_id AS amp_program_id,ap.program_percentage AS program_percentage from ((amp_activity a inner join amp_activity_program ap on(((a.amp_activity_id = ap.amp_activity_id) and (ap.program_setting = 2)))) inner join amp_theme t on((t.amp_theme_id = ap.amp_program_id))) order by a.amp_activity_id,t.name;

CREATE OR REPLACE VIEW  v_primaryprogram_all_level AS select a.amp_activity_id AS amp_activity_id,a.program_percentage AS program_percentage,a.amp_program_id AS amp_program_id,b.name AS n1,b.level_ AS l1,b1.name AS n2,b1.level_ AS l2,b2.name AS n3,b2.level_ AS l3,b3.name AS n4,b3.level_ AS l4,b4.name AS n5,b4.level_ AS l5,b5.name AS n6,b5.level_ AS l6,b6.name AS n7,b6.level_ AS l7,b7.name AS n8,b7.level_ AS l8 from ((((((((amp_activity_program a inner join amp_theme b on((a.amp_program_id = b.amp_theme_id))) left join amp_theme b1 on((b1.amp_theme_id = b.parent_theme_id))) left join amp_theme b2 on((b2.amp_theme_id = b1.parent_theme_id))) left join amp_theme b3 on((b3.amp_theme_id = b2.parent_theme_id))) left join amp_theme b4 on((b4.amp_theme_id = b3.parent_theme_id))) left join amp_theme b5 on((b5.amp_theme_id = b4.parent_theme_id))) left join amp_theme b6 on((b6.amp_theme_id = b5.parent_theme_id))) left join amp_theme b7 on((b7.amp_theme_id = b6.parent_theme_id))) where (a.program_setting = 2);

CREATE OR REPLACE VIEW  v_primaryprogram_cached AS select a.amp_activity_id AS amp_activity_id,case when (ap.program_setting = 3) then t.name else NULL end AS name,ap.amp_program_id AS amp_program_id,ap.program_percentage AS program_percentage,ap.program_setting AS program_setting from ((amp_activity a left join amp_activity_program ap on((a.amp_activity_id = ap.amp_activity_id))) left join amp_theme t on((t.amp_theme_id = ap.amp_program_id))) order by a.amp_activity_id,t.name;

CREATE OR REPLACE VIEW  v_primaryprogram_level_0 AS select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 0 when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1 when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2 when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3 when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4 when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5 when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6 when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7 when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8 end) AS name,v_primaryprogram_all_level.amp_program_id AS amp_program_id,v_primaryprogram_all_level.program_percentage AS program_percentage from v_primaryprogram_all_level;

CREATE OR REPLACE VIEW  v_primaryprogram_level_1 AS select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 1 when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1 when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2 when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3 when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4 when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5 when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6 when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7 when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8 end) AS name,v_primaryprogram_all_level.amp_program_id AS amp_program_id,v_primaryprogram_all_level.program_percentage AS program_percentage from v_primaryprogram_all_level;

CREATE OR REPLACE VIEW  v_primaryprogram_level_2 AS select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 2 when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1 when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2 when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3 when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4 when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5 when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6 when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7 when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8 end) AS name,v_primaryprogram_all_level.amp_program_id AS amp_program_id,v_primaryprogram_all_level.program_percentage AS program_percentage from v_primaryprogram_all_level;

CREATE OR REPLACE VIEW  v_primaryprogram_level_3 AS select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 3 when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1 when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2 when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3 when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4 when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5 when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6 when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7 when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8 end) AS name,v_primaryprogram_all_level.amp_program_id AS amp_program_id,v_primaryprogram_all_level.program_percentage AS program_percentage from v_primaryprogram_all_level;

CREATE OR REPLACE VIEW  v_primaryprogram_level_4 AS select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 4 when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1 when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2 when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3 when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4 when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5 when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6 when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7 when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8 end) AS name,v_primaryprogram_all_level.amp_program_id AS amp_program_id,v_primaryprogram_all_level.program_percentage AS program_percentage from v_primaryprogram_all_level;

CREATE OR REPLACE VIEW  v_primaryprogram_level_5 AS select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 5 when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1 when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2 when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3 when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4 when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5 when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6 when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7 when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8 end) AS name,v_primaryprogram_all_level.amp_program_id AS amp_program_id,v_primaryprogram_all_level.program_percentage AS program_percentage from v_primaryprogram_all_level;

CREATE OR REPLACE VIEW  v_primaryprogram_level_6 AS select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 6 when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1 when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2 when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3 when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4 when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5 when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6 when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7 when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8 end) AS name,v_primaryprogram_all_level.amp_program_id AS amp_program_id,v_primaryprogram_all_level.program_percentage AS program_percentage from v_primaryprogram_all_level;

CREATE OR REPLACE VIEW  v_primaryprogram_level_7 AS select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 7 when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1 when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2 when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3 when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4 when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5 when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6 when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7 when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8 end) AS name,v_primaryprogram_all_level.amp_program_id AS amp_program_id,v_primaryprogram_all_level.program_percentage AS program_percentage from v_primaryprogram_all_level;

CREATE OR REPLACE VIEW  v_primaryprogram_level_8 AS select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 8 when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1 when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2 when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3 when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4 when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5 when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6 when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7 when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8 end) AS name,v_primaryprogram_all_level.amp_program_id AS amp_program_id,v_primaryprogram_all_level.program_percentage AS program_percentage from v_primaryprogram_all_level;

CREATE OR REPLACE VIEW  v_project_category AS select aac.amp_activity_id AS amp_activity_id,acv.category_value AS name,aac.amp_categoryvalue_id AS amp_category_id from amp_category_value acv , amp_category_class acc, amp_activities_categoryvalues aac where ((acv.id = aac.amp_categoryvalue_id) and (acc.id = acv.amp_category_class_id) and (acc.keyName = 'project_category'));

CREATE OR REPLACE VIEW  v_project_comments AS select distinct amp_activity.amp_activity_id AS amp_activity_id,trim(dg_editor.BODY) AS editorbodytrimmed from amp_activity , dg_editor where (amp_activity.projectComments = dg_editor.EDITOR_KEY) order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_project_id AS select i.amp_activity_id AS amp_activity_id,i.internal_id||' ('||o.name ||')' AS name from amp_organisation o , amp_activity_internal_id i where (o.amp_org_id = i.amp_org_id);

CREATE OR REPLACE VIEW  v_proposed_completion_date AS select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.proposed_completion_date AS proposed_completion_date from amp_activity order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_proposed_cost AS select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.amp_activity_id AS object_id,amp_activity.amp_activity_id AS object_id2,amp_activity.proj_cost_amount AS transaction_amount,amp_activity.proj_cost_currcode AS currency_code,amp_activity.proj_cost_date AS transaction_date from amp_activity where (amp_activity.proj_cost_amount is not null);

CREATE OR REPLACE VIEW  v_purposes AS select amp_activity.amp_activity_id AS amp_activity_id,trim(dg_editor.BODY) AS ebody from amp_activity , dg_editor where (amp_activity.purpose = dg_editor.EDITOR_KEY) order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_regional_group AS select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id from amp_org_role f , amp_organisation o , amp_role r WHERE (f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'RG') order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_regions AS select ra.amp_activity_id AS amp_activity_id,l.region AS region,l.region_location_id AS region_id,sum(ra.location_percentage) AS location_percentage from ((amp_activity_location ra inner join amp_location l on((ra.amp_location_id = l.amp_location_id))) inner join amp_category_value_location cvl on (l.location_id = cvl.id)) where ((l.region_id is not null)) group by ra.amp_activity_id,l.region_id,l.region,l.region_location_id,l.name order by ra.amp_activity_id,l.name;

CREATE OR REPLACE VIEW  v_regions_cached AS select aa.amp_activity_id AS amp_activity_id,al.region_location_id AS region_id,sum(lp.location_percentage) AS location_percentage,acvl.location_name AS Region from (((amp_activity aa left join amp_activity_location lp on((aa.amp_activity_id = lp.amp_activity_id))) left join amp_location al on((lp.amp_location_id = al.amp_location_id))) left join amp_category_value_location acvl on((al.region_location_id = acvl.id))) group by aa.amp_activity_id,al.region_location_id,acvl.location_name order by aa.amp_activity_id,al.region_location_id;

CREATE OR REPLACE VIEW  v_responsible_org_groups AS select f.activity AS amp_activity_id,b.org_grp_name AS name,b.amp_org_grp_id AS amp_org_grp_id from amp_org_role f , amp_organisation o, amp_role r, amp_org_group b WHERE (f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'RO') and (o.org_grp_id = b.amp_org_grp_id) order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_responsible_organisation AS select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id from amp_org_role f , amp_organisation o, amp_role r WHERE (f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'RO') order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_results AS select amp_activity.amp_activity_id AS amp_activity_id,trim(dg_editor.BODY) AS ebody from amp_activity , dg_editor where (amp_activity.results = dg_editor.EDITOR_KEY) order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_secondary_sectors AS select sa.amp_activity_id AS amp_activity_id,getSectorName(getParentSectorId(s.amp_sector_id)) AS sectorname,getParentSectorId(s.amp_sector_id) AS amp_sector_id,sum(sa.sector_percentage) AS sector_percentage,s.amp_sec_scheme_id AS amp_sector_scheme_id from amp_activity_sector sa , amp_sector s , amp_sector_scheme ss where ((s.amp_sector_id = sa.amp_sector_id) and s.amp_sec_scheme_id in (select amp_classification_config.classification_id AS classification_id from amp_classification_config where (amp_classification_config.name = 'Secondary')) and (s.amp_sec_scheme_id = ss.amp_sec_scheme_id)) group by sa.amp_activity_id,s.amp_sec_scheme_id,getSectorName(getParentSectorId(s.amp_sector_id)), s.amp_sector_id order by sa.amp_activity_id,getSectorName(getParentSectorId(s.amp_sector_id));

CREATE OR REPLACE VIEW  v_secondary_sub_sectors AS select sa.amp_activity_id AS amp_activity_id,s.name AS name,s.amp_sector_id AS amp_sector_id,sa.sector_percentage AS sector_percentage from amp_activity_sector sa , amp_sector s , amp_sector_scheme ss WHERE ((sa.amp_sector_id = s.amp_sector_id)) and (CAST(s.sector_code AS bigint) > 1000) and s.amp_sec_scheme_id in (select amp_classification_config.classification_id AS classification_id from amp_classification_config where (amp_classification_config.name = 'Secondary')) and (s.amp_sec_scheme_id = ss.amp_sec_scheme_id) order by sa.amp_activity_id,s.name;

CREATE OR REPLACE VIEW  v_secondary_sub_sub_sectors AS select sa.amp_activity_id AS amp_activity_id,s.name AS name,s.amp_sector_id AS amp_sector_id,sa.sector_percentage AS sector_percentage from amp_activity_sector sa , amp_sector s, amp_sector_scheme ss WHERE sa.amp_sector_id = s.amp_sector_id AND (CAST(s.sector_code AS bigint) > 10000) and s.amp_sec_scheme_id in (select amp_classification_config.classification_id AS classification_id from amp_classification_config where (amp_classification_config.name = 'Secondary')) and (s.amp_sec_scheme_id = ss.amp_sec_scheme_id) order by sa.amp_activity_id,s.name;

CREATE OR REPLACE VIEW  v_secondaryprogram AS select a.amp_activity_id AS amp_activity_id,t.name AS name,ap.amp_program_id AS amp_program_id,ap.program_percentage AS program_percentage from ((amp_activity a inner join amp_activity_program ap on(((a.amp_activity_id = ap.amp_activity_id) and (ap.program_setting = 3)))) inner join amp_theme t on((t.amp_theme_id = ap.amp_program_id))) order by a.amp_activity_id,t.name;

CREATE OR REPLACE VIEW  v_secondaryprogram_all_level AS select a.amp_activity_id AS amp_activity_id,a.program_percentage AS program_percentage,a.amp_program_id AS amp_program_id,b.name AS n1,b.level_ AS l1,b1.name AS n2,b1.level_ AS l2,b2.name AS n3,b2.level_ AS l3,b3.name AS n4,b3.level_ AS l4,b4.name AS n5,b4.level_ AS l5,b5.name AS n6,b5.level_ AS l6,b6.name AS n7,b6.level_ AS l7,b7.name AS n8,b7.level_ AS l8 from ((((((((amp_activity_program a inner join amp_theme b on((a.amp_program_id = b.amp_theme_id))) left join amp_theme b1 on((b1.amp_theme_id = b.parent_theme_id))) left join amp_theme b2 on((b2.amp_theme_id = b1.parent_theme_id))) left join amp_theme b3 on((b3.amp_theme_id = b2.parent_theme_id))) left join amp_theme b4 on((b4.amp_theme_id = b3.parent_theme_id))) left join amp_theme b5 on((b5.amp_theme_id = b4.parent_theme_id))) left join amp_theme b6 on((b6.amp_theme_id = b5.parent_theme_id))) left join amp_theme b7 on((b7.amp_theme_id = b6.parent_theme_id))) where (a.program_setting = 3);

CREATE OR REPLACE VIEW  v_secondaryprogram_cached AS select a.amp_activity_id AS amp_activity_id,case when (ap.program_setting = 3) then t.name else NULL end AS name,ap.amp_program_id AS amp_program_id,ap.program_percentage AS program_percentage,ap.program_setting AS program_setting from ((amp_activity a left join amp_activity_program ap on((a.amp_activity_id = ap.amp_activity_id))) left join amp_theme t on((t.amp_theme_id = ap.amp_program_id))) order by a.amp_activity_id,t.name;

CREATE  OR REPLACE VIEW  v_secondaryprogram_level_0 AS select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 0 when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,v_secondaryprogram_all_level.amp_program_id AS amp_program_id,v_secondaryprogram_all_level.program_percentage AS program_percentage from v_secondaryprogram_all_level ;

CREATE  OR REPLACE VIEW  v_secondaryprogram_level_1 AS select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 1 when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,v_secondaryprogram_all_level.amp_program_id AS amp_program_id,v_secondaryprogram_all_level.program_percentage AS program_percentage from v_secondaryprogram_all_level ;

CREATE  OR REPLACE VIEW  v_secondaryprogram_level_2 AS select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 2 when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,v_secondaryprogram_all_level.amp_program_id AS amp_program_id,v_secondaryprogram_all_level.program_percentage AS program_percentage from v_secondaryprogram_all_level ;

CREATE  OR REPLACE VIEW  v_secondaryprogram_level_3 AS select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 3 when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,v_secondaryprogram_all_level.amp_program_id AS amp_program_id,v_secondaryprogram_all_level.program_percentage AS program_percentage from v_secondaryprogram_all_level ;

CREATE  OR REPLACE VIEW  v_secondaryprogram_level_4 AS select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 4 when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,v_secondaryprogram_all_level.amp_program_id AS amp_program_id,v_secondaryprogram_all_level.program_percentage AS program_percentage from v_secondaryprogram_all_level ;

CREATE  OR REPLACE VIEW  v_secondaryprogram_level_5 AS select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 5 when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,v_secondaryprogram_all_level.amp_program_id AS amp_program_id,v_secondaryprogram_all_level.program_percentage AS program_percentage from v_secondaryprogram_all_level ;

CREATE  OR REPLACE VIEW  v_secondaryprogram_level_6 AS select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 6 when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,v_secondaryprogram_all_level.amp_program_id AS amp_program_id,v_secondaryprogram_all_level.program_percentage AS program_percentage from v_secondaryprogram_all_level ;

CREATE  OR REPLACE VIEW  v_secondaryprogram_level_7 AS select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 7 when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,v_secondaryprogram_all_level.amp_program_id AS amp_program_id,v_secondaryprogram_all_level.program_percentage AS program_percentage from v_secondaryprogram_all_level ;

CREATE  OR REPLACE VIEW  v_secondaryprogram_level_8 AS select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,(case 8 when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,v_secondaryprogram_all_level.amp_program_id AS amp_program_id,v_secondaryprogram_all_level.program_percentage AS program_percentage from v_secondaryprogram_all_level ;

CREATE OR REPLACE VIEW  v_sector_group AS select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id from amp_org_role f , amp_organisation o, amp_role r WHERE (f.organisation = o.amp_org_id) and (f.role = r.amp_role_id) and (r.role_code = 'SG') order by f.activity,o.name;

CREATE OR REPLACE VIEW  v_sectors AS select sa.amp_activity_id AS amp_activity_id,getSectorName(getParentSectorId(s.amp_sector_id)) AS sectorname,getParentSectorId(s.amp_sector_id) AS amp_sector_id,sum(sa.sector_percentage) AS sector_percentage,s.amp_sec_scheme_id AS amp_sector_scheme_id,ss.sec_scheme_name AS sec_scheme_name from (((amp_sector_scheme ss join amp_classification_config cc on(((cc.name = 'Primary') and (cc.classification_id = ss.amp_sec_scheme_id)))) join amp_sector s on((s.amp_sec_scheme_id = ss.amp_sec_scheme_id))) join amp_activity_sector sa on(((sa.amp_sector_id = s.amp_sector_id) and (sa.classification_config_id = cc.id)))) group by sa.amp_activity_id,s.amp_sec_scheme_id,ss.sec_scheme_name,getParentSectorId(s.amp_sector_id) order by sa.amp_activity_id,getSectorName(getParentSectorId(s.amp_sector_id));

CREATE OR REPLACE VIEW  v_status AS select aac.amp_activity_id AS amp_activity_id,acv.category_value AS name,aac.amp_categoryvalue_id AS amp_status_id from amp_category_value acv, amp_category_class acc , amp_activities_categoryvalues aac where ((acv.id = aac.amp_categoryvalue_id) and (acc.id = acv.amp_category_class_id) and (acc.keyName = 'activity_status'));

CREATE OR REPLACE VIEW  v_sub_sectors AS select sa.amp_activity_id AS amp_activity_id,s.name AS name,s.amp_sector_id AS amp_sector_id,sa.sector_percentage AS sector_percentage from amp_activity_sector sa , amp_sector s, amp_sector_scheme ss WHERE (sa.amp_sector_id = s.amp_sector_id) AND (cast(s.sector_code AS bigint) > 1000) and s.amp_sec_scheme_id in (select amp_classification_config.classification_id AS classification_id from amp_classification_config where (amp_classification_config.name = 'Primary')) and (s.amp_sec_scheme_id = ss.amp_sec_scheme_id) order by sa.amp_activity_id,s.name;

CREATE OR REPLACE VIEW v_sub_sub_sectors AS select sa.amp_activity_id AS amp_activity_id,s.name AS name,s.amp_sector_id AS amp_sector_id,sa.sector_percentage AS sector_percentage from (amp_activity_sector sa join amp_sector s on((sa.amp_sector_id = s.amp_sector_id))) where ( cast(s.sector_code as bigint) > 10000) order by sa.amp_activity_id,s.name;

CREATE OR REPLACE VIEW  v_teams AS select a.amp_activity_id AS amp_activity_id,t.name AS name,t.amp_team_id AS amp_team_id from amp_activity a , amp_team t where (a.amp_team_id = t.amp_team_id) order by a.amp_activity_id,t.amp_team_id;

CREATE OR REPLACE VIEW  v_terms_assist AS select a.amp_activity_id AS amp_activity_id,val.category_value AS terms_assist_name,val.id AS terms_assist_code from amp_activity a , amp_funding fund, amp_category_value val where ((fund.amp_activity_id = a.amp_activity_id) and (val.id = fund.type_of_assistance_category_va)) group by a.amp_activity_id,val.id,val.category_value order by a.amp_activity_id,val.category_value;

CREATE OR REPLACE VIEW  v_titles AS select amp_activity.amp_activity_id AS amp_activity_id,amp_activity.name AS name,amp_activity.amp_activity_id AS title_id,amp_activity.draft AS draft,amp_activity.approval_status AS status from amp_activity order by amp_activity.amp_activity_id;

CREATE OR REPLACE VIEW  v_updated_date AS select a.amp_activity_id AS amp_activity_id,a.date_updated AS date_updated from amp_activity a order by a.amp_activity_id;

CREATE OR REPLACE VIEW  v_credit_donation AS select v.amp_activity_id AS amp_activity_id,(case when (convert_to(v.name, 'UTF8') = convert_to('Donaci�n', 'UTF8')) then convert_to('Donaci�n','UTF8') else convert_to('Credito','UTF8') end) AS name,(case when (convert_to(v.name, 'UTF8') = convert_to('Donaci�n', 'UTF8')) then 0 else 1 end) AS id from v_financing_instrument v;

create or replace view dual as select 1;