CREATE VIEW v_ac_chapters AS
    SELECT acc.amp_activity_id, acv.category_value AS name FROM amp_activities_categoryvalues acc, amp_category_value acv, amp_category_class ac WHERE (((acv.id = acc.amp_categoryvalue_id) AND (ac.id = acv.amp_category_class_id)) AND ((ac.keyname)::text = 'acchapter'::text));


ALTER TABLE public.v_ac_chapters OWNER TO amp;

CREATE VIEW v_accession_instruments AS
    SELECT acc.amp_activity_id, acv.category_value AS name FROM amp_activities_categoryvalues acc, amp_category_value acv, amp_category_class ac WHERE (((acv.id = acc.amp_categoryvalue_id) AND (ac.id = acv.amp_category_class_id)) AND ((ac.keyname)::text = 'accessioninstr'::text));


ALTER TABLE public.v_accession_instruments OWNER TO amp;

CREATE VIEW v_activity_changed_by AS
    SELECT a.amp_activity_id, u.email AS name, atm.user_ AS user_id FROM amp_activity a, amp_team_member atm, dg_user u WHERE ((atm.amp_team_mem_id = a.updated_by) AND (atm.user_ = u.id)) ORDER BY a.amp_activity_id;


ALTER TABLE public.v_activity_changed_by OWNER TO amp;

CREATE VIEW v_activity_creator AS
    SELECT a.amp_activity_id, (((u.first_names)::text || '
         '::text) || (u.last_name)::text) AS name, atm.user_ AS user_id FROM amp_activity a, amp_team_member atm, dg_user u WHERE ((atm.amp_team_mem_id = a.activity_creator) AND (atm.user_ = u.id)) ORDER BY a.amp_activity_id;


ALTER TABLE public.v_activity_creator OWNER TO amp;

CREATE VIEW v_actors AS
    SELECT ai.amp_activity_id, act.name, act.amp_actor_id FROM amp_activity a, amp_measure m, amp_issues ai, amp_actor act WHERE (((ai.amp_activity_id = a.amp_activity_id) AND (ai.amp_issue_id = m.amp_issue_id)) AND (act.amp_measure_id = m.amp_measure_id)) ORDER BY ai.amp_activity_id;


ALTER TABLE public.v_actors OWNER TO amp;

CREATE VIEW v_actual_approval_date AS
    SELECT amp_activity.amp_activity_id, amp_activity.actual_approval_date FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_actual_approval_date OWNER TO amp;

CREATE VIEW v_actual_completion_date AS
    SELECT amp_activity.amp_activity_id, amp_activity.actual_completion_date FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_actual_completion_date OWNER TO amp;

CREATE VIEW v_actual_completion_date_comments AS
    SELECT amp_comments.amp_activity_id, amp_comments.comment_ FROM amp_comments WHERE (amp_comments.amp_field_id = 1);


ALTER TABLE public.v_actual_completion_date_comments OWNER TO amp;

CREATE VIEW v_actual_proposed_date AS
    SELECT amp_activity.amp_activity_id, amp_activity.proposed_approval_date FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_actual_proposed_date OWNER TO amp;

CREATE VIEW v_actual_start_date AS
    SELECT amp_activity.amp_activity_id, amp_activity.actual_start_date FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_actual_start_date OWNER TO amp;

CREATE VIEW v_amp_id AS
    SELECT amp_activity.amp_activity_id, amp_activity.amp_id FROM amp_activity;


ALTER TABLE public.v_amp_id OWNER TO amp;
CREATE VIEW v_amp_theme AS
    SELECT amp_theme.amp_theme_id AS id, amp_theme.name AS value FROM amp_theme;


ALTER TABLE public.v_amp_theme OWNER TO amp;

CREATE VIEW v_ampid AS
    SELECT amp_activity.amp_activity_id, amp_activity.amp_id FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_ampid OWNER TO amp;

CREATE VIEW v_audit_system AS
    SELECT acc.amp_activity_id, acv.category_value AS name FROM amp_activities_categoryvalues acc, amp_category_value acv, amp_category_class ac WHERE (((acv.id = acc.amp_categoryvalue_id) AND (ac.id = acv.amp_category_class_id)) AND ((ac.keyname)::text = 'audit_system'::text));


ALTER TABLE public.v_audit_system OWNER TO amp;


CREATE VIEW v_beneficiary_agency AS
    SELECT f.activity AS amp_activity_id, o.name, f.organisation AS amp_org_id FROM amp_org_role f, amp_organisation o, amp_role r WHERE (((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'BA'::text)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_beneficiary_agency OWNER TO amp;


CREATE VIEW v_beneficiary_agency_groups AS
    SELECT f.activity AS amp_activity_id, b.org_grp_name AS name, b.amp_org_grp_id FROM amp_org_role f, amp_organisation o, amp_role r, amp_org_group b WHERE ((((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'BA'::text)) AND (o.org_grp_id = b.amp_org_grp_id)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_beneficiary_agency_groups OWNER TO amp;

CREATE VIEW v_beneficiary_agency_info AS
    SELECT aor.activity AS amp_activity_id, aor.additional_info, aor.organisation AS amp_org_id FROM amp_org_role aor, amp_role r WHERE ((aor.role = r.amp_role_id) AND ((r.role_code)::text = 'BA'::text));


ALTER TABLE public.v_beneficiary_agency_info OWNER TO amp;

CREATE VIEW v_bolivia_component_code AS
    SELECT a.amp_activity_id, c.code FROM amp_activity_components a, amp_components c WHERE (a.amp_component_id = c.amp_component_id) ORDER BY a.amp_activity_id;


ALTER TABLE public.v_bolivia_component_code OWNER TO amp;

CREATE VIEW v_budget_department AS
    SELECT a.amp_activity_id, (((dep.code)::text || ' - '::text) || (dep.name)::text) AS budget_sector FROM amp_activity a, amp_departments dep WHERE (a.budget_department = dep.id_department) ORDER BY a.amp_activity_id;


ALTER TABLE public.v_budget_department OWNER TO amp;


CREATE VIEW v_budget_organization AS
    SELECT a.amp_activity_id, (((org.name)::text || ' - '::text) || (org.budget_org_code)::text) AS budget_sector FROM amp_activity a, amp_organisation org WHERE (a.budget_organization = org.amp_org_id) ORDER BY a.amp_activity_id;


ALTER TABLE public.v_budget_organization OWNER TO amp;

CREATE VIEW v_budget_program AS
    SELECT a.amp_activity_id, (((prog.theme_code)::text || ' - '::text) || (prog.name)::text) AS budget_sector FROM amp_activity a, amp_theme prog WHERE (a.budget_program = prog.amp_theme_id) ORDER BY a.amp_activity_id;


ALTER TABLE public.v_budget_program OWNER TO amp;


CREATE VIEW v_budget_sector AS
    SELECT a.amp_activity_id, (((bs.code)::text || ' - '::text) || (bs.sector_name)::text) AS budget_sector FROM amp_activity a, amp_budget_sector bs WHERE (a.budget_sector = bs.budged_sector_id) ORDER BY a.amp_activity_id;


ALTER TABLE public.v_budget_sector OWNER TO amp;


CREATE VIEW v_budgeting_year AS
    SELECT a.amp_activity_id, c.year FROM amp_chapter c, amp_activity a WHERE ((a.chapter_code)::text = (c.code)::text);


ALTER TABLE public.v_budgeting_year OWNER TO amp;


CREATE VIEW v_code_chapitre AS
    SELECT a.amp_activity_id, c.code FROM amp_chapter c, amp_activity a WHERE ((a.chapter_code)::text = (c.code)::text);


ALTER TABLE public.v_code_chapitre OWNER TO amp;


CREATE VIEW v_component_description AS
    SELECT DISTINCT a.amp_activity_id, b.description AS title, b.amp_component_id FROM amp_activity a, amp_components b, amp_component_funding c WHERE ((a.amp_activity_id = c.activity_id) AND (b.amp_component_id = c.amp_component_id));


ALTER TABLE public.v_component_description OWNER TO amp;


CREATE VIEW v_component_funding AS
    SELECT f.activity_id AS amp_activity_id, f.amp_component_funding_id, f.amp_component_funding_id AS amp_fund_detail_id, c.title AS component_name, f.transaction_type, f.adjustment_type, f.transaction_date, f.transaction_amount, f.currency_id, cu.currency_code FROM amp_component_funding f, amp_components c, amp_currency cu WHERE ((cu.amp_currency_id = f.currency_id) AND (f.amp_component_id = c.amp_component_id)) ORDER BY f.activity_id;


ALTER TABLE public.v_component_funding OWNER TO amp;


CREATE VIEW v_component_type AS
    SELECT aac.amp_activity_id, ct.name AS component_type, ct.type_id AS component_type_id FROM amp_components c, amp_component_type ct, amp_activity_components aac WHERE ((c.type = ct.type_id) AND (aac.amp_component_id = c.amp_component_id));


ALTER TABLE public.v_component_type OWNER TO amp;


CREATE VIEW v_componente AS
    SELECT sa.amp_activity_id, s.name, sa.amp_sector_id, sa.percentage FROM amp_activity_componente sa, amp_sector s WHERE (sa.amp_sector_id = s.amp_sector_id) ORDER BY sa.amp_activity_id, s.name;


ALTER TABLE public.v_componente OWNER TO amp;

CREATE VIEW v_components AS
    SELECT DISTINCT a.amp_activity_id, b.title, b.amp_component_id FROM amp_activity a, amp_components b, amp_component_funding c WHERE ((a.amp_activity_id = c.activity_id) AND (b.amp_component_id = c.amp_component_id));


ALTER TABLE public.v_components OWNER TO amp;


CREATE VIEW v_computed_dates AS
    SELECT amp_activity.amp_activity_id, amp_activity.activity_close_date, amp_activity.actual_start_date, amp_activity.actual_approval_date, amp_activity.activity_approval_date, amp_activity.proposed_start_date, amp_activity.actual_completion_date, amp_activity.proposed_completion_date FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_computed_dates OWNER TO amp;

CREATE VIEW v_contracting_agency AS
    SELECT f.activity AS amp_activity_id, o.name, f.organisation AS amp_org_id FROM amp_org_role f, amp_organisation o, amp_role r WHERE (((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'CA'::text)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_contracting_agency OWNER TO amp;


CREATE VIEW v_contracting_agency_info AS
    SELECT aor.activity AS amp_activity_id, aor.additional_info, aor.organisation AS amp_org_id FROM amp_org_role aor, amp_role r WHERE ((aor.role = r.amp_role_id) AND ((r.role_code)::text = 'CA'::text));


ALTER TABLE public.v_contracting_agency_info OWNER TO amp;


CREATE VIEW v_contracting_date AS
    SELECT amp_activity.amp_activity_id, amp_activity.contracting_date FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_contracting_date OWNER TO amp;

CREATE VIEW v_contribution_funding AS
    SELECT eu.amp_activity_id, eu.id AS amp_funding_id, euc.id AS amp_funding_detail_id, o.name AS donor_name, euc.amount AS transaction_amount, euc.transaction_date, c.currency_code, acv_term.category_value AS terms_assist_name, acv_mod.category_value AS financing_instrument_name, o.amp_org_id, o.org_grp_id, acv_term.id AS terms_assist_id FROM amp_eu_activity eu, amp_eu_activity_contributions euc, amp_currency c, amp_category_value acv_term, amp_category_value acv_mod, amp_organisation o WHERE (((((eu.id = euc.eu_activity_id) AND (euc.amount_currency = c.amp_currency_id)) AND (acv_term.id = euc.financing_type_categ_val_id)) AND (acv_mod.id = euc.financing_instr_category_value)) AND (o.amp_org_id = euc.donor_id)) ORDER BY eu.amp_activity_id;


ALTER TABLE public.v_contribution_funding OWNER TO amp;


CREATE VIEW v_convenio_numcont AS
    SELECT amp_activity.amp_activity_id, CASE amp_activity.convenio_numcont WHEN '-'::text THEN NULL::character varying WHEN '.'::text THEN NULL::character varying ELSE amp_activity.convenio_numcont END AS numcont FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_convenio_numcont OWNER TO amp;


CREATE VIEW v_costing_donors AS
    SELECT eu.amp_activity_id, o.name, euc.donor_id FROM amp_activity a, amp_eu_activity eu, amp_eu_activity_contributions euc, amp_organisation o WHERE (((a.amp_activity_id = eu.amp_activity_id) AND (eu.id = euc.eu_activity_id)) AND (euc.donor_id = o.amp_org_id)) ORDER BY o.name;


ALTER TABLE public.v_costing_donors OWNER TO amp;


CREATE VIEW v_costs AS
    SELECT eu.amp_activity_id, eu.id AS eu_id, eu.total_cost AS cost, c.currency_code, eu.transaction_date AS currency_date, getexchange((c.currency_code)::bpchar, eu.transaction_date) AS exchange_rate FROM amp_eu_activity eu, amp_currency c WHERE (eu.total_cost_currency_id = c.amp_currency_id);


ALTER TABLE public.v_costs OWNER TO amp;

CREATE VIEW v_countries AS
    SELECT ra.amp_activity_id, getlocationname(getlocationidbyimplloc(l.location_id, 'Country'::character varying)) AS location_name, getlocationidbyimplloc(l.location_id, 'Country'::character varying) AS location_id, sum(ra.location_percentage) AS location_percentage FROM amp_activity_location ra, amp_location l WHERE ((ra.amp_location_id = l.amp_location_id) AND (getlocationidbyimplloc(l.location_id, 'Country'::character varying) IS NOT NULL)) GROUP BY ra.amp_activity_id, getlocationidbyimplloc(l.location_id, 'Country'::character varying);


ALTER TABLE public.v_countries OWNER TO amp;


CREATE VIEW v_creation_date AS
    SELECT a.amp_activity_id, a.date_created AS creation_date FROM amp_activity a ORDER BY a.amp_activity_id;


ALTER TABLE public.v_creation_date OWNER TO amp;


CREATE VIEW v_financing_instrument AS
    SELECT f.amp_activity_id, val.category_value AS name, f.financing_instr_category_value AS amp_modality_id FROM amp_funding f, amp_category_value val WHERE (f.financing_instr_category_value = val.id) UNION SELECT eu.amp_activity_id, val.category_value AS name, eu_con.financing_instr_category_value AS amp_modality_id FROM amp_eu_activity eu, amp_eu_activity_contributions eu_con, amp_category_value val WHERE ((eu_con.eu_activity_id = eu.id) AND (eu_con.financing_instr_category_value = val.id)) ORDER BY 1, 2;


ALTER TABLE public.v_financing_instrument OWNER TO amp;


CREATE VIEW v_credit_donation AS
    SELECT v.amp_activity_id, CASE WHEN (convert_to((v.name)::text, 'UTF8'::name) = convert_to('Donacion'::text, 'UTF8'::name)) THEN convert_to('Donacion'::text, 'UTF8'::name) ELSE convert_to('Credito'::text, 'UTF8'::name) END AS name, CASE WHEN (convert_to((v.name)::text, 'UTF8'::name) = convert_to('Donacion'::text, 'UTF8'::name)) THEN 0 ELSE 1 END AS id FROM v_financing_instrument v;


ALTER TABLE public.v_credit_donation OWNER TO amp;


CREATE VIEW v_cris_number AS
    SELECT a.amp_activity_id, a.cris_number FROM amp_activity a ORDER BY a.amp_activity_id;


ALTER TABLE public.v_cris_number OWNER TO amp;


CREATE VIEW v_date_formats AS
    SELECT util_global_settings_possible_values.value_id AS id, util_global_settings_possible_values.value_shown AS value FROM util_global_settings_possible_values WHERE ((util_global_settings_possible_values.setting_name)::text = 'Default Date Format'::text);


ALTER TABLE public.v_date_formats OWNER TO amp;


CREATE VIEW v_default_number_format AS
    SELECT util_global_settings_possible_values.value_id AS id, util_global_settings_possible_values.value_shown AS value FROM util_global_settings_possible_values WHERE ((util_global_settings_possible_values.setting_name)::text = 'Default Number Format'::text);


ALTER TABLE public.v_default_number_format OWNER TO amp;


CREATE VIEW v_description AS
    SELECT DISTINCT amp_activity.amp_activity_id, btrim(dg_editor.body) AS ebody FROM amp_activity, dg_editor WHERE (amp_activity.description = (dg_editor.editor_key)::text) ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_description OWNER TO amp;


CREATE VIEW v_description_chapitre AS
    SELECT a.amp_activity_id, c.description FROM amp_chapter c, amp_activity a WHERE ((a.chapter_code)::text = (c.code)::text);


ALTER TABLE public.v_description_chapitre OWNER TO amp;


CREATE VIEW v_description_imputation AS
    SELECT a.amp_activity_id, i.description FROM amp_chapter c, amp_activity a, amp_imputation i WHERE (((a.chapter_code)::text = (c.code)::text) AND ((i.chapter_code)::text = (c.code)::text));


ALTER TABLE public.v_description_imputation OWNER TO amp;


CREATE VIEW v_disbursements_date AS
    SELECT amp_activity.amp_activity_id, amp_activity.disbursments_date FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_disbursements_date OWNER TO amp;


CREATE VIEW v_disbursements_date_comments AS
    SELECT amp_comments.amp_activity_id, amp_comments.comment_ FROM amp_comments WHERE (amp_comments.amp_field_id = 12);


ALTER TABLE public.v_disbursements_date_comments OWNER TO amp;


CREATE VIEW v_districts AS
    SELECT ra.amp_activity_id, getlocationname(getlocationidbyimplloc(l.location_id, 'District'::character varying)) AS location_name, getlocationidbyimplloc(l.location_id, 'District'::character varying) AS location_id, sum(ra.location_percentage) AS location_percentage FROM amp_activity_location ra, amp_location l WHERE ((ra.amp_location_id = l.amp_location_id) AND (getlocationidbyimplloc(l.location_id, 'District'::character varying) IS NOT NULL)) GROUP BY ra.amp_activity_id, getlocationidbyimplloc(l.location_id, 'District'::character varying);


ALTER TABLE public.v_districts OWNER TO amp;

CREATE VIEW v_donor_commitment_date AS
    SELECT f.amp_activity_id, fd.transaction_date FROM amp_funding f, amp_funding_detail fd WHERE ((f.amp_funding_id = fd.amp_funding_id) AND (fd.transaction_type = 0)) ORDER BY f.amp_activity_id, fd.transaction_date;


ALTER TABLE public.v_donor_commitment_date OWNER TO amp;

CREATE VIEW v_donor_cont_email AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS email FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'DONOR_CONT'::text)) AND ((cp.name)::text = 'contact email'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_donor_cont_email OWNER TO amp;

CREATE VIEW v_donor_cont_fax AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS fax FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'DONOR_CONT'::text)) AND ((cp.name)::text = 'contact fax'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_donor_cont_fax OWNER TO amp;


CREATE VIEW v_donor_cont_name AS
    SELECT ac.activity_id AS amp_activity_id, (((c.name)::text || ' '::text) || (c.lastname)::text) AS contact FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'DONOR_CONT'::text)) AND (btrim((((c.name)::text || ' '::text) || (c.lastname)::text)) <> ''::text));


ALTER TABLE public.v_donor_cont_name OWNER TO amp;

CREATE VIEW v_donor_cont_org AS
    SELECT ac.activity_id AS amp_activity_id, c.organisation_name AS org FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'DONOR_CONT'::text)) AND (btrim((c.organisation_name)::text) <> ' '::text)) UNION SELECT ac.activity_id AS amp_activity_id, org.name AS org FROM amp_activity_contact ac, amp_org_contact oc, amp_organisation org WHERE (((ac.contact_id = oc.contact_id) AND (oc.amp_org_id = org.amp_org_id)) AND ((ac.contact_type)::text = 'DONOR_CONT'::text));


