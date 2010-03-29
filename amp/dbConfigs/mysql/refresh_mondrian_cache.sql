DROP TABLE IF EXISTS cached_v_status;
CREATE TABLE cached_v_status AS SELECT * FROM v_status;
DROP TABLE IF EXISTS cached_v_donors;
CREATE TABLE cached_v_donors AS SELECT * FROM v_donors;
DROP TABLE IF EXISTS cached_v_actual_start_date;
CREATE TABLE cached_v_actual_start_date AS SELECT * FROM v_actual_start_date;
DROP TABLE IF EXISTS cached_v_titles;
CREATE TABLE cached_v_titles AS SELECT * FROM v_titles;
DROP TABLE IF EXISTS cached_v_terms_assist;
CREATE TABLE cached_v_terms_assist AS SELECT * FROM v_terms_assist;
DROP TABLE IF EXISTS cached_v_implementation_level;
CREATE TABLE cached_v_implementation_level AS SELECT * FROM v_implementation_level;
DROP TABLE IF EXISTS cached_v_actual_completion_date;
CREATE TABLE cached_v_actual_completion_date AS SELECT * FROM v_actual_completion_date;
DROP TABLE IF EXISTS cached_v_sectors;
CREATE TABLE cached_v_sectors AS SELECT * FROM v_sectors;
DROP TABLE IF EXISTS cached_v_regions;
CREATE TABLE cached_v_regions AS SELECT * FROM v_regions;
DROP TABLE IF EXISTS cached_v_financing_instrument;
CREATE TABLE cached_v_financing_instrument AS SELECT * FROM v_financing_instrument;
DROP TABLE IF EXISTS cached_v_objectives;
CREATE TABLE cached_v_objectives AS SELECT * FROM v_objectives;
DROP TABLE IF EXISTS cached_v_amp_id;
CREATE TABLE cached_v_amp_id AS SELECT * FROM v_amp_id;

DROP TABLE IF EXISTS cached_v_description;
CREATE TABLE cached_v_description AS SELECT * FROM v_description;
DROP TABLE IF EXISTS cached_v_components;
CREATE TABLE cached_v_components AS SELECT * FROM v_components;
DROP TABLE IF EXISTS cached_v_teams;
CREATE TABLE cached_v_teams AS SELECT * FROM v_teams;
DROP TABLE IF EXISTS cached_v_issues;
CREATE TABLE cached_v_issues AS SELECT * FROM v_issues;
DROP TABLE IF EXISTS cached_v_measures_taken;
CREATE TABLE cached_v_measures_taken AS SELECT * FROM v_measures_taken;
DROP TABLE IF EXISTS cached_v_actors;
CREATE TABLE cached_v_actors AS SELECT * FROM v_actors;
DROP TABLE IF EXISTS cached_v_actual_approval_date;
CREATE TABLE cached_v_actual_approval_date AS SELECT * FROM v_actual_approval_date;
DROP TABLE IF EXISTS cached_v_donor_commitment_date;
CREATE TABLE cached_v_donor_commitment_date AS SELECT * FROM v_donor_commitment_date;
DROP TABLE IF EXISTS cached_v_physical_progress;
CREATE TABLE cached_v_physical_progress AS SELECT * FROM v_physical_progress;
DROP TABLE IF EXISTS cached_v_ac_chapters;
CREATE TABLE cached_v_ac_chapters AS SELECT * FROM v_ac_chapters;
DROP TABLE IF EXISTS cached_v_accession_instruments;
CREATE TABLE cached_v_accession_instruments AS SELECT * FROM v_accession_instruments;
DROP TABLE IF EXISTS cached_v_costing_donors;
CREATE TABLE cached_v_costing_donors AS SELECT * FROM v_costing_donors;
DROP TABLE IF EXISTS cached_v_donor_groups;
CREATE TABLE cached_v_donor_groups AS SELECT * FROM v_donor_groups;
DROP TABLE IF EXISTS cached_v_component_description;
CREATE TABLE cached_v_component_description AS SELECT * FROM v_component_description;
DROP TABLE IF EXISTS cached_v_physical_title;
CREATE TABLE cached_v_physical_title AS SELECT * FROM v_physical_title;
DROP TABLE IF EXISTS cached_v_physical_description;
CREATE TABLE cached_v_physical_description AS SELECT * FROM v_physical_description;
DROP TABLE IF EXISTS `cached_v_sub_sectors`;
CREATE TABLE cached_v_sub_sectors AS SELECT * FROM `v_sub_sectors`;
DROP TABLE IF EXISTS `cached_v_nationalobjectives_level_0`;
CREATE TABLE cached_v_nationalobjectives_level_0 AS SELECT * FROM `v_nationalobjectives_level_0`;
DROP TABLE IF EXISTS cached_v_nationalobjectives_level_1;
CREATE TABLE cached_v_nationalobjectives_level_1 AS SELECT * FROM `v_nationalobjectives_level_1`;
DROP TABLE IF EXISTS `cached_v_nationalobjectives_level_2`;
CREATE TABLE cached_v_nationalobjectives_level_2 AS SELECT * FROM `v_nationalobjectives_level_2`;
DROP TABLE IF EXISTS `cached_v_nationalobjectives_level_3`;
CREATE TABLE cached_v_nationalobjectives_level_3 AS SELECT * FROM `v_nationalobjectives_level_3`;
DROP TABLE IF EXISTS `cached_v_nationalobjectives_level_4`;
CREATE TABLE cached_v_nationalobjectives_level_4 AS SELECT * FROM `v_nationalobjectives_level_4`;
DROP TABLE IF EXISTS `cached_v_nationalobjectives_level_5`;
CREATE TABLE cached_v_nationalobjectives_level_5 AS SELECT * FROM `v_nationalobjectives_level_5`;
DROP TABLE IF EXISTS `cached_v_nationalobjectives_level_6`;
CREATE TABLE cached_v_nationalobjectives_level_6 AS SELECT * FROM `v_nationalobjectives_level_6`;
DROP TABLE IF EXISTS `cached_v_nationalobjectives_level_7`;
CREATE TABLE cached_v_nationalobjectives_level_7 AS SELECT * FROM `v_nationalobjectives_level_7`;
DROP TABLE IF EXISTS `cached_v_nationalobjectives_level_8`;
CREATE TABLE cached_v_nationalobjectives_level_8 AS SELECT * FROM `v_nationalobjectives_level_8`;
DROP TABLE IF EXISTS `cached_v_primaryprogram_level_0`;
CREATE TABLE cached_v_primaryprogram_level_0 AS SELECT * FROM `v_primaryprogram_level_0`;
DROP TABLE IF EXISTS `cached_v_primaryprogram_level_1`;
CREATE TABLE cached_v_primaryprogram_level_1 AS SELECT * FROM `v_primaryprogram_level_1`;
DROP TABLE IF EXISTS `cached_v_primaryprogram_level_2`;
CREATE TABLE cached_v_primaryprogram_level_2 AS SELECT * FROM `v_primaryprogram_level_2`;
DROP TABLE IF EXISTS `cached_v_primaryprogram_level_3`;
CREATE TABLE cached_v_primaryprogram_level_3 AS SELECT * FROM `v_primaryprogram_level_3`;
DROP TABLE IF EXISTS `cached_v_primaryprogram_level_4`;
CREATE TABLE cached_v_primaryprogram_level_4 AS SELECT * FROM `v_primaryprogram_level_4`;
DROP TABLE IF EXISTS `cached_v_primaryprogram_level_5`;
CREATE TABLE cached_v_primaryprogram_level_5 AS SELECT * FROM `v_primaryprogram_level_5`;
DROP TABLE IF EXISTS `cached_v_primaryprogram_level_6`;
CREATE TABLE cached_v_primaryprogram_level_6 AS SELECT * FROM `v_primaryprogram_level_6`;
DROP TABLE IF EXISTS `cached_v_primaryprogram_level_7`;
CREATE TABLE cached_v_primaryprogram_level_7 AS SELECT * FROM `v_primaryprogram_level_7`;
DROP TABLE IF EXISTS `cached_v_primaryprogram_level_8`;
CREATE TABLE cached_v_primaryprogram_level_8 AS SELECT * FROM `v_primaryprogram_level_8`;
DROP TABLE IF EXISTS `cached_v_secondaryprogram_level_0`;
CREATE TABLE cached_v_secondaryprogram_level_0 AS SELECT * FROM `v_secondaryprogram_level_0`;
DROP TABLE IF EXISTS `cached_v_secondaryprogram_level_1`;
CREATE TABLE cached_v_secondaryprogram_level_1 AS SELECT * FROM `v_secondaryprogram_level_1`;
DROP TABLE IF EXISTS `cached_v_secondaryprogram_level_2`;
CREATE TABLE cached_v_secondaryprogram_level_2 AS SELECT * FROM `v_secondaryprogram_level_2`;
DROP TABLE IF EXISTS `cached_v_secondaryprogram_level_3`;
CREATE TABLE cached_v_secondaryprogram_level_3 AS SELECT * FROM  `v_secondaryprogram_level_3`;
DROP TABLE IF EXISTS `cached_v_secondaryprogram_level_4`;
CREATE TABLE cached_v_secondaryprogram_level_4 AS SELECT * FROM `v_secondaryprogram_level_4`;
DROP TABLE IF EXISTS `cached_v_secondaryprogram_level_5`;
CREATE TABLE cached_v_secondaryprogram_level_5 AS SELECT * FROM `v_secondaryprogram_level_5`;
DROP TABLE IF EXISTS `cached_v_secondaryprogram_level_6`;
CREATE TABLE cached_v_secondaryprogram_level_6 AS SELECT * FROM `v_secondaryprogram_level_7`;
DROP TABLE IF EXISTS `cached_v_secondaryprogram_level_7`;
CREATE TABLE cached_v_secondaryprogram_level_7 AS SELECT * FROM `v_secondaryprogram_level_7`;
DROP TABLE IF EXISTS `cached_v_secondaryprogram_level_8`;
CREATE TABLE cached_v_secondaryprogram_level_8 AS SELECT * FROM `v_secondaryprogram_level_8`;
DROP TABLE IF EXISTS `cached_v_executing_agency`;
CREATE TABLE cached_v_executing_agency AS SELECT * FROM `v_executing_agency`;
DROP TABLE IF EXISTS `cached_v_implementing_agency`;
CREATE TABLE cached_v_implementing_agency AS SELECT * FROM `v_implementing_agency`;
DROP TABLE IF EXISTS `cached_v_contracting_agency`;
CREATE TABLE cached_v_contracting_agency AS SELECT * FROM `v_contracting_agency`;
DROP TABLE IF EXISTS `cached_v_beneficiary_agency`;
CREATE TABLE cached_v_beneficiary_agency AS SELECT * FROM `v_beneficiary_agency`;
DROP TABLE IF EXISTS `cached_v_sector_group`;
CREATE TABLE cached_v_sector_group AS SELECT * FROM `v_sector_group`;
DROP TABLE IF EXISTS `cached_v_regional_group`;
CREATE TABLE cached_v_regional_group AS SELECT * FROM `v_regional_group`;
DROP TABLE IF EXISTS `cached_v_project_id`;
CREATE TABLE cached_v_project_id AS SELECT * FROM `v_project_id`;
DROP TABLE IF EXISTS `cached_v_credit_donation`;
CREATE TABLE cached_v_credit_donation AS SELECT * FROM `v_credit_donation`;
DROP TABLE IF EXISTS `cached_v_credit_donation`;
CREATE TABLE cached_v_credit_donation AS SELECT * FROM `v_credit_donation`;
DROP TABLE IF EXISTS `cached_v_implementation_location`;
CREATE TABLE cached_v_implementation_location AS SELECT * FROM `v_implementation_location`;
DROP TABLE IF EXISTS `cached_v_activity_creator`;
CREATE TABLE cached_v_activity_creator AS SELECT * FROM `v_activity_creator`;
DROP TABLE IF EXISTS `cached_v_activity_changed_by`;
CREATE TABLE cached_v_activity_changed_by AS SELECT * FROM `v_activity_changed_by`;
DROP TABLE IF EXISTS `cached_v_creation_date`;
CREATE TABLE cached_v_creation_date AS SELECT * FROM `v_creation_date`;
DROP TABLE IF EXISTS `cached_v_secondary_sectors`;
CREATE TABLE `cached_v_secondary_sectors` AS SELECT * FROM `v_secondary_sectors`;