ALTER TABLE public.v_donor_cont_org OWNER TO amp;


CREATE VIEW v_donor_cont_phone AS
    SELECT ac.activity_id AS amp_activity_id, (COALESCE(((((SELECT amp_category_value.category_value FROM amp_category_value WHERE ((amp_category_value.id)::text = "substring"((cp.value)::text, 1, "position"((cp.value)::text, ' '::text)))))::text || ' '::text) || substr((cp.value)::text, "position"((cp.value)::text, ' '::text)))) || "substring"((cp.value)::text, "position"((cp.value)::text, ' '::text))) AS phone FROM amp_activity_contact ac, amp_contact_properties cp WHERE (((ac.contact_id = cp.contact_id) AND ((cp.name)::text = 'contact phone'::text)) AND ((ac.contact_type)::text = 'DONOR_CONT'::text));


ALTER TABLE public.v_donor_cont_phone OWNER TO amp;

CREATE VIEW v_donor_cont_title AS
    SELECT ac.activity_id AS amp_activity_id, c.function FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'DONOR_CONT'::text)) AND (btrim((c.function)::text) <> ' '::text));


ALTER TABLE public.v_donor_cont_title OWNER TO amp;


CREATE VIEW v_donor_date_hierarchy AS
    SELECT a.amp_activity_id, fd.amp_fund_detail_id, fd.transaction_date AS full_date, date_part('year'::text, fd.transaction_date) AS year, date_part('month'::text, fd.transaction_date) AS month, to_char(fd.transaction_date, 'TMMonth'::text) AS month_name, date_part('quarter'::text, fd.transaction_date) AS quarter, ('Q'::text || date_part('quarter'::text, fd.transaction_date)) AS quarter_name FROM amp_activity a, amp_funding f, amp_funding_detail fd WHERE ((a.amp_activity_id = f.amp_activity_id) AND (f.amp_funding_id = fd.amp_funding_id));


ALTER TABLE public.v_donor_date_hierarchy OWNER TO amp;


CREATE VIEW v_donor_funding AS
    SELECT f.amp_activity_id, f.amp_funding_id, fd.amp_fund_detail_id, d.name AS donor_name, fd.transaction_type, fd.adjustment_type, fd.transaction_date, fd.transaction_amount, fd.pledge_id, c.currency_code, cval.id AS terms_assist_id, cval.category_value AS terms_assist_name, fd.fixed_exchange_rate, b.org_grp_name, ot.org_type AS donor_type_name, cval2.category_value AS financing_instrument_name, cval2.id AS financing_instrument_id, d.amp_org_id AS org_id, d.org_grp_id, ot.amp_org_type_id AS org_type_id FROM amp_funding f, amp_funding_detail fd, amp_category_value cval, amp_currency c, amp_organisation d, amp_org_group b, amp_org_type ot, amp_category_value cval2 WHERE (((((((cval2.id = f.financing_instr_category_value) AND (c.amp_currency_id = fd.amp_currency_id)) AND (f.amp_funding_id = fd.amp_funding_id)) AND (cval.id = f.type_of_assistance_category_va)) AND (d.amp_org_id = f.amp_donor_org_id)) AND (d.org_grp_id = b.amp_org_grp_id)) AND (ot.amp_org_type_id = b.org_type)) ORDER BY f.amp_activity_id;


ALTER TABLE public.v_donor_funding OWNER TO amp;


CREATE VIEW v_donor_groups AS
    SELECT a.amp_activity_id, b.org_grp_name AS name, b.amp_org_grp_id FROM amp_funding a, amp_organisation c, amp_org_group b WHERE ((a.amp_donor_org_id = c.amp_org_id) AND (c.org_grp_id = b.amp_org_grp_id)) ORDER BY a.amp_activity_id;


ALTER TABLE public.v_donor_groups OWNER TO amp;


CREATE VIEW v_donor_type AS
    SELECT f.amp_activity_id, ot.org_type, ot.amp_org_type_id AS org_type_id FROM amp_funding f, amp_organisation o, amp_org_group gr, amp_org_type ot WHERE (((f.amp_donor_org_id = o.amp_org_id) AND (o.org_grp_id = gr.amp_org_grp_id)) AND (ot.amp_org_type_id = gr.org_type)) ORDER BY f.amp_activity_id, ot.org_type;


ALTER TABLE public.v_donor_type OWNER TO amp;


CREATE VIEW v_donors AS
    SELECT f.amp_activity_id, o.name, f.amp_donor_org_id, o.org_grp_id, gr.org_type AS org_type_id FROM amp_funding f, amp_organisation o, amp_org_group gr WHERE ((f.amp_donor_org_id = o.amp_org_id) AND (o.org_grp_id = gr.amp_org_grp_id)) ORDER BY f.amp_activity_id, o.name;


ALTER TABLE public.v_donors OWNER TO amp;


CREATE VIEW v_executing_agency AS
    SELECT f.activity AS amp_activity_id, o.name, f.organisation AS amp_org_id, f.percentage FROM amp_org_role f, amp_organisation o, amp_role r WHERE (((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'EA'::text)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_executing_agency OWNER TO amp;

CREATE VIEW v_executing_agency_groups AS
    SELECT f.activity AS amp_activity_id, b.org_grp_name AS name, b.amp_org_grp_id FROM amp_org_role f, amp_organisation o, amp_role r, amp_org_group b WHERE ((((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'EA'::text)) AND (o.org_grp_id = b.amp_org_grp_id)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_executing_agency_groups OWNER TO amp;


CREATE VIEW v_executing_agency_info AS
    SELECT aor.activity AS amp_activity_id, aor.additional_info, aor.organisation AS amp_org_id FROM amp_org_role aor, amp_role r WHERE ((aor.role = r.amp_role_id) AND ((r.role_code)::text = 'EA'::text));


ALTER TABLE public.v_executing_agency_info OWNER TO amp;

CREATE VIEW v_financial_instrument AS
    SELECT aac.amp_activity_id, acv.category_value, acv.id FROM amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac WHERE (((acv.amp_category_class_id = acc.id) AND ((acc.keyname)::text = 'financial_instrument'::text)) AND (aac.amp_categoryvalue_id = acv.id));


ALTER TABLE public.v_financial_instrument OWNER TO amp;

CREATE VIEW v_funding_end_date AS
    SELECT f.amp_activity_id, f.amp_funding_id, f.actual_completion_date FROM (amp_funding f JOIN amp_activity a ON ((f.amp_activity_id = a.amp_activity_id))) ORDER BY f.amp_activity_id;


ALTER TABLE public.v_funding_end_date OWNER TO amp;


CREATE VIEW v_funding_org_id AS
    SELECT f.amp_activity_id, f.financing_id AS funding_org_id FROM (amp_funding f JOIN amp_activity a ON ((((f.amp_activity_id = a.amp_activity_id) AND (f.financing_id IS NOT NULL)) AND ((f.financing_id)::text <> ''::text)))) ORDER BY f.amp_activity_id;


ALTER TABLE public.v_funding_org_id OWNER TO amp;

CREATE VIEW v_funding_start_date AS
    SELECT f.amp_activity_id, f.amp_funding_id, f.actual_start_date FROM (amp_funding f JOIN amp_activity a ON ((f.amp_activity_id = a.amp_activity_id))) ORDER BY f.amp_activity_id;


ALTER TABLE public.v_funding_start_date OWNER TO amp;


CREATE VIEW v_funding_status AS
    SELECT a.amp_activity_id, val.category_value AS funding_status_name, val.id AS funding_status_code FROM amp_activity a, amp_funding fund, amp_category_value val, amp_category_class c WHERE ((((fund.amp_activity_id = a.amp_activity_id) AND (val.id = fund.funding_status_category_va)) AND ((c.keyname)::text = 'funding_status'::text)) AND (c.id = val.amp_category_class_id)) GROUP BY a.amp_activity_id, val.id, val.category_value ORDER BY a.amp_activity_id, val.category_value;


ALTER TABLE public.v_funding_status OWNER TO amp;


CREATE VIEW v_g_settings_bso AS
    SELECT cv.id, cv.category_value AS value FROM amp_category_value cv, amp_category_class cc WHERE ((cv.amp_category_class_id = cc.id) AND ((cc.keyname)::text = 'financing_instrument'::text));


ALTER TABLE public.v_g_settings_bso OWNER TO amp;


CREATE VIEW v_g_settings_countries AS
    SELECT dg_countries.iso AS id, dg_countries.country_name AS value FROM dg_countries ORDER BY dg_countries.country_name;


ALTER TABLE public.v_g_settings_countries OWNER TO amp;


CREATE VIEW v_g_settings_curr_fiscal_year AS
    SELECT util_global_settings_possible_values.value_id AS id, util_global_settings_possible_values.value_shown AS value FROM util_global_settings_possible_values WHERE ((util_global_settings_possible_values.setting_name)::text = 'Current Fiscal Year'::text);


ALTER TABLE public.v_g_settings_curr_fiscal_year OWNER TO amp;


CREATE VIEW v_g_settings_currency AS
    SELECT amp_currency.currency_code AS id, amp_currency.currency_name AS value FROM amp_currency WHERE (amp_currency.active_flag = 1) ORDER BY amp_currency.currency_name;


ALTER TABLE public.v_g_settings_currency OWNER TO amp;


CREATE VIEW v_g_settings_def_comp_type AS
    SELECT amp_component_type.type_id AS id, amp_component_type.name AS value FROM amp_component_type WHERE (amp_component_type.enable = true);


ALTER TABLE public.v_g_settings_def_comp_type OWNER TO amp;


CREATE VIEW v_g_settings_default_calendar AS
    SELECT amp_fiscal_calendar.amp_fiscal_cal_id AS id, amp_fiscal_calendar.name AS value FROM amp_fiscal_calendar;


ALTER TABLE public.v_g_settings_default_calendar OWNER TO amp;


CREATE VIEW v_g_settings_feature_templates AS
    SELECT amp_feature_templates.id, amp_feature_templates.featuretemplatename AS value FROM amp_feature_templates;


ALTER TABLE public.v_g_settings_feature_templates OWNER TO amp;


CREATE VIEW v_g_settings_filter_reports AS
    SELECT util_global_settings_possible_values.value_id AS id, util_global_settings_possible_values.value_shown AS value FROM util_global_settings_possible_values WHERE ((util_global_settings_possible_values.setting_name)::text = 'Filter reports by month'::text);


ALTER TABLE public.v_g_settings_filter_reports OWNER TO amp;


CREATE VIEW v_g_settings_public_view AS
    SELECT util_global_settings_possible_values.value_id AS id, util_global_settings_possible_values.value_shown AS value FROM util_global_settings_possible_values WHERE ((util_global_settings_possible_values.setting_name)::text = 'Public View'::text);


ALTER TABLE public.v_g_settings_public_view OWNER TO amp;

CREATE VIEW v_g_settings_pv_budget_filter AS
    SELECT util_global_settings_possible_values.value_id AS id, util_global_settings_possible_values.value_shown AS value FROM util_global_settings_possible_values WHERE ((util_global_settings_possible_values.setting_name)::text = 'Public View Budget Filter'::text);


ALTER TABLE public.v_g_settings_pv_budget_filter OWNER TO amp;

CREATE VIEW v_g_settings_sector_schemes AS
    SELECT amp_sector_scheme.amp_sec_scheme_id AS id, amp_sector_scheme.sec_scheme_name AS value FROM amp_sector_scheme ORDER BY amp_sector_scheme.sec_scheme_name;


ALTER TABLE public.v_g_settings_sector_schemes OWNER TO amp;

CREATE VIEW v_g_settings_sh_comp_fund_year AS
    SELECT util_global_settings_possible_values.value_id AS id, util_global_settings_possible_values.value_shown AS value FROM util_global_settings_possible_values WHERE ((util_global_settings_possible_values.setting_name)::text = 'Show Component Funding by Year'::text);


ALTER TABLE public.v_g_settings_sh_comp_fund_year OWNER TO amp;

CREATE VIEW v_g_settings_templ_visibility AS
    SELECT amp_templates_visibility.id, amp_templates_visibility.name AS value FROM amp_templates_visibility;


ALTER TABLE public.v_g_settings_templ_visibility OWNER TO amp;


CREATE VIEW v_gov_agreement_number AS
    SELECT a.amp_activity_id, a.gov_agreement_number FROM amp_activity a WHERE (btrim((a.gov_agreement_number)::text) <> ''::text);


ALTER TABLE public.v_gov_agreement_number OWNER TO amp;


CREATE VIEW v_impl_ex_cont_email AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS email FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'IMPL_EXEC_AGENCY_CONT'::text)) AND ((cp.name)::text = 'contact email'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_impl_ex_cont_email OWNER TO amp;


CREATE VIEW v_impl_ex_cont_fax AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS fax FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'IMPL_EXEC_AGENCY_CONT'::text)) AND ((cp.name)::text = 'contact fax'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_impl_ex_cont_fax OWNER TO amp;


CREATE VIEW v_impl_ex_cont_name AS
    SELECT ac.activity_id AS amp_activity_id, btrim((((c.name)::text || ' '::text) || (c.lastname)::text)) AS contact FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'IMPL_EXEC_AGENCY_CONT'::text)) AND (btrim((((c.name)::text || ' '::text) || (c.lastname)::text)) <> ''::text));