DROP TABLE IF EXISTS `cached_v_secondary_sub_sectors`;
CREATE TABLE cached_v_secondary_sub_sectors AS SELECT * FROM `v_secondary_sub_sectors`;

DROP TABLE IF EXISTS `cached_v_convenio_numcont`;
CREATE TABLE cached_v_convenio_numcont AS SELECT * FROM `v_convenio_numcont`;
DROP TABLE IF EXISTS `cached_v_responsible_organisation`;
CREATE TABLE cached_v_responsible_organisation AS SELECT * FROM `v_responsible_organisation`;
DROP TABLE IF EXISTS `cached_v_responsible_org_info`;
CREATE TABLE cached_v_responsible_org_info AS SELECT * FROM `v_responsible_org_info`;
DROP TABLE IF EXISTS `cached_v_responsible_org_groups`;
CREATE TABLE cached_v_responsible_org_groups AS SELECT * FROM `v_responsible_org_groups`;

DROP TABLE IF EXISTS `cached_v_donor_type`;
CREATE TABLE cached_v_donor_type AS SELECT * FROM `v_donor_type`;
DROP TABLE IF EXISTS `cached_v_implementing_agency_groups_type`;
CREATE TABLE cached_v_implementing_agency_groups_type AS SELECT * FROM `v_implementing_agency_groups`;
DROP TABLE IF EXISTS `cached_v_beneficiary_agency_groups`;
CREATE TABLE cached_v_beneficiary_agency_groups AS SELECT * FROM `v_beneficiary_agency_groups`;
DROP TABLE IF EXISTS `cached_v_executing_agency_groups`;
CREATE TABLE cached_v_executing_agency_groups AS SELECT * FROM `v_executing_agency_groups`;
DROP TABLE IF EXISTS `cached_v_proposed_cost`;
CREATE TABLE cached_v_proposed_cost AS SELECT * FROM `v_proposed_cost`;
DROP TABLE IF EXISTS `cached_v_secondary_sub_sub_sectors`;
CREATE TABLE cached_v_secondary_sub_sub_sectors AS SELECT * FROM `v_secondary_sub_sub_sectors`;
DROP TABLE IF EXISTS `cached_v_updated_date`;
CREATE TABLE cached_v_updated_date AS SELECT * FROM `v_updated_date`;
DROP TABLE IF EXISTS `cached_v_on_off_budget`;
CREATE TABLE cached_v_on_off_budget AS SELECT * FROM `v_on_off_budget`;
DROP TABLE IF EXISTS `cached_v_actual_proposed_date`;
CREATE TABLE cached_v_actual_proposed_date AS SELECT * FROM `v_actual_proposed_date`;
DROP TABLE IF EXISTS `cached_v_proposed_completion_date`;
CREATE TABLE cached_v_proposed_completion_date AS SELECT * FROM `v_proposed_completion_date`;
DROP TABLE IF EXISTS `cached_v_project_category`;
CREATE TABLE cached_v_project_category AS SELECT * FROM `v_project_category`;
DROP TABLE IF EXISTS `cached_v_project_comments`;
CREATE TABLE cached_v_project_comments AS SELECT * FROM `v_project_comments`;
DROP TABLE IF EXISTS `cached_v_component_type`;
CREATE TABLE cached_v_component_type AS SELECT * FROM `v_component_type`;
DROP TABLE IF EXISTS `cached_v_computed_dates`;
CREATE TABLE cached_v_computed_dates AS SELECT * FROM `v_computed_dates`;
DROP TABLE IF EXISTS `cached_v_cris_number`;
CREATE TABLE cached_v_cris_number AS SELECT * FROM `v_cris_number`;
DROP TABLE IF EXISTS `cached_v_code_chapitre`;
CREATE TABLE cached_v_code_chapitre AS SELECT * FROM `v_code_chapitre`;
DROP TABLE IF EXISTS `cached_v_executing_agency_info`;
CREATE TABLE cached_v_executing_agency_info AS SELECT * FROM `v_executing_agency_info`;
DROP TABLE IF EXISTS `cached_v_beneficiary_agency_info`;
CREATE TABLE cached_v_beneficiary_agency_info AS SELECT * FROM `v_beneficiary_agency_info`;
DROP TABLE IF EXISTS `cached_v_contracting_agency_info`;
CREATE TABLE cached_v_contracting_agency_info AS SELECT * FROM `v_contracting_agency_info`;
DROP TABLE IF EXISTS `cached_v_implementing_agency_info`;
CREATE TABLE cached_v_implementing_agency_info AS SELECT * FROM `v_implementing_agency_info`;
DROP TABLE IF EXISTS `cached_v_regional_group_info`;
CREATE TABLE cached_v_regional_group_info AS SELECT * FROM `v_regional_group_info`;
DROP TABLE IF EXISTS `cached_v_sector_group_info`;
CREATE TABLE cached_v_sector_group_info AS SELECT * FROM `v_sector_group_info`;
DROP TABLE IF EXISTS `cached_v_funding_status`;
CREATE TABLE cached_v_funding_status AS SELECT * FROM `v_funding_status`;
DROP TABLE IF EXISTS `cached_v_contact_donor`;
CREATE TABLE cached_v_contact_donor AS SELECT * FROM `v_contact_donor`;
DROP TABLE IF EXISTS `cached_v_contact_mofed`;
CREATE TABLE cached_v_contact_mofed AS SELECT * FROM `v_contact_mofed`;
DROP TABLE IF EXISTS `cached_v_contact_project_coordinator`;
CREATE TABLE cached_v_contact_project_coordinator AS SELECT * FROM `v_contact_project_coordinator`;
DROP TABLE IF EXISTS `cached_v_contact_sector_ministry`;
CREATE TABLE cached_v_contact_sector_ministry AS SELECT * FROM `v_contact_sector_ministry`;
DROP TABLE IF EXISTS `cached_v_contact_impl_exec_agency`;
CREATE TABLE cached_v_contact_impl_exec_agency AS SELECT * FROM `v_contact_impl_exec_agency`;
DROP TABLE IF EXISTS `cached_v_zones`;
CREATE TABLE cached_v_zones AS SELECT * FROM `v_zones`;
DROP TABLE IF EXISTS `cached_v_districts`;
CREATE TABLE cached_v_districts AS SELECT * FROM `v_districts`;
DROP TABLE IF EXISTS `cached_v_contribution_funding`;
CREATE TABLE cached_v_contribution_funding AS SELECT * FROM `v_contribution_funding`;
DROP TABLE IF EXISTS `cached_v_donor_funding`;
CREATE TABLE cached_v_donor_funding AS SELECT * FROM `v_donor_funding`;
DROP TABLE IF EXISTS `cached_v_regional_funding`;
CREATE TABLE cached_v_regional_funding AS SELECT * FROM `v_regional_funding`;
DROP TABLE IF EXISTS `cached_v_component_funding`;
CREATE TABLE cached_v_component_funding AS SELECT * FROM `v_component_funding`;
DROP TABLE IF EXISTS `cached_v_sub_sub_sectors`;
CREATE TABLE cached_v_sub_sub_sectors AS SELECT * FROM `v_sub_sub_sectors`;
DROP TABLE IF EXISTS `cached_v_costs`;
CREATE TABLE cached_v_costs AS SELECT * FROM `v_costs`;