ALTER TABLE public.v_impl_ex_cont_name OWNER TO amp;

CREATE VIEW v_impl_ex_cont_org AS
    SELECT ac.activity_id AS amp_activity_id, c.organisation_name AS org FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'IMPL_EXEC_AGENCY_CONT'::text)) AND (btrim((c.organisation_name)::text) <> ' '::text)) UNION SELECT ac.activity_id AS amp_activity_id, org.name AS org FROM amp_activity_contact ac, amp_org_contact oc, amp_organisation org WHERE (((ac.contact_id = oc.contact_id) AND (oc.amp_org_id = org.amp_org_id)) AND ((ac.contact_type)::text = 'IMPL_EXEC_AGENCY_CONT'::text));


ALTER TABLE public.v_impl_ex_cont_org OWNER TO amp;

CREATE VIEW v_impl_ex_cont_phone AS
    SELECT ac.activity_id AS amp_activity_id, COALESCE(((((SELECT amp_category_value.category_value FROM amp_category_value WHERE ((amp_category_value.id)::text = "substring"((cp.value)::text, 1, "position"((cp.value)::text, ' '::text)))))::text || ' '::text) || "substring"((cp.value)::text, "position"((cp.value)::text, ' '::text))), substr((cp.value)::text, "position"((cp.value)::text, ' '::text))) AS phone FROM amp_activity_contact ac, amp_contact_properties cp WHERE (((ac.contact_id = cp.contact_id) AND ((cp.name)::text = 'contact phone'::text)) AND ((ac.contact_type)::text = 'IMPL_EXEC_AGENCY_CONT'::text));


ALTER TABLE public.v_impl_ex_cont_phone OWNER TO amp;

CREATE VIEW v_impl_ex_cont_title AS
    SELECT ac.activity_id AS amp_activity_id, c.function FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'IMPL_EXEC_AGENCY_CONT'::text)) AND (btrim((c.function)::text) <> ' '::text));


ALTER TABLE public.v_impl_ex_cont_title OWNER TO amp;


CREATE VIEW v_implementation_level AS
    SELECT aac.amp_activity_id, acv.category_value AS name, aac.amp_categoryvalue_id AS level_code FROM amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac WHERE (((acv.id = aac.amp_categoryvalue_id) AND (acc.id = acv.amp_category_class_id)) AND ((acc.keyname)::text = 'implementation_level'::text));


ALTER TABLE public.v_implementation_level OWNER TO amp;


CREATE VIEW v_implementation_location AS
    SELECT aac.amp_activity_id, acv.category_value AS name, aac.amp_categoryvalue_id AS amp_status_id FROM amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac WHERE (((acv.id = aac.amp_categoryvalue_id) AND (acc.id = acv.amp_category_class_id)) AND ((acc.keyname)::text = 'implementation_location'::text));


ALTER TABLE public.v_implementation_location OWNER TO amp;


CREATE VIEW v_implementing_agency AS
    SELECT f.activity AS amp_activity_id, o.name, f.organisation AS amp_org_id FROM amp_org_role f, amp_organisation o, amp_role r WHERE (((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'IA'::text)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_implementing_agency OWNER TO amp;


CREATE VIEW v_implementing_agency_groups AS
    SELECT f.activity AS amp_activity_id, b.org_grp_name AS name, b.amp_org_grp_id FROM amp_org_role f, amp_organisation o, amp_role r, amp_org_group b WHERE ((((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'IA'::text)) AND (o.org_grp_id = b.amp_org_grp_id)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_implementing_agency_groups OWNER TO amp;

CREATE VIEW v_implementing_agency_info AS
    SELECT aor.activity AS amp_activity_id, aor.additional_info, aor.organisation AS amp_org_id FROM amp_org_role aor, amp_role r WHERE ((aor.role = r.amp_role_id) AND ((r.role_code)::text = 'IA'::text));


ALTER TABLE public.v_implementing_agency_info OWNER TO amp;

CREATE VIEW v_imputation AS
    SELECT a.amp_activity_id, i.code FROM amp_chapter c, amp_activity a, amp_imputation i WHERE (((a.chapter_code)::text = (c.code)::text) AND ((i.chapter_code)::text = (c.code)::text));


ALTER TABLE public.v_imputation OWNER TO amp;


CREATE VIEW v_indicator_actualvalue AS
    SELECT con.activity_id AS amp_activity_id, val.value AS name, ind.indicator_id AS amp_me_indicator_id FROM amp_indicator_connection con, amp_indicator ind, amp_indicator_values val WHERE ((((ind.indicator_id = con.indicator_id) AND (val.ind_connect_id = con.id)) AND (val.value_type = 1)) AND ((con.sub_clazz)::text = 'a'::text));


ALTER TABLE public.v_indicator_actualvalue OWNER TO amp;


CREATE VIEW v_indicator_basevalue AS
    SELECT con.activity_id AS amp_activity_id, val.value AS name, ind.indicator_id AS amp_me_indicator_id FROM amp_indicator_connection con, amp_indicator ind, amp_indicator_values val WHERE ((((ind.indicator_id = con.indicator_id) AND (val.ind_connect_id = con.id)) AND (val.value_type = 2)) AND ((con.sub_clazz)::text = 'a'::text));


ALTER TABLE public.v_indicator_basevalue OWNER TO amp;


CREATE VIEW v_indicator_description AS
    SELECT con.activity_id AS amp_activity_id, ind.description AS name, ind.indicator_id AS amp_me_indicator_id FROM amp_indicator_connection con, amp_indicator ind WHERE ((ind.indicator_id = con.indicator_id) AND ((con.sub_clazz)::text = 'a'::text));


ALTER TABLE public.v_indicator_description OWNER TO amp;


CREATE VIEW v_indicator_id AS
    SELECT con.activity_id AS amp_activity_id, ind.code AS name, ind.indicator_id AS amp_me_indicator_id FROM amp_indicator_connection con, amp_indicator ind WHERE ((ind.indicator_id = con.indicator_id) AND ((con.sub_clazz)::text = 'a'::text));


ALTER TABLE public.v_indicator_id OWNER TO amp;


CREATE VIEW v_indicator_name AS
    SELECT con.activity_id AS amp_activity_id, ind.name, ind.indicator_id AS amp_me_indicator_id FROM amp_indicator_connection con, amp_indicator ind WHERE ((ind.indicator_id = con.indicator_id) AND ((con.sub_clazz)::text = 'a'::text));


ALTER TABLE public.v_indicator_name OWNER TO amp;


CREATE VIEW v_indicator_targetvalue AS
    SELECT con.activity_id AS amp_activity_id, val.value AS name, ind.indicator_id AS amp_me_indicator_id FROM amp_indicator_connection con, amp_indicator ind, amp_indicator_values val WHERE ((((ind.indicator_id = con.indicator_id) AND (val.ind_connect_id = con.id)) AND (val.value_type = 0)) AND ((con.sub_clazz)::text = 'a'::text));


ALTER TABLE public.v_indicator_targetvalue OWNER TO amp;

CREATE VIEW v_institutions AS
    SELECT acc.amp_activity_id, acv.category_value AS name FROM amp_activities_categoryvalues acc, amp_category_value acv, amp_category_class ac WHERE (((acv.id = acc.amp_categoryvalue_id) AND (ac.id = acv.amp_category_class_id)) AND ((ac.keyname)::text = 'institutions'::text));


ALTER TABLE public.v_institutions OWNER TO amp;


CREATE VIEW v_issues AS
    SELECT ai.amp_activity_id, ai.name, ai.amp_issue_id FROM amp_issues ai, amp_activity a WHERE (ai.amp_activity_id = a.amp_activity_id) ORDER BY ai.amp_activity_id;


ALTER TABLE public.v_issues OWNER TO amp;


CREATE VIEW v_measures_taken AS
    SELECT ai.amp_activity_id, m.name, m.amp_measure_id FROM amp_activity a, amp_measure m, amp_issues ai WHERE ((ai.amp_activity_id = a.amp_activity_id) AND (ai.amp_issue_id = m.amp_issue_id)) ORDER BY ai.amp_activity_id;


ALTER TABLE public.v_measures_taken OWNER TO amp;

CREATE VIEW v_mode_of_payment AS
    SELECT a.amp_activity_id, val.category_value AS mode_of_payment_name, val.id AS mode_of_payment_code FROM amp_activity a, amp_funding fund, amp_category_value val, amp_category_class c WHERE ((((fund.amp_activity_id = a.amp_activity_id) AND (val.id = fund.mode_of_payment_category_va)) AND ((c.keyname)::text = 'mode_of_payment'::text)) AND (c.id = val.amp_category_class_id)) GROUP BY a.amp_activity_id, val.id, val.category_value ORDER BY a.amp_activity_id, val.category_value;


ALTER TABLE public.v_mode_of_payment OWNER TO amp;


CREATE VIEW v_mofed_cont_email AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS email FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'MOFED_CONT'::text)) AND ((cp.name)::text = 'contact email'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_mofed_cont_email OWNER TO amp;


CREATE VIEW v_mofed_cont_fax AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS fax FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'MOFED_CONT'::text)) AND ((cp.name)::text = 'contact fax'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_mofed_cont_fax OWNER TO amp;

CREATE VIEW v_mofed_cont_name AS
    SELECT ac.activity_id AS amp_activity_id, btrim((((c.name)::text || ' '::text) || (c.lastname)::text)) AS contact FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'MOFED_CONT'::text)) AND (btrim((((c.name)::text || ' '::text) || (c.lastname)::text)) <> ''::text));


ALTER TABLE public.v_mofed_cont_name OWNER TO amp;


CREATE VIEW v_mofed_cont_org AS
    SELECT ac.activity_id AS amp_activity_id, c.organisation_name AS org FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'MOFED_CONT'::text)) AND (btrim((c.organisation_name)::text) <> ' '::text)) UNION SELECT ac.activity_id AS amp_activity_id, org.name AS org FROM amp_activity_contact ac, amp_org_contact oc, amp_organisation org WHERE (((ac.contact_id = oc.contact_id) AND (oc.amp_org_id = org.amp_org_id)) AND ((ac.contact_type)::text = 'MOFED_CONT'::text));


ALTER TABLE public.v_mofed_cont_org OWNER TO amp;


CREATE VIEW v_mofed_cont_phone AS
    SELECT ac.activity_id AS amp_activity_id, COALESCE(((((SELECT amp_category_value.category_value FROM amp_category_value WHERE ((amp_category_value.id)::text = "substring"((cp.value)::text, 1, "position"((cp.value)::text, ' '::text)))))::text || ' '::text) || "substring"((cp.value)::text, "position"((cp.value)::text, ' '::text))), "substring"((cp.value)::text, "position"((cp.value)::text, ' '::text))) AS phone FROM amp_activity_contact ac, amp_contact_properties cp WHERE (((ac.contact_id = cp.contact_id) AND ((cp.name)::text = 'contact phone'::text)) AND ((ac.contact_type)::text = 'MOFED_CONT'::text));


ALTER TABLE public.v_mofed_cont_phone OWNER TO amp;

CREATE VIEW v_mofed_cont_title AS
    SELECT ac.activity_id AS amp_activity_id, c.function FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'MOFED_CONT'::text)) AND (btrim((c.function)::text) <> ' '::text));


ALTER TABLE public.v_mofed_cont_title OWNER TO amp;

CREATE VIEW v_nationalobjectives_all_level AS
    SELECT a.amp_activity_id, a.program_percentage, a.amp_program_id, b.name AS n1, b.level_ AS l1, b1.name AS n2, b1.level_ AS l2, b2.name AS n3, b2.level_ AS l3, b3.name AS n4, b3.level_ AS l4, b4.name AS n5, b4.level_ AS l5, b5.name AS n6, b5.level_ AS l6, b6.name AS n7, b6.level_ AS l7, b7.name AS n8, b7.level_ AS l8 FROM ((((((((amp_activity_program a JOIN amp_theme b ON ((a.amp_program_id = b.amp_theme_id))) LEFT JOIN amp_theme b1 ON ((b1.amp_theme_id = b.parent_theme_id))) LEFT JOIN amp_theme b2 ON ((b2.amp_theme_id = b1.parent_theme_id))) LEFT JOIN amp_theme b3 ON ((b3.amp_theme_id = b2.parent_theme_id))) LEFT JOIN amp_theme b4 ON ((b4.amp_theme_id = b3.parent_theme_id))) LEFT JOIN amp_theme b5 ON ((b5.amp_theme_id = b4.parent_theme_id))) LEFT JOIN amp_theme b6 ON ((b6.amp_theme_id = b5.parent_theme_id))) LEFT JOIN amp_theme b7 ON ((b7.amp_theme_id = b6.parent_theme_id))) WHERE (a.program_setting = 1);


ALTER TABLE public.v_nationalobjectives_all_level OWNER TO amp;

CREATE VIEW v_nationalobjectives_level_0 AS
    SELECT v_nationalobjectives_all_level.amp_activity_id, CASE 0 WHEN v_nationalobjectives_all_level.l1 THEN v_nationalobjectives_all_level.n1 WHEN v_nationalobjectives_all_level.l2 THEN v_nationalobjectives_all_level.n2 WHEN v_nationalobjectives_all_level.l3 THEN v_nationalobjectives_all_level.n3 WHEN v_nationalobjectives_all_level.l4 THEN v_nationalobjectives_all_level.n4 WHEN v_nationalobjectives_all_level.l5 THEN v_nationalobjectives_all_level.n5 WHEN v_nationalobjectives_all_level.l6 THEN v_nationalobjectives_all_level.n6 WHEN v_nationalobjectives_all_level.l7 THEN v_nationalobjectives_all_level.n7 WHEN v_nationalobjectives_all_level.l8 THEN v_nationalobjectives_all_level.n8 ELSE NULL::character varying END AS name, v_nationalobjectives_all_level.amp_program_id, v_nationalobjectives_all_level.program_percentage FROM v_nationalobjectives_all_level;


ALTER TABLE public.v_nationalobjectives_level_0 OWNER TO amp;


CREATE VIEW v_nationalobjectives_level_1 AS
    SELECT v_nationalobjectives_all_level.amp_activity_id, CASE 1 WHEN v_nationalobjectives_all_level.l1 THEN v_nationalobjectives_all_level.n1 WHEN v_nationalobjectives_all_level.l2 THEN v_nationalobjectives_all_level.n2 WHEN v_nationalobjectives_all_level.l3 THEN v_nationalobjectives_all_level.n3 WHEN v_nationalobjectives_all_level.l4 THEN v_nationalobjectives_all_level.n4 WHEN v_nationalobjectives_all_level.l5 THEN v_nationalobjectives_all_level.n5 WHEN v_nationalobjectives_all_level.l6 THEN v_nationalobjectives_all_level.n6 WHEN v_nationalobjectives_all_level.l7 THEN v_nationalobjectives_all_level.n7 WHEN v_nationalobjectives_all_level.l8 THEN v_nationalobjectives_all_level.n8 ELSE NULL::character varying END AS name, v_nationalobjectives_all_level.amp_program_id, v_nationalobjectives_all_level.program_percentage FROM v_nationalobjectives_all_level;


ALTER TABLE public.v_nationalobjectives_level_1 OWNER TO amp;


CREATE VIEW v_nationalobjectives_level_2 AS
    SELECT v_nationalobjectives_all_level.amp_activity_id, CASE 2 WHEN v_nationalobjectives_all_level.l1 THEN v_nationalobjectives_all_level.n1 WHEN v_nationalobjectives_all_level.l2 THEN v_nationalobjectives_all_level.n2 WHEN v_nationalobjectives_all_level.l3 THEN v_nationalobjectives_all_level.n3 WHEN v_nationalobjectives_all_level.l4 THEN v_nationalobjectives_all_level.n4 WHEN v_nationalobjectives_all_level.l5 THEN v_nationalobjectives_all_level.n5 WHEN v_nationalobjectives_all_level.l6 THEN v_nationalobjectives_all_level.n6 WHEN v_nationalobjectives_all_level.l7 THEN v_nationalobjectives_all_level.n7 WHEN v_nationalobjectives_all_level.l8 THEN v_nationalobjectives_all_level.n8 ELSE NULL::character varying END AS name, v_nationalobjectives_all_level.amp_program_id, v_nationalobjectives_all_level.program_percentage FROM v_nationalobjectives_all_level;


ALTER TABLE public.v_nationalobjectives_level_2 OWNER TO amp;


CREATE VIEW v_nationalobjectives_level_3 AS
    SELECT v_nationalobjectives_all_level.amp_activity_id, CASE 3 WHEN v_nationalobjectives_all_level.l1 THEN v_nationalobjectives_all_level.n1 WHEN v_nationalobjectives_all_level.l2 THEN v_nationalobjectives_all_level.n2 WHEN v_nationalobjectives_all_level.l3 THEN v_nationalobjectives_all_level.n3 WHEN v_nationalobjectives_all_level.l4 THEN v_nationalobjectives_all_level.n4 WHEN v_nationalobjectives_all_level.l5 THEN v_nationalobjectives_all_level.n5 WHEN v_nationalobjectives_all_level.l6 THEN v_nationalobjectives_all_level.n6 WHEN v_nationalobjectives_all_level.l7 THEN v_nationalobjectives_all_level.n7 WHEN v_nationalobjectives_all_level.l8 THEN v_nationalobjectives_all_level.n8 ELSE NULL::character varying END AS name, v_nationalobjectives_all_level.amp_program_id, v_nationalobjectives_all_level.program_percentage FROM v_nationalobjectives_all_level;


ALTER TABLE public.v_nationalobjectives_level_3 OWNER TO amp;

CREATE VIEW v_nationalobjectives_level_4 AS
    SELECT v_nationalobjectives_all_level.amp_activity_id, CASE 4 WHEN v_nationalobjectives_all_level.l1 THEN v_nationalobjectives_all_level.n1 WHEN v_nationalobjectives_all_level.l2 THEN v_nationalobjectives_all_level.n2 WHEN v_nationalobjectives_all_level.l3 THEN v_nationalobjectives_all_level.n3 WHEN v_nationalobjectives_all_level.l4 THEN v_nationalobjectives_all_level.n4 WHEN v_nationalobjectives_all_level.l5 THEN v_nationalobjectives_all_level.n5 WHEN v_nationalobjectives_all_level.l6 THEN v_nationalobjectives_all_level.n6 WHEN v_nationalobjectives_all_level.l7 THEN v_nationalobjectives_all_level.n7 WHEN v_nationalobjectives_all_level.l8 THEN v_nationalobjectives_all_level.n8 ELSE NULL::character varying END AS name, v_nationalobjectives_all_level.amp_program_id, v_nationalobjectives_all_level.program_percentage FROM v_nationalobjectives_all_level;


ALTER TABLE public.v_nationalobjectives_level_4 OWNER TO amp;


CREATE VIEW v_nationalobjectives_level_5 AS
    SELECT v_nationalobjectives_all_level.amp_activity_id, CASE 5 WHEN v_nationalobjectives_all_level.l1 THEN v_nationalobjectives_all_level.n1 WHEN v_nationalobjectives_all_level.l2 THEN v_nationalobjectives_all_level.n2 WHEN v_nationalobjectives_all_level.l3 THEN v_nationalobjectives_all_level.n3 WHEN v_nationalobjectives_all_level.l4 THEN v_nationalobjectives_all_level.n4 WHEN v_nationalobjectives_all_level.l5 THEN v_nationalobjectives_all_level.n5 WHEN v_nationalobjectives_all_level.l6 THEN v_nationalobjectives_all_level.n6 WHEN v_nationalobjectives_all_level.l7 THEN v_nationalobjectives_all_level.n7 WHEN v_nationalobjectives_all_level.l8 THEN v_nationalobjectives_all_level.n8 ELSE NULL::character varying END AS name, v_nationalobjectives_all_level.amp_program_id, v_nationalobjectives_all_level.program_percentage FROM v_nationalobjectives_all_level;


ALTER TABLE public.v_nationalobjectives_level_5 OWNER TO amp;


CREATE VIEW v_nationalobjectives_level_6 AS
    SELECT v_nationalobjectives_all_level.amp_activity_id, CASE 6 WHEN v_nationalobjectives_all_level.l1 THEN v_nationalobjectives_all_level.n1 WHEN v_nationalobjectives_all_level.l2 THEN v_nationalobjectives_all_level.n2 WHEN v_nationalobjectives_all_level.l3 THEN v_nationalobjectives_all_level.n3 WHEN v_nationalobjectives_all_level.l4 THEN v_nationalobjectives_all_level.n4 WHEN v_nationalobjectives_all_level.l5 THEN v_nationalobjectives_all_level.n5 WHEN v_nationalobjectives_all_level.l6 THEN v_nationalobjectives_all_level.n6 WHEN v_nationalobjectives_all_level.l7 THEN v_nationalobjectives_all_level.n7 WHEN v_nationalobjectives_all_level.l8 THEN v_nationalobjectives_all_level.n8 ELSE NULL::character varying END AS name, v_nationalobjectives_all_level.amp_program_id, v_nationalobjectives_all_level.program_percentage FROM v_nationalobjectives_all_level;


ALTER TABLE public.v_nationalobjectives_level_6 OWNER TO amp;


CREATE VIEW v_nationalobjectives_level_7 AS
    SELECT v_nationalobjectives_all_level.amp_activity_id, CASE 7 WHEN v_nationalobjectives_all_level.l1 THEN v_nationalobjectives_all_level.n1 WHEN v_nationalobjectives_all_level.l2 THEN v_nationalobjectives_all_level.n2 WHEN v_nationalobjectives_all_level.l3 THEN v_nationalobjectives_all_level.n3 WHEN v_nationalobjectives_all_level.l4 THEN v_nationalobjectives_all_level.n4 WHEN v_nationalobjectives_all_level.l5 THEN v_nationalobjectives_all_level.n5 WHEN v_nationalobjectives_all_level.l6 THEN v_nationalobjectives_all_level.n6 WHEN v_nationalobjectives_all_level.l7 THEN v_nationalobjectives_all_level.n7 WHEN v_nationalobjectives_all_level.l8 THEN v_nationalobjectives_all_level.n8 ELSE NULL::character varying END AS name, v_nationalobjectives_all_level.amp_program_id, v_nationalobjectives_all_level.program_percentage FROM v_nationalobjectives_all_level;


ALTER TABLE public.v_nationalobjectives_level_7 OWNER TO amp;


CREATE VIEW v_nationalobjectives_level_8 AS
    SELECT v_nationalobjectives_all_level.amp_activity_id, CASE 8 WHEN v_nationalobjectives_all_level.l1 THEN v_nationalobjectives_all_level.n1 WHEN v_nationalobjectives_all_level.l2 THEN v_nationalobjectives_all_level.n2 WHEN v_nationalobjectives_all_level.l3 THEN v_nationalobjectives_all_level.n3 WHEN v_nationalobjectives_all_level.l4 THEN v_nationalobjectives_all_level.n4 WHEN v_nationalobjectives_all_level.l5 THEN v_nationalobjectives_all_level.n5 WHEN v_nationalobjectives_all_level.l6 THEN v_nationalobjectives_all_level.n6 WHEN v_nationalobjectives_all_level.l7 THEN v_nationalobjectives_all_level.n7 WHEN v_nationalobjectives_all_level.l8 THEN v_nationalobjectives_all_level.n8 ELSE NULL::character varying END AS name, v_nationalobjectives_all_level.amp_program_id, v_nationalobjectives_all_level.program_percentage FROM v_nationalobjectives_all_level;


ALTER TABLE public.v_nationalobjectives_level_8 OWNER TO amp;


CREATE VIEW v_objectives AS
    SELECT amp_activity.amp_activity_id, btrim(dg_editor.body) AS ebody FROM amp_activity, dg_editor WHERE (amp_activity.objectives = (dg_editor.editor_key)::text) ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_objectives OWNER TO amp;


CREATE VIEW v_on_off_budget AS
    SELECT a.amp_activity_id, CASE WHEN (acv.id IS NULL) THEN 'Unallocated'::text ELSE (acv.category_value)::text END AS budget FROM (((amp_activities_categoryvalues aac JOIN amp_category_value acv ON ((aac.amp_categoryvalue_id = acv.id))) JOIN amp_category_class acc ON (((acc.id = acv.amp_category_class_id) AND ((acc.keyname)::text = 'activity_budget'::text)))) RIGHT JOIN amp_activity a ON ((a.amp_activity_id = aac.amp_activity_id))) ORDER BY a.amp_activity_id;


ALTER TABLE public.v_on_off_budget OWNER TO amp;

CREATE VIEW v_organization_projectid AS
    SELECT aaii.amp_activity_id, (((org.name)::text || ' -- '::text) || (aaii.internal_id)::text) AS name, aaii.amp_org_id FROM amp_activity_internal_id aaii, amp_organisation org WHERE (aaii.amp_org_id = org.amp_org_id);


ALTER TABLE public.v_organization_projectid OWNER TO amp;

CREATE VIEW v_physical_description AS
    SELECT a.amp_activity_id, a.description FROM amp_physical_performance a ORDER BY a.amp_activity_id;


ALTER TABLE public.v_physical_description OWNER TO amp;

CREATE VIEW v_physical_progress AS
    SELECT p.amp_activity_id, p.description, p.amp_pp_id FROM amp_physical_performance p ORDER BY p.reporting_date;


ALTER TABLE public.v_physical_progress OWNER TO amp;

CREATE VIEW v_physical_title AS
    SELECT a.amp_activity_id, a.description FROM amp_physical_performance a ORDER BY a.amp_activity_id;


ALTER TABLE public.v_physical_title OWNER TO amp;


CREATE VIEW v_pledges_aid_modality AS
    SELECT f.pledge_id, val.category_value AS name, f.aid_modality AS amp_modality_id FROM amp_funding_pledges_details f, amp_category_value val WHERE (f.aid_modality = val.id);


ALTER TABLE public.v_pledges_aid_modality OWNER TO amp;

CREATE VIEW v_pledges_contact1_address AS
    SELECT pf.id AS pledge_id, pf.contactaddress_1 AS address, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact1_address OWNER TO amp;


CREATE VIEW v_pledges_contact1_alternate AS
    SELECT pf.id AS pledge_id, pf.contactalternativename AS altername, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact1_alternate OWNER TO amp;

CREATE VIEW v_pledges_contact1_email AS
    SELECT pf.id AS pledge_id, pf.contactalternativeemail AS email, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact1_email OWNER TO amp;

CREATE VIEW v_pledges_contact1_ministry AS
    SELECT pf.id AS pledge_id, pf.contactministry AS ministry, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact1_ministry OWNER TO amp;

CREATE VIEW v_pledges_contact1_name AS
    SELECT pf.id AS pledge_id, pf.contactname AS name, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact1_name OWNER TO amp;

CREATE VIEW v_pledges_contact1_telephone AS
    SELECT pf.id AS pledge_id, pf.contacttelephone_1 AS telephone, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact1_telephone OWNER TO amp;

CREATE VIEW v_pledges_contact1_title AS
    SELECT pf.id AS pledge_id, pf.contacttitle AS title, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact1_title OWNER TO amp;