DROP TABLE IF EXISTS cached_amp_activity;
CREATE TABLE cached_amp_activity LIKE amp_activity_version;
INSERT INTO cached_amp_activity SELECT * FROM amp_activity;

DROP TABLE IF EXISTS cached_v_donor_date_hierarchy;
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy limit 0,0;
alter table cached_v_donor_date_hierarchy modify quarter_name varchar(2);
insert into cached_v_donor_date_hierarchy SELECT * FROM v_donor_date_hierarchy;


DROP TABLE IF EXISTS `cached_v_m_regions`;
CREATE TABLE cached_v_m_regions AS SELECT * FROM `v_regions_cached`;

ALTER TABLE `cached_v_m_regions` ADD INDEX idx_amp_activity (amp_activity_id),
ADD INDEX idx_region_id (region_id);


ALTER TABLE `cached_v_status` ADD INDEX idx_amp_activity (amp_activity_id),
ADD INDEX idx_st_name (name),ADD INDEX idx_st_id (amp_status_id);

ALTER TABLE `cached_v_sectors` ADD INDEX idx_psec_activity (amp_activity_id),
ADD INDEX idx_psec_name (sectorname),ADD INDEX idx_pesc_id (amp_sector_id),
ADD INDEX idx_pesc_per (sector_percentage);