CREATE VIEW v_pledges_contact2_address AS
    SELECT pf.id AS pledge_id, pf.contactaddress_1 AS address, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact2_address OWNER TO amp;


CREATE VIEW v_pledges_contact2_alternate AS
    SELECT pf.id AS pledge_id, pf.contactalternativename_1 AS altername, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact2_alternate OWNER TO amp;


CREATE VIEW v_pledges_contact2_email AS
    SELECT pf.id AS pledge_id, pf.contactalternativeemail_1 AS email, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact2_email OWNER TO amp;

CREATE VIEW v_pledges_contact2_ministry AS
    SELECT pf.id AS pledge_id, pf.contactministry_1 AS ministry, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact2_ministry OWNER TO amp;


CREATE VIEW v_pledges_contact2_name AS
    SELECT pf.id AS pledge_id, pf.contactname_1 AS name, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact2_name OWNER TO amp;

CREATE VIEW v_pledges_contact2_telephone AS
    SELECT pf.id AS pledge_id, pf.contacttelephone_1 AS telephone, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact2_telephone OWNER TO amp;


CREATE VIEW v_pledges_contact2_title AS
    SELECT pf.id AS pledge_id, pf.contacttitle_1 AS title, pf.id AS contactname_id FROM amp_funding_pledges pf ORDER BY pf.id;


ALTER TABLE public.v_pledges_contact2_title OWNER TO amp;

CREATE VIEW v_pledges_date_hierarchy AS
    SELECT fd.year, pl.id AS pledgeid, fd.id AS amp_fund_detail_id FROM (amp_funding_pledges_details fd JOIN amp_funding_pledges pl ON ((fd.pledge_id = pl.id))) WHERE (pl.id = fd.pledge_id);


ALTER TABLE public.v_pledges_date_hierarchy OWNER TO amp;


CREATE VIEW v_pledges_districts AS
    SELECT ra.pledge_id, getlocationname(getlocationidbyimplloc(cvl.id, 'District'::character varying)) AS location_name, getlocationidbyimplloc(cvl.id, 'District'::character varying) AS location_id, sum(ra.location_percentage) AS location_percentage FROM (amp_funding_pledges_location ra JOIN amp_category_value_location cvl ON ((ra.location_id = cvl.id))) WHERE ((ra.location_id = cvl.id) AND (getlocationidbyimplloc(cvl.id, 'District'::character varying) IS NOT NULL)) GROUP BY ra.pledge_id, getlocationidbyimplloc(cvl.id, 'District'::character varying);


ALTER TABLE public.v_pledges_districts OWNER TO amp;

CREATE VIEW v_pledges_donor AS
    SELECT f.id AS pledge_id, o.name, f.amp_org_id AS amp_donor_org_id, o.org_grp_id, gr.org_type AS org_type_id FROM amp_funding_pledges f, amp_organisation o, amp_org_group gr WHERE ((f.amp_org_id = o.amp_org_id) AND (o.org_grp_id = gr.amp_org_grp_id)) ORDER BY f.id, o.name;


ALTER TABLE public.v_pledges_donor OWNER TO amp;


CREATE VIEW v_pledges_funding AS
    SELECT ap.id AS pledgeid, ap.title, COALESCE(ap.contactname, ''::character varying) AS contact_name, COALESCE(ap.contactemail, ''::character varying) AS contact_email, ((((apd.amount * COALESCE(fps.sector_percentage, (100)::double precision)) / (100)::double precision) * COALESCE(fpl.location_percentage, (100)::double precision)) / (100)::double precision) AS amount, apd.id AS amp_fund_detail_id, to_date(('02 Jan '::text || (apd.year)::text), 'DD Mon YYYY'::text) AS transaction_date, ac.currency_code, aorg.name AS org_name, catv.category_value AS aid_modality_name, catv1.category_value AS type_of_assistance_name, catv2.category_value AS pledge_type, s.name AS p_sectorname, fps.sector_percentage AS spercenatage, l.location_name AS region FROM ((((((((((amp_funding_pledges ap JOIN amp_funding_pledges_details apd ON ((ap.id = apd.pledge_id))) JOIN amp_category_value catv ON ((apd.aid_modality = catv.id))) JOIN amp_category_value catv1 ON ((apd.type_of_assistance = catv1.id))) JOIN amp_category_value catv2 ON ((apd.pledge_type = catv2.id))) JOIN amp_currency ac ON ((apd.currency = ac.amp_currency_id))) JOIN amp_organisation aorg ON ((ap.amp_org_id = aorg.amp_org_id))) JOIN amp_funding_pledges_sector fps ON ((ap.id = fps.pledge_id))) JOIN amp_sector s ON ((fps.amp_sector_id = s.amp_sector_id))) JOIN amp_funding_pledges_location fpl ON ((ap.id = fpl.pledge_id))) LEFT JOIN amp_category_value_location l ON ((fpl.location_id = l.id))) GROUP BY ap.id, apd.id, aorg.amp_org_id, s.amp_sector_id, l.id, ap.title, COALESCE(ap.contactname, ''::character varying), ap.contactemail, apd.amount, fps.sector_percentage, fpl.location_percentage, apd.year, ac.currency_code, aorg.name, catv.category_value, catv1.category_value, catv2.category_value, s.name, l.location_name;


ALTER TABLE public.v_pledges_funding OWNER TO amp;


CREATE VIEW v_pledges_funding_st AS
    SELECT f.id AS pledge_id, d.name AS donor_name, fd.pledge_id AS amp_fund_detail_id, afd.transaction_type, afd.adjustment_type, afd.transaction_date, afd.transaction_amount, c.currency_code, cval.id AS terms_assist_id, cval.category_value AS terms_assist_name, b.org_grp_name, ot.org_type AS donor_type_name, cval2.category_value AS financing_instrument_name, cval2.id AS financing_instrument_id, d.amp_org_id AS org_id, d.org_grp_id, ot.amp_org_type_id AS org_type_id FROM amp_funding_pledges f, amp_funding_pledges_details fd, amp_category_value cval, amp_currency c, amp_organisation d, amp_org_group b, amp_org_type ot, amp_category_value cval2, amp_funding_detail afd WHERE ((afd.pledge_id = f.id) AND (((((((cval2.id = fd.aid_modality) AND (c.amp_currency_id = fd.currency)) AND (f.id = fd.pledge_id)) AND (cval.id = fd.type_of_assistance)) AND (d.amp_org_id = f.amp_org_id)) AND (d.org_grp_id = b.amp_org_grp_id)) AND (ot.amp_org_type_id = b.org_type))) UNION ALL SELECT f.id AS pledge_id, d.name AS donor_name, fd.pledge_id AS amp_fund_detail_id, 7 AS transaction_type, 1 AS adjustment_type, to_date(('02 Jan '::text || (fd.year)::text), 'DD Mon YYYY'::text) AS transaction_date, fd.amount AS transaction_amount, c.currency_code, cval.id AS terms_assist_id, cval.category_value AS terms_assist_name, b.org_grp_name, ot.org_type AS donor_type_name, cval2.category_value AS financing_instrument_name, cval2.id AS financing_instrument_id, d.amp_org_id AS org_id, d.org_grp_id, ot.amp_org_type_id AS org_type_id FROM amp_funding_pledges f, amp_funding_pledges_details fd, amp_category_value cval, amp_currency c, amp_organisation d, amp_org_group b, amp_org_type ot, amp_category_value cval2 WHERE (((((((cval2.id = fd.aid_modality) AND (c.amp_currency_id = fd.currency)) AND (f.id = fd.pledge_id)) AND (cval.id = fd.type_of_assistance)) AND (d.amp_org_id = f.amp_org_id)) AND (d.org_grp_id = b.amp_org_grp_id)) AND (ot.amp_org_type_id = b.org_type)) ORDER BY 1;


ALTER TABLE public.v_pledges_funding_st OWNER TO amp;


CREATE VIEW v_pledges_programs AS
    SELECT p.id AS pledge_id, t.name, fp.amp_program_id, fp.program_percentage FROM ((amp_funding_pledges p JOIN amp_funding_pledges_program fp ON ((p.id = fp.pledge_id))) JOIN amp_theme t ON ((t.amp_theme_id = fp.amp_program_id))) ORDER BY p.id, t.name;


ALTER TABLE public.v_pledges_programs OWNER TO amp;


CREATE VIEW v_pledges_projects AS
    SELECT fp.id AS pledge_id, ac.name AS title, ac.amp_activity_id FROM (((amp_funding_pledges fp JOIN amp_funding_detail fd ON ((fd.pledge_id = fp.id))) JOIN amp_funding fu ON ((fd.amp_funding_id = fu.amp_funding_id))) JOIN amp_activity ac ON ((fu.amp_activity_id = ac.amp_activity_id))) GROUP BY ac.amp_activity_id, fp.id, ac.name;


ALTER TABLE public.v_pledges_projects OWNER TO amp;

CREATE VIEW v_pledges_regions AS
    SELECT ra.pledge_id, getlocationname(getlocationidbyimplloc(cvl.id, 'Region'::character varying)) AS location_name, getlocationidbyimplloc(cvl.id, 'Region'::character varying) AS location_id, sum(ra.location_percentage) AS location_percentage FROM (amp_funding_pledges_location ra JOIN amp_category_value_location cvl ON ((ra.location_id = cvl.id))) WHERE ((ra.location_id = cvl.id) AND (getlocationidbyimplloc(cvl.id, 'Region'::character varying) IS NOT NULL)) GROUP BY ra.pledge_id, getlocationidbyimplloc(cvl.id, 'Region'::character varying);


ALTER TABLE public.v_pledges_regions OWNER TO amp;

CREATE VIEW v_pledges_titles AS
    SELECT p.id AS pledge_id, val.category_value AS title, val.id AS title_id FROM (amp_funding_pledges p JOIN amp_category_value val ON ((p.title = val.id))) ORDER BY p.id;


ALTER TABLE public.v_pledges_titles OWNER TO amp;


CREATE VIEW v_pledges_type_of_assistance AS
    SELECT p.id AS pledge_id, val.category_value AS terms_assist_name, val.id AS terms_assist_code FROM ((amp_funding_pledges p JOIN amp_funding_pledges_details fund ON ((p.id = fund.pledge_id))) JOIN amp_category_value val ON ((fund.type_of_assistance = val.id))) GROUP BY p.id, val.id, val.category_value ORDER BY p.id, val.category_value;


ALTER TABLE public.v_pledges_type_of_assistance OWNER TO amp;


CREATE VIEW v_pledges_zones AS
    SELECT ra.pledge_id, getlocationname(getlocationidbyimplloc(cvl.id, 'Zone'::character varying)) AS location_name, getlocationidbyimplloc(cvl.id, 'Zone'::character varying) AS location_id, sum(ra.location_percentage) AS location_percentage FROM (amp_funding_pledges_location ra JOIN amp_category_value_location cvl ON ((ra.location_id = cvl.id))) WHERE ((ra.location_id = cvl.id) AND (getlocationidbyimplloc(cvl.id, 'Zone'::character varying) IS NOT NULL)) GROUP BY ra.pledge_id, getlocationidbyimplloc(cvl.id, 'Zone'::character varying);


ALTER TABLE public.v_pledges_zones OWNER TO amp;

CREATE VIEW v_primaryprogram AS
    SELECT a.amp_activity_id, t.name, ap.amp_program_id, ap.program_percentage FROM ((amp_activity a JOIN amp_activity_program ap ON (((a.amp_activity_id = ap.amp_activity_id) AND (ap.program_setting = 2)))) JOIN amp_theme t ON ((t.amp_theme_id = ap.amp_program_id))) ORDER BY a.amp_activity_id, t.name;


ALTER TABLE public.v_primaryprogram OWNER TO amp;


CREATE VIEW v_primaryprogram_all_level AS
    SELECT a.amp_activity_id, a.program_percentage, a.amp_program_id, b.name AS n1, b.level_ AS l1, b1.name AS n2, b1.level_ AS l2, b2.name AS n3, b2.level_ AS l3, b3.name AS n4, b3.level_ AS l4, b4.name AS n5, b4.level_ AS l5, b5.name AS n6, b5.level_ AS l6, b6.name AS n7, b6.level_ AS l7, b7.name AS n8, b7.level_ AS l8 FROM ((((((((amp_activity_program a JOIN amp_theme b ON ((a.amp_program_id = b.amp_theme_id))) LEFT JOIN amp_theme b1 ON ((b1.amp_theme_id = b.parent_theme_id))) LEFT JOIN amp_theme b2 ON ((b2.amp_theme_id = b1.parent_theme_id))) LEFT JOIN amp_theme b3 ON ((b3.amp_theme_id = b2.parent_theme_id))) LEFT JOIN amp_theme b4 ON ((b4.amp_theme_id = b3.parent_theme_id))) LEFT JOIN amp_theme b5 ON ((b5.amp_theme_id = b4.parent_theme_id))) LEFT JOIN amp_theme b6 ON ((b6.amp_theme_id = b5.parent_theme_id))) LEFT JOIN amp_theme b7 ON ((b7.amp_theme_id = b6.parent_theme_id))) WHERE (a.program_setting = 2);


ALTER TABLE public.v_primaryprogram_all_level OWNER TO amp;

CREATE VIEW v_primaryprogram_cached AS
    SELECT a.amp_activity_id, CASE WHEN (ap.program_setting = 3) THEN t.name ELSE NULL::character varying END AS name, ap.amp_program_id, ap.program_percentage, ap.program_setting FROM ((amp_activity a LEFT JOIN amp_activity_program ap ON ((a.amp_activity_id = ap.amp_activity_id))) LEFT JOIN amp_theme t ON ((t.amp_theme_id = ap.amp_program_id))) ORDER BY a.amp_activity_id, t.name;


ALTER TABLE public.v_primaryprogram_cached OWNER TO amp;

CREATE VIEW v_primaryprogram_level_0 AS
    SELECT v_primaryprogram_all_level.amp_activity_id, CASE 0 WHEN v_primaryprogram_all_level.l1 THEN v_primaryprogram_all_level.n1 WHEN v_primaryprogram_all_level.l2 THEN v_primaryprogram_all_level.n2 WHEN v_primaryprogram_all_level.l3 THEN v_primaryprogram_all_level.n3 WHEN v_primaryprogram_all_level.l4 THEN v_primaryprogram_all_level.n4 WHEN v_primaryprogram_all_level.l5 THEN v_primaryprogram_all_level.n5 WHEN v_primaryprogram_all_level.l6 THEN v_primaryprogram_all_level.n6 WHEN v_primaryprogram_all_level.l7 THEN v_primaryprogram_all_level.n7 WHEN v_primaryprogram_all_level.l8 THEN v_primaryprogram_all_level.n8 ELSE NULL::character varying END AS name, v_primaryprogram_all_level.amp_program_id, v_primaryprogram_all_level.program_percentage FROM v_primaryprogram_all_level;


ALTER TABLE public.v_primaryprogram_level_0 OWNER TO amp;


CREATE VIEW v_primaryprogram_level_1 AS
    SELECT v_primaryprogram_all_level.amp_activity_id, CASE 1 WHEN v_primaryprogram_all_level.l1 THEN v_primaryprogram_all_level.n1 WHEN v_primaryprogram_all_level.l2 THEN v_primaryprogram_all_level.n2 WHEN v_primaryprogram_all_level.l3 THEN v_primaryprogram_all_level.n3 WHEN v_primaryprogram_all_level.l4 THEN v_primaryprogram_all_level.n4 WHEN v_primaryprogram_all_level.l5 THEN v_primaryprogram_all_level.n5 WHEN v_primaryprogram_all_level.l6 THEN v_primaryprogram_all_level.n6 WHEN v_primaryprogram_all_level.l7 THEN v_primaryprogram_all_level.n7 WHEN v_primaryprogram_all_level.l8 THEN v_primaryprogram_all_level.n8 ELSE NULL::character varying END AS name, v_primaryprogram_all_level.amp_program_id, v_primaryprogram_all_level.program_percentage FROM v_primaryprogram_all_level;


ALTER TABLE public.v_primaryprogram_level_1 OWNER TO amp;


CREATE VIEW v_primaryprogram_level_2 AS
    SELECT v_primaryprogram_all_level.amp_activity_id, CASE 2 WHEN v_primaryprogram_all_level.l1 THEN v_primaryprogram_all_level.n1 WHEN v_primaryprogram_all_level.l2 THEN v_primaryprogram_all_level.n2 WHEN v_primaryprogram_all_level.l3 THEN v_primaryprogram_all_level.n3 WHEN v_primaryprogram_all_level.l4 THEN v_primaryprogram_all_level.n4 WHEN v_primaryprogram_all_level.l5 THEN v_primaryprogram_all_level.n5 WHEN v_primaryprogram_all_level.l6 THEN v_primaryprogram_all_level.n6 WHEN v_primaryprogram_all_level.l7 THEN v_primaryprogram_all_level.n7 WHEN v_primaryprogram_all_level.l8 THEN v_primaryprogram_all_level.n8 ELSE NULL::character varying END AS name, v_primaryprogram_all_level.amp_program_id, v_primaryprogram_all_level.program_percentage FROM v_primaryprogram_all_level;


ALTER TABLE public.v_primaryprogram_level_2 OWNER TO amp;


CREATE VIEW v_primaryprogram_level_3 AS
    SELECT v_primaryprogram_all_level.amp_activity_id, CASE 3 WHEN v_primaryprogram_all_level.l1 THEN v_primaryprogram_all_level.n1 WHEN v_primaryprogram_all_level.l2 THEN v_primaryprogram_all_level.n2 WHEN v_primaryprogram_all_level.l3 THEN v_primaryprogram_all_level.n3 WHEN v_primaryprogram_all_level.l4 THEN v_primaryprogram_all_level.n4 WHEN v_primaryprogram_all_level.l5 THEN v_primaryprogram_all_level.n5 WHEN v_primaryprogram_all_level.l6 THEN v_primaryprogram_all_level.n6 WHEN v_primaryprogram_all_level.l7 THEN v_primaryprogram_all_level.n7 WHEN v_primaryprogram_all_level.l8 THEN v_primaryprogram_all_level.n8 ELSE NULL::character varying END AS name, v_primaryprogram_all_level.amp_program_id, v_primaryprogram_all_level.program_percentage FROM v_primaryprogram_all_level;


ALTER TABLE public.v_primaryprogram_level_3 OWNER TO amp;


CREATE VIEW v_primaryprogram_level_4 AS
    SELECT v_primaryprogram_all_level.amp_activity_id, CASE 4 WHEN v_primaryprogram_all_level.l1 THEN v_primaryprogram_all_level.n1 WHEN v_primaryprogram_all_level.l2 THEN v_primaryprogram_all_level.n2 WHEN v_primaryprogram_all_level.l3 THEN v_primaryprogram_all_level.n3 WHEN v_primaryprogram_all_level.l4 THEN v_primaryprogram_all_level.n4 WHEN v_primaryprogram_all_level.l5 THEN v_primaryprogram_all_level.n5 WHEN v_primaryprogram_all_level.l6 THEN v_primaryprogram_all_level.n6 WHEN v_primaryprogram_all_level.l7 THEN v_primaryprogram_all_level.n7 WHEN v_primaryprogram_all_level.l8 THEN v_primaryprogram_all_level.n8 ELSE NULL::character varying END AS name, v_primaryprogram_all_level.amp_program_id, v_primaryprogram_all_level.program_percentage FROM v_primaryprogram_all_level;


ALTER TABLE public.v_primaryprogram_level_4 OWNER TO amp;


CREATE VIEW v_primaryprogram_level_5 AS
    SELECT v_primaryprogram_all_level.amp_activity_id, CASE 5 WHEN v_primaryprogram_all_level.l1 THEN v_primaryprogram_all_level.n1 WHEN v_primaryprogram_all_level.l2 THEN v_primaryprogram_all_level.n2 WHEN v_primaryprogram_all_level.l3 THEN v_primaryprogram_all_level.n3 WHEN v_primaryprogram_all_level.l4 THEN v_primaryprogram_all_level.n4 WHEN v_primaryprogram_all_level.l5 THEN v_primaryprogram_all_level.n5 WHEN v_primaryprogram_all_level.l6 THEN v_primaryprogram_all_level.n6 WHEN v_primaryprogram_all_level.l7 THEN v_primaryprogram_all_level.n7 WHEN v_primaryprogram_all_level.l8 THEN v_primaryprogram_all_level.n8 ELSE NULL::character varying END AS name, v_primaryprogram_all_level.amp_program_id, v_primaryprogram_all_level.program_percentage FROM v_primaryprogram_all_level;


ALTER TABLE public.v_primaryprogram_level_5 OWNER TO amp;


CREATE VIEW v_primaryprogram_level_6 AS
    SELECT v_primaryprogram_all_level.amp_activity_id, CASE 6 WHEN v_primaryprogram_all_level.l1 THEN v_primaryprogram_all_level.n1 WHEN v_primaryprogram_all_level.l2 THEN v_primaryprogram_all_level.n2 WHEN v_primaryprogram_all_level.l3 THEN v_primaryprogram_all_level.n3 WHEN v_primaryprogram_all_level.l4 THEN v_primaryprogram_all_level.n4 WHEN v_primaryprogram_all_level.l5 THEN v_primaryprogram_all_level.n5 WHEN v_primaryprogram_all_level.l6 THEN v_primaryprogram_all_level.n6 WHEN v_primaryprogram_all_level.l7 THEN v_primaryprogram_all_level.n7 WHEN v_primaryprogram_all_level.l8 THEN v_primaryprogram_all_level.n8 ELSE NULL::character varying END AS name, v_primaryprogram_all_level.amp_program_id, v_primaryprogram_all_level.program_percentage FROM v_primaryprogram_all_level;


ALTER TABLE public.v_primaryprogram_level_6 OWNER TO amp;


CREATE VIEW v_primaryprogram_level_7 AS
    SELECT v_primaryprogram_all_level.amp_activity_id, CASE 7 WHEN v_primaryprogram_all_level.l1 THEN v_primaryprogram_all_level.n1 WHEN v_primaryprogram_all_level.l2 THEN v_primaryprogram_all_level.n2 WHEN v_primaryprogram_all_level.l3 THEN v_primaryprogram_all_level.n3 WHEN v_primaryprogram_all_level.l4 THEN v_primaryprogram_all_level.n4 WHEN v_primaryprogram_all_level.l5 THEN v_primaryprogram_all_level.n5 WHEN v_primaryprogram_all_level.l6 THEN v_primaryprogram_all_level.n6 WHEN v_primaryprogram_all_level.l7 THEN v_primaryprogram_all_level.n7 WHEN v_primaryprogram_all_level.l8 THEN v_primaryprogram_all_level.n8 ELSE NULL::character varying END AS name, v_primaryprogram_all_level.amp_program_id, v_primaryprogram_all_level.program_percentage FROM v_primaryprogram_all_level;


ALTER TABLE public.v_primaryprogram_level_7 OWNER TO amp;

CREATE VIEW v_primaryprogram_level_8 AS
    SELECT v_primaryprogram_all_level.amp_activity_id, CASE 8 WHEN v_primaryprogram_all_level.l1 THEN v_primaryprogram_all_level.n1 WHEN v_primaryprogram_all_level.l2 THEN v_primaryprogram_all_level.n2 WHEN v_primaryprogram_all_level.l3 THEN v_primaryprogram_all_level.n3 WHEN v_primaryprogram_all_level.l4 THEN v_primaryprogram_all_level.n4 WHEN v_primaryprogram_all_level.l5 THEN v_primaryprogram_all_level.n5 WHEN v_primaryprogram_all_level.l6 THEN v_primaryprogram_all_level.n6 WHEN v_primaryprogram_all_level.l7 THEN v_primaryprogram_all_level.n7 WHEN v_primaryprogram_all_level.l8 THEN v_primaryprogram_all_level.n8 ELSE NULL::character varying END AS name, v_primaryprogram_all_level.amp_program_id, v_primaryprogram_all_level.program_percentage FROM v_primaryprogram_all_level;


ALTER TABLE public.v_primaryprogram_level_8 OWNER TO amp;


CREATE VIEW v_procurement_system AS
    SELECT acc.amp_activity_id, acv.category_value AS name FROM amp_activities_categoryvalues acc, amp_category_value acv, amp_category_class ac WHERE (((acv.id = acc.amp_categoryvalue_id) AND (ac.id = acv.amp_category_class_id)) AND ((ac.keyname)::text = 'procurement_system'::text));


ALTER TABLE public.v_procurement_system OWNER TO amp;


CREATE VIEW v_proj_coordr_cont_email AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS email FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'PROJ_COORDINATOR_CONT'::text)) AND ((cp.name)::text = 'contact email'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_proj_coordr_cont_email OWNER TO amp;


CREATE VIEW v_proj_coordr_cont_fax AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS fax FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'PROJ_COORDINATOR_CONT'::text)) AND ((cp.name)::text = 'contact fax'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_proj_coordr_cont_fax OWNER TO amp;


CREATE VIEW v_proj_coordr_cont_name AS
    SELECT ac.activity_id AS amp_activity_id, btrim((((c.name)::text || ' '::text) || (c.lastname)::text)) AS contact FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'PROJ_COORDINATOR_CONT'::text)) AND (btrim((((c.name)::text || ' '::text) || (c.lastname)::text)) <> ''::text));


ALTER TABLE public.v_proj_coordr_cont_name OWNER TO amp;


CREATE VIEW v_proj_coordr_cont_org AS
    SELECT ac.activity_id AS amp_activity_id, c.organisation_name AS org FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'PROJ_COORDINATOR_CONT'::text)) AND (btrim((c.organisation_name)::text) <> ' '::text)) UNION SELECT ac.activity_id AS amp_activity_id, org.name AS org FROM amp_activity_contact ac, amp_org_contact oc, amp_organisation org WHERE (((ac.contact_id = oc.contact_id) AND (oc.amp_org_id = org.amp_org_id)) AND ((ac.contact_type)::text = 'PROJ_COORDINATOR_CONT'::text));


ALTER TABLE public.v_proj_coordr_cont_org OWNER TO amp;

CREATE VIEW v_proj_coordr_cont_phone AS
    SELECT ac.activity_id AS amp_activity_id, COALESCE(((((SELECT amp_category_value.category_value FROM amp_category_value WHERE ((amp_category_value.id)::text = substr((cp.value)::text, 1, "position"((cp.value)::text, ' '::text)))))::text || ' '::text) || substr((cp.value)::text, "position"((cp.value)::text, ' '::text))), substr((cp.value)::text, "position"((cp.value)::text, ' '::text))) AS phone FROM amp_activity_contact ac, amp_contact_properties cp WHERE (((ac.contact_id = cp.contact_id) AND ((cp.name)::text = 'contact phone'::text)) AND ((ac.contact_type)::text = 'PROJ_COORDINATOR_CONT'::text));


ALTER TABLE public.v_proj_coordr_cont_phone OWNER TO amp;


CREATE VIEW v_proj_coordr_cont_title AS
    SELECT ac.activity_id AS amp_activity_id, c.function FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'PROJ_COORDINATOR_CONT'::text)) AND (btrim((c.function)::text) <> ' '::text));


ALTER TABLE public.v_proj_coordr_cont_title OWNER TO amp;


CREATE VIEW v_project_category AS
    SELECT aac.amp_activity_id, acv.category_value AS name, aac.amp_categoryvalue_id AS amp_category_id FROM amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac WHERE (((acv.id = aac.amp_categoryvalue_id) AND (acc.id = acv.amp_category_class_id)) AND ((acc.keyname)::text = 'project_category'::text));


ALTER TABLE public.v_project_category OWNER TO amp;


CREATE VIEW v_project_code AS
    SELECT a.amp_activity_id, a.project_code FROM amp_activity a WHERE (btrim((a.project_code)::text) <> ''::text);


ALTER TABLE public.v_project_code OWNER TO amp;


CREATE VIEW v_project_comments AS
    SELECT DISTINCT amp_activity.amp_activity_id, btrim(dg_editor.body) AS editorbodytrimmed FROM amp_activity, dg_editor WHERE (amp_activity.projectcomments = (dg_editor.editor_key)::text) ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_project_comments OWNER TO amp;

CREATE VIEW v_project_id AS
    SELECT i.amp_activity_id, ((((i.internal_id)::text || ' ('::text) || (o.name)::text) || ')'::text) FROM amp_organisation o, amp_activity_internal_id i WHERE (o.amp_org_id = i.amp_org_id);


ALTER TABLE public.v_project_id OWNER TO amp;

CREATE VIEW v_project_impl_unit AS
    SELECT acc.amp_activity_id, acv.category_value AS name, acv.id AS proj_impl_unit_id FROM amp_activities_categoryvalues acc, amp_category_value acv, amp_category_class ac WHERE (((acv.id = acc.amp_categoryvalue_id) AND (ac.id = acv.amp_category_class_id)) AND ((ac.keyname)::text = 'project_impl_unit'::text));


ALTER TABLE public.v_project_impl_unit OWNER TO amp;


CREATE VIEW v_proposed_completion_date AS
    SELECT amp_activity.amp_activity_id, amp_activity.proposed_completion_date FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_proposed_completion_date OWNER TO amp;

CREATE VIEW v_proposed_cost AS
    SELECT amp_activity.amp_activity_id, amp_activity.amp_activity_id AS object_id, amp_activity.amp_activity_id AS object_id2, amp_activity.proj_cost_amount AS transaction_amount, amp_activity.proj_cost_currcode AS currency_code, amp_activity.proj_cost_date AS transaction_date FROM amp_activity WHERE (amp_activity.proj_cost_amount IS NOT NULL);


ALTER TABLE public.v_proposed_cost OWNER TO amp;


CREATE VIEW v_proposed_start_date AS
    SELECT amp_activity.amp_activity_id, amp_activity.proposed_start_date FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_proposed_start_date OWNER TO amp;


CREATE VIEW v_purposes AS
    SELECT amp_activity.amp_activity_id, btrim(dg_editor.body) AS ebody FROM amp_activity, dg_editor WHERE (amp_activity.purpose = (dg_editor.editor_key)::text) ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_purposes OWNER TO amp;