ALTER TABLE cached_v_secondary_sectors ADD INDEX idx_ssec_activity (amp_activity_id),
ADD INDEX idx_ssec_name (sectorname),ADD INDEX idx_sesc_id (amp_sector_id),
ADD INDEX idx_sesc_per (sector_percentage);

ALTER TABLE cached_v_sub_sectors ADD INDEX idx_psub_activity (amp_activity_id),
ADD INDEX idx_psub_name (sectorname),ADD INDEX idx_psub_id (amp_sector_id),
ADD INDEX idx_psub_per (sector_percentage);

ALTER TABLE `cached_v_secondary_sub_sectors` ADD INDEX idx_ssub_activity (amp_activity_id),
ADD INDEX idx_ssub_name (sectorname),ADD INDEX idx_ssub_id (amp_sector_id),
ADD INDEX idx_ssub_per (sector_percentage);


ALTER TABLE cached_v_nationalobjectives_level_0 ADD INDEX idx_npidl0_pidid (amp_program_id),
ADD INDEX idx_npl0_activity (amp_activity_id),ADD INDEX idx_npl0_name (name);
ALTER TABLE cached_v_nationalobjectives_level_1 ADD INDEX idx_npidl0_pidid (amp_program_id),
ADD INDEX idx_npl0_activity (amp_activity_id),ADD INDEX idx_npl0_name (name);
ALTER TABLE cached_v_nationalobjectives_level_2 ADD INDEX idx_npidl0_pidid (amp_program_id),
ADD INDEX idx_npl0_activity (amp_activity_id),ADD INDEX idx_npl0_name (name);
ALTER TABLE cached_v_nationalobjectives_level_3 ADD INDEX idx_npidl0_pidid (amp_program_id),
ADD INDEX idx_npl0_activity (amp_activity_id),ADD INDEX idx_npl0_name (name);
ALTER TABLE cached_v_nationalobjectives_level_4 ADD INDEX idx_npidl0_pidid (amp_program_id),
ADD INDEX idx_npl0_activity (amp_activity_id),ADD INDEX idx_npl0_name (name);
ALTER TABLE cached_v_nationalobjectives_level_5 ADD INDEX idx_npidl0_pidid (amp_program_id),
ADD INDEX idx_npl0_activity (amp_activity_id),ADD INDEX idx_npl0_name (name);
ALTER TABLE cached_v_nationalobjectives_level_6 ADD INDEX idx_npidl0_pidid (amp_program_id),
ADD INDEX idx_npl0_activity (amp_activity_id),ADD INDEX idx_npl0_name (name);
ALTER TABLE cached_v_nationalobjectives_level_7 ADD INDEX idx_npidl0_pidid (amp_program_id),
ADD INDEX idx_npl0_activity (amp_activity_id),ADD INDEX idx_npl0_name (name);
ALTER TABLE cached_v_nationalobjectives_level_8 ADD INDEX idx_npidl0_pidid (amp_program_id),
ADD INDEX idx_npl0_activity (amp_activity_id),ADD INDEX idx_npl0_name (name);