CREATE VIEW v_regional_funding AS
    SELECT f.activity_id AS amp_activity_id, f.amp_regional_funding_id, f.amp_regional_funding_id AS amp_fund_detail_id, r.location_name AS region_name, f.transaction_type, f.adjustment_type, f.transaction_date, f.transaction_amount, c.currency_code, f.region_location_id AS region_id FROM amp_regional_funding f, amp_category_value_location r, amp_currency c, amp_category_value v WHERE ((((c.amp_currency_id = f.currency_id) AND (f.region_location_id = r.id)) AND (r.parent_category_value = v.id)) AND ((v.category_value)::text = 'Region'::text)) ORDER BY f.activity_id;


ALTER TABLE public.v_regional_funding OWNER TO amp;

CREATE VIEW v_regional_group AS
    SELECT f.activity AS amp_activity_id, o.name, f.organisation AS amp_org_id FROM amp_org_role f, amp_organisation o, amp_role r WHERE (((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'RG'::text)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_regional_group OWNER TO amp;


CREATE VIEW v_regional_group_info AS
    SELECT aor.activity AS amp_activity_id, aor.additional_info, aor.organisation AS amp_org_id FROM amp_org_role aor, amp_role r WHERE ((aor.role = r.amp_role_id) AND ((r.role_code)::text = 'RG'::text));


ALTER TABLE public.v_regional_group_info OWNER TO amp;


CREATE VIEW v_regions AS
    SELECT ra.amp_activity_id, l.region, l.region_location_id AS region_id, sum(ra.location_percentage) AS location_percentage FROM ((amp_activity_location ra JOIN amp_location l ON ((ra.amp_location_id = l.amp_location_id))) JOIN amp_category_value_location cvl ON ((l.location_id = cvl.id))) WHERE (l.region_location_id IS NOT NULL) GROUP BY ra.amp_activity_id, l.region_location_id, l.region ORDER BY ra.amp_activity_id, l.region;


ALTER TABLE public.v_regions OWNER TO amp;


CREATE VIEW v_regions_cached AS
    SELECT aa.amp_activity_id, al.region_location_id AS region_id, sum(lp.location_percentage) AS location_percentage, acvl.location_name AS region FROM (((amp_activity aa LEFT JOIN amp_activity_location lp ON ((aa.amp_activity_id = lp.amp_activity_id))) LEFT JOIN amp_location al ON ((lp.amp_location_id = al.amp_location_id))) LEFT JOIN amp_category_value_location acvl ON ((al.region_location_id = acvl.id))) GROUP BY aa.amp_activity_id, al.region_location_id, acvl.location_name ORDER BY aa.amp_activity_id, al.region_location_id;


ALTER TABLE public.v_regions_cached OWNER TO amp;



CREATE VIEW v_reporting_system AS
    SELECT acc.amp_activity_id, acv.category_value AS name FROM amp_activities_categoryvalues acc, amp_category_value acv, amp_category_class ac WHERE (((acv.id = acc.amp_categoryvalue_id) AND (ac.id = acv.amp_category_class_id)) AND ((ac.keyname)::text = 'reporting_system'::text));


ALTER TABLE public.v_reporting_system OWNER TO amp;


CREATE VIEW v_responsible_org_groups AS
    SELECT f.activity AS amp_activity_id, b.org_grp_name AS name, b.amp_org_grp_id FROM amp_org_role f, amp_organisation o, amp_role r, amp_org_group b WHERE ((((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'RO'::text)) AND (o.org_grp_id = b.amp_org_grp_id)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_responsible_org_groups OWNER TO amp;


CREATE VIEW v_responsible_org_info AS
    SELECT aor.activity AS amp_activity_id, aor.additional_info, aor.organisation AS amp_org_id FROM amp_org_role aor, amp_role r WHERE ((aor.role = r.amp_role_id) AND ((r.role_code)::text = 'RO'::text));


ALTER TABLE public.v_responsible_org_info OWNER TO amp;



CREATE VIEW v_responsible_organisation AS
    SELECT f.activity AS amp_activity_id, o.name, f.organisation AS amp_org_id FROM amp_org_role f, amp_organisation o, amp_role r WHERE (((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'RO'::text)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_responsible_organisation OWNER TO amp;


CREATE VIEW v_results AS
    SELECT amp_activity.amp_activity_id, btrim(dg_editor.body) AS ebody FROM amp_activity, dg_editor WHERE (amp_activity.results = (dg_editor.editor_key)::text) ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_results OWNER TO amp;


CREATE VIEW v_secondary_sectors AS
    SELECT sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id)) AS sectorname, getparentsectorid(s.amp_sector_id) AS amp_sector_id, sum(sa.sector_percentage) AS sector_percentage, s.amp_sec_scheme_id AS amp_sector_scheme_id FROM amp_activity_sector sa, amp_sector s, amp_sector_scheme ss WHERE (((s.amp_sector_id = sa.amp_sector_id) AND (s.amp_sec_scheme_id IN (SELECT amp_classification_config.classification_id FROM amp_classification_config WHERE ((amp_classification_config.name)::text = 'Secondary'::text)))) AND (s.amp_sec_scheme_id = ss.amp_sec_scheme_id)) GROUP BY sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id)), getparentsectorid(s.amp_sector_id), s.amp_sec_scheme_id ORDER BY sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id));


ALTER TABLE public.v_secondary_sectors OWNER TO amp;


CREATE VIEW v_secondary_sub_sectors AS
    SELECT sa.amp_activity_id, getsectorname(getparentsubsectorid(s.amp_sector_id)) AS sectorname, getparentsubsectorid(s.amp_sector_id) AS amp_sector_id, sum(sa.sector_percentage) AS sector_percentage, s.amp_sec_scheme_id, ss.sec_scheme_name FROM (((amp_sector_scheme ss JOIN amp_classification_config cc ON ((((cc.name)::text = 'Secondary'::text) AND (cc.classification_id = ss.amp_sec_scheme_id)))) JOIN amp_sector s ON (((s.amp_sec_scheme_id = ss.amp_sec_scheme_id) AND (getsectordepth(s.amp_sector_id) >= 1)))) JOIN amp_activity_sector sa ON (((sa.amp_sector_id = s.amp_sector_id) AND (sa.classification_config_id = cc.id)))) GROUP BY sa.amp_activity_id, getparentsubsectorid(s.amp_sector_id), s.amp_sec_scheme_id, ss.sec_scheme_name ORDER BY sa.amp_activity_id, getsectorname(getparentsubsectorid(s.amp_sector_id));


ALTER TABLE public.v_secondary_sub_sectors OWNER TO amp;

CREATE VIEW v_secondary_sub_sub_sectors AS
    SELECT a.amp_activity_id, s.name, s.amp_sector_id, a.sector_percentage FROM amp_activity_sector a, amp_sector s WHERE (((a.amp_sector_id = s.amp_sector_id) AND (getsectordepth(s.amp_sector_id) = 2)) AND (s.amp_sec_scheme_id IN (SELECT c.classification_id FROM amp_classification_config c WHERE ((c.name)::text = 'Secondary'::text)))) ORDER BY a.amp_activity_id, s.name;


ALTER TABLE public.v_secondary_sub_sub_sectors OWNER TO amp;


CREATE VIEW v_secondaryprogram AS
    SELECT a.amp_activity_id, t.name, ap.amp_program_id, ap.program_percentage FROM ((amp_activity a JOIN amp_activity_program ap ON (((a.amp_activity_id = ap.amp_activity_id) AND (ap.program_setting = 3)))) JOIN amp_theme t ON ((t.amp_theme_id = ap.amp_program_id))) ORDER BY a.amp_activity_id, t.name;


ALTER TABLE public.v_secondaryprogram OWNER TO amp;


CREATE VIEW v_secondaryprogram_all_level AS
    SELECT a.amp_activity_id, a.program_percentage, b.amp_theme_id AS amp_program_id1, b.name AS n1, b.level_ AS l1, b1.amp_theme_id AS amp_program_id2, b1.name AS n2, b1.level_ AS l2, b2.amp_theme_id AS amp_program_id3, b2.name AS n3, b2.level_ AS l3, b3.amp_theme_id AS amp_program_id4, b3.name AS n4, b3.level_ AS l4, b4.amp_theme_id AS amp_program_id5, b4.name AS n5, b4.level_ AS l5, b5.amp_theme_id AS amp_program_id6, b5.name AS n6, b5.level_ AS l6, b6.amp_theme_id AS amp_program_id7, b6.name AS n7, b6.level_ AS l7, b7.amp_theme_id AS amp_program_id8, b7.name AS n8, b7.level_ AS l8 FROM ((((((((amp_activity_program a JOIN amp_theme b ON ((a.amp_program_id = b.amp_theme_id))) LEFT JOIN amp_theme b1 ON ((b1.amp_theme_id = b.parent_theme_id))) LEFT JOIN amp_theme b2 ON ((b2.amp_theme_id = b1.parent_theme_id))) LEFT JOIN amp_theme b3 ON ((b3.amp_theme_id = b2.parent_theme_id))) LEFT JOIN amp_theme b4 ON ((b4.amp_theme_id = b3.parent_theme_id))) LEFT JOIN amp_theme b5 ON ((b5.amp_theme_id = b4.parent_theme_id))) LEFT JOIN amp_theme b6 ON ((b6.amp_theme_id = b5.parent_theme_id))) LEFT JOIN amp_theme b7 ON ((b7.amp_theme_id = b6.parent_theme_id))) WHERE (getprogramsettingid(a.amp_program_id) = 3);


ALTER TABLE public.v_secondaryprogram_all_level OWNER TO amp;


CREATE VIEW v_secondaryprogram_cached AS
    SELECT a.amp_activity_id, CASE ap.program_setting WHEN 3 THEN t.name ELSE NULL::character varying END AS name, ap.amp_program_id, ap.program_percentage, ap.program_setting FROM ((amp_activity a LEFT JOIN amp_activity_program ap ON ((a.amp_activity_id = ap.amp_activity_id))) LEFT JOIN amp_theme t ON ((t.amp_theme_id = ap.amp_program_id))) ORDER BY a.amp_activity_id, t.name;


ALTER TABLE public.v_secondaryprogram_cached OWNER TO amp;


CREATE VIEW v_secondaryprogram_level_0 AS
    SELECT v_secondaryprogram_all_level.amp_activity_id, CASE 0 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END AS name, CASE 0 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END AS amp_program_id, sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage FROM v_secondaryprogram_all_level GROUP BY v_secondaryprogram_all_level.amp_activity_id, CASE 0 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END, CASE 0 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END HAVING (CASE 0 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END IS NOT NULL);


ALTER TABLE public.v_secondaryprogram_level_0 OWNER TO amp;


CREATE VIEW v_secondaryprogram_level_1 AS
    SELECT v_secondaryprogram_all_level.amp_activity_id, CASE 1 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END AS name, CASE 1 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END AS amp_program_id, sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage FROM v_secondaryprogram_all_level GROUP BY v_secondaryprogram_all_level.amp_activity_id, CASE 1 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END, CASE 1 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END HAVING (CASE 1 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END IS NOT NULL);


ALTER TABLE public.v_secondaryprogram_level_1 OWNER TO amp;


CREATE VIEW v_secondaryprogram_level_2 AS
    SELECT v_secondaryprogram_all_level.amp_activity_id, CASE 2 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END AS name, CASE 2 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END AS amp_program_id, sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage FROM v_secondaryprogram_all_level GROUP BY v_secondaryprogram_all_level.amp_activity_id, CASE 2 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END, CASE 2 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END HAVING (CASE 2 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END IS NOT NULL);


ALTER TABLE public.v_secondaryprogram_level_2 OWNER TO amp;


CREATE VIEW v_secondaryprogram_level_3 AS
    SELECT v_secondaryprogram_all_level.amp_activity_id, CASE 3 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END AS name, CASE 3 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END AS amp_program_id, sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage FROM v_secondaryprogram_all_level GROUP BY v_secondaryprogram_all_level.amp_activity_id, CASE 3 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END, CASE 3 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END HAVING (CASE 3 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END IS NOT NULL);


ALTER TABLE public.v_secondaryprogram_level_3 OWNER TO amp;


CREATE VIEW v_secondaryprogram_level_4 AS
    SELECT v_secondaryprogram_all_level.amp_activity_id, CASE 4 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END AS name, CASE 4 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END AS amp_program_id, sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage FROM v_secondaryprogram_all_level GROUP BY v_secondaryprogram_all_level.amp_activity_id, CASE 4 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END, CASE 4 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END HAVING (CASE 4 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END IS NOT NULL);


ALTER TABLE public.v_secondaryprogram_level_4 OWNER TO amp;


CREATE VIEW v_secondaryprogram_level_5 AS
    SELECT v_secondaryprogram_all_level.amp_activity_id, CASE 5 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END AS name, CASE 5 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END AS amp_program_id, sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage FROM v_secondaryprogram_all_level GROUP BY v_secondaryprogram_all_level.amp_activity_id, CASE 5 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END, CASE 5 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END HAVING (CASE 5 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END IS NOT NULL);


ALTER TABLE public.v_secondaryprogram_level_5 OWNER TO amp;


CREATE VIEW v_secondaryprogram_level_6 AS
    SELECT v_secondaryprogram_all_level.amp_activity_id, CASE 6 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END AS name, CASE 6 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END AS amp_program_id, sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage FROM v_secondaryprogram_all_level GROUP BY v_secondaryprogram_all_level.amp_activity_id, CASE 6 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END, CASE 6 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END HAVING (CASE 6 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END IS NOT NULL);


ALTER TABL

CREATE VIEW v_secondaryprogram_level_7 AS
    SELECT v_secondaryprogram_all_level.amp_activity_id, CASE 7 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END AS name, CASE 7 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END AS amp_program_id, sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage FROM v_secondaryprogram_all_level GROUP BY v_secondaryprogram_all_level.amp_activity_id, CASE 7 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END, CASE 7 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END HAVING (CASE 7 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END IS NOT NULL);


ALTER TABLE public.v_secondaryprogram_level_7 OWNER TO amp;


CREATE VIEW v_secondaryprogram_level_8 AS
    SELECT v_secondaryprogram_all_level.amp_activity_id, CASE 8 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END AS name, CASE 8 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END AS amp_program_id, sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage FROM v_secondaryprogram_all_level GROUP BY v_secondaryprogram_all_level.amp_activity_id, CASE 8 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END, CASE 8 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.amp_program_id1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.amp_program_id2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.amp_program_id3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.amp_program_id4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.amp_program_id5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.amp_program_id6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.amp_program_id7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.amp_program_id8 ELSE NULL::bigint END HAVING (CASE 8 WHEN v_secondaryprogram_all_level.l1 THEN v_secondaryprogram_all_level.n1 WHEN v_secondaryprogram_all_level.l2 THEN v_secondaryprogram_all_level.n2 WHEN v_secondaryprogram_all_level.l3 THEN v_secondaryprogram_all_level.n3 WHEN v_secondaryprogram_all_level.l4 THEN v_secondaryprogram_all_level.n4 WHEN v_secondaryprogram_all_level.l5 THEN v_secondaryprogram_all_level.n5 WHEN v_secondaryprogram_all_level.l6 THEN v_secondaryprogram_all_level.n6 WHEN v_secondaryprogram_all_level.l7 THEN v_secondaryprogram_all_level.n7 WHEN v_secondaryprogram_all_level.l8 THEN v_secondaryprogram_all_level.n8 ELSE NULL::character varying END IS NOT NULL);


ALTER TABLE public.v_secondaryprogram_level_8 OWNER TO amp;


CREATE VIEW v_sect_min_cont_email AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS email FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'SECTOR_MINISTRY_CONT'::text)) AND ((cp.name)::text = 'contact email'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_sect_min_cont_email OWNER TO amp;


CREATE VIEW v_sect_min_cont_fax AS
    SELECT ac.activity_id AS amp_activity_id, cp.value AS fax FROM amp_activity_contact ac, amp_contact_properties cp WHERE ((((ac.contact_id = cp.contact_id) AND ((ac.contact_type)::text = 'SECTOR_MINISTRY_CONT'::text)) AND ((cp.name)::text = 'contact fax'::text)) AND (btrim((cp.value)::text) <> ''::text));


ALTER TABLE public.v_sect_min_cont_fax OWNER TO amp;

CREATE VIEW v_sect_min_cont_name AS
    SELECT ac.activity_id AS amp_activity_id, btrim((((c.name)::text || ' '::text) || (c.lastname)::text)) AS contact FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'SECTOR_MINISTRY_CONT'::text)) AND (btrim((((c.name)::text || ' '::text) || (c.lastname)::text)) <> ''::text));


ALTER TABLE public.v_sect_min_cont_name OWNER TO amp;


CREATE VIEW v_sect_min_cont_org AS
    SELECT ac.activity_id AS amp_activity_id, c.organisation_name AS org FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'SECTOR_MINISTRY_CONT'::text)) AND (btrim((c.organisation_name)::text) <> ' '::text)) UNION SELECT ac.activity_id AS amp_activity_id, org.name AS org FROM amp_activity_contact ac, amp_org_contact oc, amp_organisation org WHERE (((ac.contact_id = oc.contact_id) AND (oc.amp_org_id = org.amp_org_id)) AND ((ac.contact_type)::text = 'SECTOR_MINISTRY_CONT'::text));


ALTER TABLE public.v_sect_min_cont_org OWNER TO amp;


CREATE VIEW v_sect_min_cont_phone AS
    SELECT ac.activity_id AS amp_activity_id, (COALESCE(((((SELECT amp_category_value.category_value FROM amp_category_value WHERE ((amp_category_value.id)::text = substr((cp.value)::text, 1, "position"((cp.value)::text, ' '::text)))))::text || ' '::text) || "substring"((cp.value)::text, "position"((cp.value)::text, ' '::text)))) || "substring"((cp.value)::text, "position"((cp.value)::text, ' '::text))) AS phone FROM amp_activity_contact ac, amp_contact_properties cp WHERE (((ac.contact_id = cp.contact_id) AND ((cp.name)::text = 'contact phone'::text)) AND ((ac.contact_type)::text = 'SECTOR_MINISTRY_CONT'::text));


ALTER TABLE public.v_sect_min_cont_phone OWNER TO amp;


CREATE VIEW v_sect_min_cont_title AS
    SELECT ac.activity_id AS amp_activity_id, c.function FROM amp_activity_contact ac, amp_contact c WHERE (((ac.contact_id = c.contact_id) AND ((ac.contact_type)::text = 'SECTOR_MINISTRY_CONT'::text)) AND (btrim((c.function)::text) <> ' '::text));


ALTER TABLE public.v_sect_min_cont_title OWNER TO amp;


CREATE VIEW v_sector_group AS
    SELECT f.activity AS amp_activity_id, o.name, f.organisation AS amp_org_id FROM amp_org_role f, amp_organisation o, amp_role r WHERE (((f.organisation = o.amp_org_id) AND (f.role = r.amp_role_id)) AND ((r.role_code)::text = 'SG'::text)) ORDER BY f.activity, o.name;


ALTER TABLE public.v_sector_group OWNER TO amp;


CREATE VIEW v_sector_group_info AS
    SELECT aor.activity AS amp_activity_id, aor.additional_info, aor.organisation AS amp_org_id FROM amp_org_role aor, amp_role r WHERE ((aor.role = r.amp_role_id) AND ((r.role_code)::text = 'SG'::text));


ALTER TABLE public.v_sector_group_info OWNER TO amp;


CREATE VIEW v_sectors AS
    SELECT sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id)) AS sectorname, getparentsectorid(s.amp_sector_id) AS amp_sector_id, sum(sa.sector_percentage) AS sector_percentage, s.amp_sec_scheme_id AS amp_sector_scheme_id, ss.sec_scheme_name FROM (((amp_sector_scheme ss JOIN amp_classification_config cc ON ((((cc.name)::text = 'Primary'::text) AND (cc.classification_id = ss.amp_sec_scheme_id)))) JOIN amp_sector s ON ((s.amp_sec_scheme_id = ss.amp_sec_scheme_id))) JOIN amp_activity_sector sa ON (((sa.amp_sector_id = s.amp_sector_id) AND (sa.classification_config_id = cc.id)))) GROUP BY sa.amp_activity_id, getparentsectorid(s.amp_sector_id), s.amp_sec_scheme_id, ss.sec_scheme_name ORDER BY sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id));


ALTER TABLE public.v_sectors OWNER TO amp;


CREATE VIEW v_sectors_cached AS
    SELECT sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id)) AS sectorname, getparentsectorid(s.amp_sector_id) AS amp_sector_id, s.amp_sec_scheme_id AS amp_sector_scheme_id, ss.sec_scheme_name, sa.sector_percentage, ((fd.transaction_amount * sa.sector_percentage) / (100)::double precision) AS transaction_amount, fd.transaction_type, fd.adjustment_type, fd.transaction_date, fd.fixed_exchange_rate, c.currency_code FROM ((amp_funding_detail fd LEFT JOIN ((((amp_sector_scheme ss JOIN amp_classification_config cc ON (((cc.name)::text = 'Primary'::text))) JOIN amp_sector s ON ((s.amp_sec_scheme_id = ss.amp_sec_scheme_id))) JOIN amp_activity_sector sa ON ((sa.amp_sector_id = s.amp_sector_id))) JOIN amp_funding f ON ((sa.amp_activity_id = f.amp_activity_id))) ON ((f.amp_funding_id = fd.amp_funding_id))) JOIN amp_currency c ON ((fd.amp_currency_id = c.amp_currency_id))) WHERE ((cc.classification_id = ss.amp_sec_scheme_id) AND (sa.classification_config_id = cc.id)) GROUP BY sa.amp_activity_id, getparentsectorid(s.amp_sector_id), fd.transaction_type, fd.amp_fund_detail_id, f.amp_funding_id, fd.adjustment_type, s.sector_code, s.amp_sec_scheme_id, ss.sec_scheme_name, sa.sector_percentage, fd.transaction_amount, fd.transaction_date, fd.fixed_exchange_rate, c.currency_code ORDER BY sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id)), fd.transaction_type, f.amp_funding_id;


ALTER TABLE public.v_sectors_cached OWNER TO amp;


CREATE VIEW v_status AS
    SELECT aac.amp_activity_id, acv.category_value AS name, aac.amp_categoryvalue_id AS amp_status_id FROM amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac WHERE (((acv.id = aac.amp_categoryvalue_id) AND (acc.id = acv.amp_category_class_id)) AND ((acc.keyname)::text = 'activity_status'::text));


ALTER TABLE public.v_status OWNER TO amp;


CREATE VIEW v_sub_sectors AS
    SELECT sa.amp_activity_id, getsectorname(getparentsubsectorid(s.amp_sector_id)) AS sectorname, getparentsubsectorid(s.amp_sector_id) AS amp_sector_id, sum(sa.sector_percentage) AS sector_percentage, s.amp_sec_scheme_id, ss.sec_scheme_name FROM (((amp_sector_scheme ss JOIN amp_classification_config cc ON ((((cc.name)::text = 'Primary'::text) AND (cc.classification_id = ss.amp_sec_scheme_id)))) JOIN amp_sector s ON (((s.amp_sec_scheme_id = ss.amp_sec_scheme_id) AND (getsectordepth(s.amp_sector_id) >= 1)))) JOIN amp_activity_sector sa ON (((sa.amp_sector_id = s.amp_sector_id) AND (sa.classification_config_id = cc.id)))) GROUP BY sa.amp_activity_id, getparentsubsectorid(s.amp_sector_id), s.amp_sec_scheme_id, ss.sec_scheme_name ORDER BY sa.amp_activity_id, getsectorname(getparentsubsectorid(s.amp_sector_id));


ALTER TABLE public.v_sub_sectors OWNER TO amp;


CREATE VIEW v_sub_sub_sectors AS
    SELECT a.amp_activity_id, s.name, s.amp_sector_id, a.sector_percentage FROM amp_activity_sector a, amp_sector s WHERE (((a.amp_sector_id = s.amp_sector_id) AND (getsectordepth(s.amp_sector_id) = 2)) AND (s.amp_sec_scheme_id IN (SELECT c.classification_id FROM amp_classification_config c WHERE ((c.name)::text = 'Primary'::text)))) ORDER BY a.amp_activity_id, s.name;


ALTER TABLE public.v_sub_sub_sectors OWNER TO amp;


CREATE VIEW v_teams AS
    SELECT a.amp_activity_id, t.name, t.amp_team_id FROM amp_activity a, amp_team t WHERE (a.amp_team_id = t.amp_team_id) ORDER BY a.amp_activity_id, t.amp_team_id;


ALTER TABLE public.v_teams OWNER TO amp;


CREATE VIEW v_terms_assist AS
    SELECT a.amp_activity_id, val.category_value AS terms_assist_name, val.id AS terms_assist_code FROM amp_activity a, amp_funding fund, amp_category_value val WHERE ((fund.amp_activity_id = a.amp_activity_id) AND (val.id = fund.type_of_assistance_category_va)) GROUP BY a.amp_activity_id, val.id, val.category_value ORDER BY a.amp_activity_id, val.category_value;


ALTER TABLE public.v_terms_assist OWNER TO amp;


CREATE VIEW v_tertiary_sectors AS
    SELECT sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id)) AS sectorname, getparentsectorid(s.amp_sector_id) AS amp_sector_id, sum(sa.sector_percentage) AS sector_percentage, s.amp_sec_scheme_id AS amp_sector_scheme_id FROM amp_activity_sector sa, amp_sector s, amp_sector_scheme ss WHERE (((s.amp_sector_id = sa.amp_sector_id) AND (s.amp_sec_scheme_id IN (SELECT amp_classification_config.classification_id FROM amp_classification_config WHERE ((amp_classification_config.name)::text = 'Tertiary'::text)))) AND (s.amp_sec_scheme_id = ss.amp_sec_scheme_id)) GROUP BY sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id)), s.amp_sector_id, s.amp_sec_scheme_id ORDER BY sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id));


ALTER TABLE public.v_tertiary_sectors OWNER TO amp;


CREATE VIEW v_tertiary_sub_sectors AS
    SELECT sa.amp_activity_id, getsectorname(getparentsubsectorid(s.amp_sector_id)) AS sectorname, getparentsubsectorid(s.amp_sector_id) AS amp_sector_id, sum(sa.sector_percentage) AS sector_percentage, s.amp_sec_scheme_id, ss.sec_scheme_name FROM (((amp_sector_scheme ss JOIN amp_classification_config cc ON ((((cc.name)::text = 'Tertiary'::text) AND (cc.classification_id = ss.amp_sec_scheme_id)))) JOIN amp_sector s ON (((s.amp_sec_scheme_id = ss.amp_sec_scheme_id) AND (getsectordepth(s.amp_sector_id) >= 1)))) JOIN amp_activity_sector sa ON (((sa.amp_sector_id = s.amp_sector_id) AND (sa.classification_config_id = cc.id)))) GROUP BY sa.amp_activity_id, getparentsubsectorid(s.amp_sector_id), s.amp_sec_scheme_id, ss.sec_scheme_name ORDER BY sa.amp_activity_id, getsectorname(getparentsubsectorid(s.amp_sector_id));


ALTER TABLE public.v_tertiary_sub_sectors OWNER TO amp;


CREATE VIEW v_tertiary_sub_sub_sectors AS
    SELECT a.amp_activity_id, s.name, s.amp_sector_id, a.sector_percentage FROM amp_activity_sector a, amp_sector s WHERE (((a.amp_sector_id = s.amp_sector_id) AND (getsectordepth(s.amp_sector_id) = 2)) AND (s.amp_sec_scheme_id IN (SELECT c.classification_id FROM amp_classification_config c WHERE ((c.name)::text = 'Tertiary'::text)))) ORDER BY a.amp_activity_id, s.name;


ALTER TABLE public.v_tertiary_sub_sub_sectors OWNER TO amp;


CREATE VIEW v_titles AS
    SELECT amp_activity.amp_activity_id, amp_activity.name, amp_activity.amp_activity_id AS title_id, amp_activity.draft, amp_activity.approval_status AS status FROM amp_activity ORDER BY amp_activity.amp_activity_id;


ALTER TABLE public.v_titles OWNER TO amp;

CREATE VIEW v_updated_date AS
    SELECT a.amp_activity_id, a.date_updated FROM amp_activity a ORDER BY a.amp_activity_id;


ALTER TABLE public.v_updated_date OWNER TO amp;


CREATE VIEW v_yes_no_government_approval_proc AS
    SELECT a.amp_activity_id, CASE a.governmentapprovalprocedures WHEN true THEN 'yes'::text WHEN false THEN 'no'::text ELSE 'unallocated'::text END AS governmentapprovalprocedures FROM amp_activity a ORDER BY a.amp_activity_id;


ALTER TABLE public.v_yes_no_government_approval_proc OWNER TO amp;


CREATE VIEW v_yes_no_joint_criteria AS
    SELECT a.amp_activity_id, CASE a.jointcriteria WHEN true THEN 'yes'::text WHEN false THEN 'no'::text ELSE 'unallocated'::text END AS jointcriteria FROM amp_activity a ORDER BY a.amp_activity_id;


ALTER TABLE public.v_yes_no_joint_criteria OWNER TO amp;



CREATE VIEW v_zones AS
    SELECT ra.amp_activity_id, getlocationname(getlocationidbyimplloc(l.location_id, 'Zone'::character varying)) AS location_name, getlocationidbyimplloc(l.location_id, 'Zone'::character varying) AS location_id, sum(ra.location_percentage) AS location_percentage FROM amp_activity_location ra, amp_location l WHERE ((ra.amp_location_id = l.amp_location_id) AND (getlocationidbyimplloc(l.location_id, 'Zone'::character varying) IS NOT NULL)) GROUP BY ra.amp_activity_id, getlocationidbyimplloc(l.location_id, 'Zone'::character varying);


ALTER TABLE public.v_zones OWNER TO amp;