ALTER TABLE cached_v_primaryprogram_level_0 ADD INDEX idx_ppidl0_pidid (amp_program_id),
ADD INDEX idx_ppl0_activity (amp_activity_id),ADD INDEX idx_ppl0_name (name);
ALTER TABLE cached_v_primaryprogram_level_1 ADD INDEX idx_ppidl1_pidid (amp_program_id),
ADD INDEX idx_ppl1_activity (amp_activity_id),ADD INDEX idx_ppl1_name (name);
ALTER TABLE cached_v_primaryprogram_level_2 ADD INDEX idx_ppidl2_pidid (amp_program_id),
ADD INDEX idx_ppl2_activity (amp_activity_id),ADD INDEX idx_ppl2_name (name);
ALTER TABLE cached_v_primaryprogram_level_3 ADD INDEX idx_ppidl3_pidid (amp_program_id),
ADD INDEX idx_ppl3_activity (amp_activity_id),ADD INDEX idx_ppl3_name (name);
ALTER TABLE cached_v_primaryprogram_level_4 ADD INDEX idx_ppidl4_pidid (amp_program_id),
ADD INDEX idx_npl4_activity (amp_activity_id),ADD INDEX idx_npl4_name (name);
ALTER TABLE cached_v_primaryprogram_level_5 ADD INDEX idx_ppidl5_pidid (amp_program_id),
ADD INDEX idx_ppl5_activity (amp_activity_id),ADD INDEX idx_ppl5_name (name);
ALTER TABLE cached_v_primaryprogram_level_6 ADD INDEX idx_ppidl6_pidid (amp_program_id),
ADD INDEX idx_ppl6_activity (amp_activity_id),ADD INDEX idx_ppl6_name (name);
ALTER TABLE cached_v_primaryprogram_level_7 ADD INDEX idx_ppidl7_pidid (amp_program_id),
ADD INDEX idx_ppl7_activity (amp_activity_id),ADD INDEX idx_ppl7_name (name);
ALTER TABLE cached_v_primaryprogram_level_8 ADD INDEX idx_ppidl8_pidid (amp_program_id),
ADD INDEX idx_ppl8_activity (amp_activity_id),ADD INDEX idx_ppl8_name (name);



ALTER TABLE cached_v_secondaryprogram_level_0 ADD INDEX idx_spidl0_pidid (amp_program_id),
ADD INDEX idx_spl0_activity (amp_activity_id),ADD INDEX idx_spl0_name (name);
ALTER TABLE cached_v_secondaryprogram_level_1 ADD INDEX idx_spidl1_pidid (amp_program_id),
ADD INDEX idx_spl1_activity (amp_activity_id),ADD INDEX idx_spl1_name (name);
ALTER TABLE cached_v_secondaryprogram_level_2 ADD INDEX idx_spidl2_pidid (amp_program_id),
ADD INDEX idx_spl2_activity (amp_activity_id),ADD INDEX idx_spl2_name (name);
ALTER TABLE cached_v_secondaryprogram_level_3 ADD INDEX idx_spidl3_pidid (amp_program_id),
ADD INDEX idx_spl3_activity (amp_activity_id),ADD INDEX idx_spl3_name (name);
ALTER TABLE cached_v_secondaryprogram_level_4 ADD INDEX idx_spidl4_pidid (amp_program_id),
ADD INDEX idx_spl4_activity (amp_activity_id),ADD INDEX idx_spl4_name (name);
ALTER TABLE cached_v_secondaryprogram_level_5 ADD INDEX idx_spidl5_pidid (amp_program_id),
ADD INDEX idx_spl5_activity (amp_activity_id),ADD INDEX idx_spl5_name (name);
ALTER TABLE cached_v_secondaryprogram_level_6 ADD INDEX idx_spidl6_pidid (amp_program_id),
ADD INDEX idx_spl6_activity (amp_activity_id),ADD INDEX idx_spl6_name (name);
ALTER TABLE cached_v_secondaryprogram_level_7 ADD INDEX idx_spidl7_pidid (amp_program_id),
ADD INDEX idx_spl7_activity (amp_activity_id),ADD INDEX idx_spl7_name (name);
ALTER TABLE cached_v_secondaryprogram_level_8 ADD INDEX idx_spidl8_pidid (amp_program_id),
ADD INDEX idx_spl8_activity (amp_activity_id),ADD INDEX idx_spl8_name (name);


DROP TABLE IF EXISTS cached_v_m_donor_funding;
CREATE TABLE cached_v_m_donor_funding AS SELECT * FROM `v_donor_funding_cached`;

ALTER TABLE v_donor_funding_cached ADD INDEX idx_amp_activity (amp_activity_id),
ADD INDEX idx_donor_name (donor_name),
ADD INDEX idx_curr_code (currency_code),
ADD INDEX idx_financ_intrum (financing_instrument_name),
ADD INDEX idx_donor_type (donor_type_name),
ADD INDEX idx_donor_name (donor_name),
ADD INDEX idx_curr_code (currency_code)
ADD INDEX idx_donor_name (donor_name),
ADD INDEX idx_donor_group (org_grp_name)
ADD INDEX idx_region_name (Region),
ADD INDEX idx_pri_prog_name (primary_program_name),
ADD INDEX idx_sec_prog_name (secondary_program_name),
ADD INDEX idx_pri_sector_name (p_sectorname),
ADD INDEX idx_nac_prog_name (national_program_name);





