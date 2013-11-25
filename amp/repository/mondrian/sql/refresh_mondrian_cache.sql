DROP TABLE IF EXISTS cached_v_status CASCADE;
CREATE TABLE cached_v_status AS SELECT * FROM v_status CASCADE;
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
DROP TABLE IF EXISTS cached_v_sectors CASCADE;
CREATE TABLE cached_v_sectors AS SELECT * FROM v_sectors;

DROP TABLE IF EXISTS cached_v_contracting_arrangements;
CREATE TABLE cached_v_contracting_arrangements AS SELECT * FROM v_contracting_arrangements;
DROP TABLE IF EXISTS cached_v_activity_modified_by;
CREATE TABLE cached_v_activity_modified_by AS SELECT * FROM v_activity_modified_by;

CREATE INDEX ON cached_v_contracting_arrangements(amp_activity_id);
CREATE INDEX ON cached_v_contracting_arrangements(act_id);

CREATE INDEX ON cached_v_activity_modified_by(amp_activity_id);
CREATE INDEX ON cached_v_activity_modified_by(user_id);

DROP TABLE IF EXISTS cached_v_m_sectors CASCADE;
CREATE TABLE cached_v_m_sectors AS SELECT * FROM v_m_sectors;

DROP TABLE IF EXISTS cached_v_capital_and_exp;
CREATE TABLE cached_v_capital_and_exp AS SELECT * from v_capital_and_exp;

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
DROP TABLE IF EXISTS cached_v_sub_sectors;
CREATE TABLE cached_v_sub_sectors AS SELECT * FROM v_sub_sectors;
DROP TABLE IF EXISTS cached_v_sub_sub_sectors;
CREATE TABLE cached_v_sub_sub_sectors AS SELECT * FROM v_sub_sectors;

DROP TABLE IF EXISTS cached_v_nationalobjectives_level_0 CASCADE;
CREATE TABLE cached_v_nationalobjectives_level_0 AS SELECT * FROM v_nationalobjectives_level_0;
DROP TABLE IF EXISTS cached_v_nationalobjectives_level_1;
CREATE TABLE cached_v_nationalobjectives_level_1 AS SELECT * FROM v_nationalobjectives_level_1;
DROP TABLE IF EXISTS cached_v_nationalobjectives_level_2;
CREATE TABLE cached_v_nationalobjectives_level_2 AS SELECT * FROM v_nationalobjectives_level_2;
DROP TABLE IF EXISTS cached_v_nationalobjectives_level_3;
CREATE TABLE cached_v_nationalobjectives_level_3 AS SELECT * FROM v_nationalobjectives_level_3;
DROP TABLE IF EXISTS cached_v_nationalobjectives_level_4;
CREATE TABLE cached_v_nationalobjectives_level_4 AS SELECT * FROM v_nationalobjectives_level_4;
DROP TABLE IF EXISTS cached_v_nationalobjectives_level_5;
CREATE TABLE cached_v_nationalobjectives_level_5 AS SELECT * FROM v_nationalobjectives_level_5;
DROP TABLE IF EXISTS cached_v_nationalobjectives_level_6;
CREATE TABLE cached_v_nationalobjectives_level_6 AS SELECT * FROM v_nationalobjectives_level_6;
DROP TABLE IF EXISTS cached_v_nationalobjectives_level_7;
CREATE TABLE cached_v_nationalobjectives_level_7 AS SELECT * FROM v_nationalobjectives_level_7;
DROP TABLE IF EXISTS cached_v_nationalobjectives_level_8;
CREATE TABLE cached_v_nationalobjectives_level_8 AS SELECT * FROM v_nationalobjectives_level_8;
DROP TABLE IF EXISTS cached_v_primaryprogram_level_0;
CREATE TABLE cached_v_primaryprogram_level_0 AS SELECT * FROM v_primaryprogram_level_0;
DROP TABLE IF EXISTS cached_v_primaryprogram_level_1;
CREATE TABLE cached_v_primaryprogram_level_1 AS SELECT * FROM v_primaryprogram_level_1;
DROP TABLE IF EXISTS cached_v_primaryprogram_level_2;
CREATE TABLE cached_v_primaryprogram_level_2 AS SELECT * FROM v_primaryprogram_level_2;
DROP TABLE IF EXISTS cached_v_primaryprogram_level_3;
CREATE TABLE cached_v_primaryprogram_level_3 AS SELECT * FROM v_primaryprogram_level_3;
DROP TABLE IF EXISTS cached_v_primaryprogram_level_4;
CREATE TABLE cached_v_primaryprogram_level_4 AS SELECT * FROM v_primaryprogram_level_4;
DROP TABLE IF EXISTS cached_v_primaryprogram_level_5;
CREATE TABLE cached_v_primaryprogram_level_5 AS SELECT * FROM v_primaryprogram_level_5;
DROP TABLE IF EXISTS cached_v_primaryprogram_level_6;
CREATE TABLE cached_v_primaryprogram_level_6 AS SELECT * FROM v_primaryprogram_level_6;
DROP TABLE IF EXISTS cached_v_primaryprogram_level_7;
CREATE TABLE cached_v_primaryprogram_level_7 AS SELECT * FROM v_primaryprogram_level_7;
DROP TABLE IF EXISTS cached_v_primaryprogram_level_8;
CREATE TABLE cached_v_primaryprogram_level_8 AS SELECT * FROM v_primaryprogram_level_8;
DROP TABLE IF EXISTS cached_v_secondaryprogram_level_0;
CREATE TABLE cached_v_secondaryprogram_level_0 AS SELECT * FROM v_secondaryprogram_level_0;
DROP TABLE IF EXISTS cached_v_secondaryprogram_level_1;
CREATE TABLE cached_v_secondaryprogram_level_1 AS SELECT * FROM v_secondaryprogram_level_1;
DROP TABLE IF EXISTS cached_v_secondaryprogram_level_2;
CREATE TABLE cached_v_secondaryprogram_level_2 AS SELECT * FROM v_secondaryprogram_level_2;
DROP TABLE IF EXISTS cached_v_secondaryprogram_level_3;
CREATE TABLE cached_v_secondaryprogram_level_3 AS SELECT * FROM  v_secondaryprogram_level_3;
DROP TABLE IF EXISTS cached_v_secondaryprogram_level_4;
CREATE TABLE cached_v_secondaryprogram_level_4 AS SELECT * FROM v_secondaryprogram_level_4;
DROP TABLE IF EXISTS cached_v_secondaryprogram_level_5;
CREATE TABLE cached_v_secondaryprogram_level_5 AS SELECT * FROM v_secondaryprogram_level_5;
DROP TABLE IF EXISTS cached_v_secondaryprogram_level_6;
CREATE TABLE cached_v_secondaryprogram_level_6 AS SELECT * FROM v_secondaryprogram_level_7;
DROP TABLE IF EXISTS cached_v_secondaryprogram_level_7;
CREATE TABLE cached_v_secondaryprogram_level_7 AS SELECT * FROM v_secondaryprogram_level_7;
DROP TABLE IF EXISTS cached_v_secondaryprogram_level_8;
CREATE TABLE cached_v_secondaryprogram_level_8 AS SELECT * FROM v_secondaryprogram_level_8;
DROP TABLE IF EXISTS cached_v_executing_agency;
CREATE TABLE cached_v_executing_agency AS SELECT * FROM v_executing_agency;
DROP TABLE IF EXISTS cached_v_implementing_agency;
CREATE TABLE cached_v_implementing_agency AS SELECT * FROM v_implementing_agency;
DROP TABLE IF EXISTS cached_v_contracting_agency;
CREATE TABLE cached_v_contracting_agency AS SELECT * FROM v_contracting_agency;
DROP TABLE IF EXISTS cached_v_beneficiary_agency;
CREATE TABLE cached_v_beneficiary_agency AS SELECT * FROM v_beneficiary_agency;
DROP TABLE IF EXISTS cached_v_sector_group;
CREATE TABLE cached_v_sector_group AS SELECT * FROM v_sector_group;
DROP TABLE IF EXISTS cached_v_regional_group;
CREATE TABLE cached_v_regional_group AS SELECT * FROM v_regional_group;
DROP TABLE IF EXISTS cached_v_project_id;
CREATE TABLE cached_v_project_id AS SELECT * FROM v_project_id;
DROP TABLE IF EXISTS cached_v_credit_donation;
CREATE TABLE cached_v_credit_donation AS SELECT * FROM v_credit_donation;
DROP TABLE IF EXISTS cached_v_credit_donation;
CREATE TABLE cached_v_credit_donation AS SELECT * FROM v_credit_donation;
DROP TABLE IF EXISTS cached_v_implementation_location;
CREATE TABLE cached_v_implementation_location AS SELECT * FROM v_implementation_location;
DROP TABLE IF EXISTS cached_v_activity_creator;
CREATE TABLE cached_v_activity_creator AS SELECT * FROM v_activity_creator;
DROP TABLE IF EXISTS cached_v_activity_changed_by;
CREATE TABLE cached_v_activity_changed_by AS SELECT * FROM v_activity_modified_by;
DROP TABLE IF EXISTS cached_v_creation_date;
CREATE TABLE cached_v_creation_date AS SELECT * FROM v_creation_date;
DROP TABLE IF EXISTS cached_v_secondary_sectors;
CREATE TABLE cached_v_secondary_sectors AS SELECT * FROM v_secondary_sectors;

DROP TABLE IF EXISTS cached_v_m_secondary_sectors;
CREATE TABLE cached_v_m_secondary_sectors AS SELECT * FROM v_m_secondary_sectors;

DROP TABLE IF EXISTS cached_v_secondary_sub_sectors;
CREATE TABLE cached_v_secondary_sub_sectors AS SELECT * FROM v_secondary_sub_sectors;

DROP TABLE IF EXISTS cached_v_convenio_numcont;
CREATE TABLE cached_v_convenio_numcont AS SELECT * FROM v_convenio_numcont;
DROP TABLE IF EXISTS cached_v_responsible_organisation;
CREATE TABLE cached_v_responsible_organisation AS SELECT * FROM v_responsible_organisation;
DROP TABLE IF EXISTS cached_v_responsible_org_info;
CREATE TABLE cached_v_responsible_org_info AS SELECT * FROM v_responsible_org_info;
DROP TABLE IF EXISTS cached_v_responsible_org_groups;
CREATE TABLE cached_v_responsible_org_groups AS SELECT * FROM v_responsible_org_groups;

DROP TABLE IF EXISTS cached_v_donor_type;
CREATE TABLE cached_v_donor_type AS SELECT * FROM v_donor_type;
DROP TABLE IF EXISTS cached_v_implementing_agency_groups_type;
CREATE TABLE cached_v_implementing_agency_groups_type AS SELECT * FROM v_implementing_agency_groups;
DROP TABLE IF EXISTS cached_v_beneficiary_agency_groups;
CREATE TABLE cached_v_beneficiary_agency_groups AS SELECT * FROM v_beneficiary_agency_groups;
DROP TABLE IF EXISTS cached_v_executing_agency_groups;
CREATE TABLE cached_v_executing_agency_groups AS SELECT * FROM v_executing_agency_groups;
DROP TABLE IF EXISTS cached_v_proposed_cost;
DROP TABLE IF EXISTS cached_v_proposed_start_date;
CREATE TABLE cached_v_proposed_start_date AS SELECT * FROM v_proposed_start_date;
DROP TABLE IF EXISTS cached_v_results;
CREATE TABLE cached_v_results AS SELECT * FROM v_results;
CREATE TABLE cached_v_proposed_cost AS SELECT * FROM v_proposed_cost;
DROP TABLE IF EXISTS cached_v_secondary_sub_sub_sectors;
CREATE TABLE cached_v_secondary_sub_sub_sectors AS SELECT * FROM v_secondary_sub_sub_sectors;
DROP TABLE IF EXISTS cached_v_updated_date;
CREATE TABLE cached_v_updated_date AS SELECT * FROM v_updated_date;
DROP TABLE IF EXISTS cached_v_on_off_budget;
CREATE TABLE cached_v_on_off_budget AS SELECT * FROM v_on_off_budget;
DROP TABLE IF EXISTS cached_v_actual_proposed_date;
CREATE TABLE cached_v_actual_proposed_date AS SELECT * FROM v_actual_proposed_date;
DROP TABLE IF EXISTS cached_v_proposed_completion_date;
CREATE TABLE cached_v_proposed_completion_date AS SELECT * FROM v_proposed_completion_date;
DROP TABLE IF EXISTS cached_v_project_category;
CREATE TABLE cached_v_project_category AS SELECT * FROM v_project_category;
DROP TABLE IF EXISTS cached_v_project_comments;
CREATE TABLE cached_v_project_comments AS SELECT * FROM v_project_comments;
DROP TABLE IF EXISTS cached_v_component_type;
CREATE TABLE cached_v_component_type AS SELECT * FROM v_component_type;
DROP TABLE IF EXISTS cached_v_computed_dates;
CREATE TABLE cached_v_computed_dates AS SELECT * FROM v_computed_dates;
DROP TABLE IF EXISTS cached_v_cris_number;
CREATE TABLE cached_v_cris_number AS SELECT * FROM v_cris_number;
DROP TABLE IF EXISTS cached_v_code_chapitre;
CREATE TABLE cached_v_code_chapitre AS SELECT * FROM v_code_chapitre;
DROP TABLE IF EXISTS cached_v_executing_agency_info;
CREATE TABLE cached_v_executing_agency_info AS SELECT * FROM v_executing_agency_info;
DROP TABLE IF EXISTS cached_v_beneficiary_agency_info;
CREATE TABLE cached_v_beneficiary_agency_info AS SELECT * FROM v_beneficiary_agency_info;
DROP TABLE IF EXISTS cached_v_contracting_agency_info;
CREATE TABLE cached_v_contracting_agency_info AS SELECT * FROM v_contracting_agency_info;
DROP TABLE IF EXISTS cached_v_implementing_agency_info;
CREATE TABLE cached_v_implementing_agency_info AS SELECT * FROM v_implementing_agency_info;
DROP TABLE IF EXISTS cached_v_regional_group_info;
CREATE TABLE cached_v_regional_group_info AS SELECT * FROM v_regional_group_info;
DROP TABLE IF EXISTS cached_v_sector_group_info;
CREATE TABLE cached_v_sector_group_info AS SELECT * FROM v_sector_group_info;
DROP TABLE IF EXISTS cached_v_funding_status;
CREATE TABLE cached_v_funding_status AS SELECT * FROM v_funding_status;

DROP TABLE IF EXISTS cached_v_zones;
CREATE TABLE cached_v_zones AS SELECT * FROM v_zones;
DROP TABLE IF EXISTS cached_v_districts;
CREATE TABLE cached_v_districts AS SELECT * FROM v_districts;
DROP TABLE IF EXISTS cached_v_contribution_funding;
CREATE TABLE cached_v_contribution_funding AS SELECT * FROM v_contribution_funding;
DROP TABLE IF EXISTS cached_v_donor_funding;
CREATE TABLE cached_v_donor_funding AS SELECT * FROM v_donor_funding;
DROP TABLE IF EXISTS cached_v_regional_funding;
CREATE TABLE cached_v_regional_funding AS SELECT * FROM v_regional_funding;
DROP TABLE IF EXISTS cached_v_component_funding;
CREATE TABLE cached_v_component_funding AS SELECT * FROM v_component_funding;
DROP TABLE IF EXISTS cached_v_sub_sub_sectors;
CREATE TABLE cached_v_sub_sub_sectors AS SELECT * FROM v_sub_sub_sectors;
DROP TABLE IF EXISTS cached_v_costs;
CREATE TABLE cached_v_costs AS SELECT * FROM v_costs;

DROP TABLE IF EXISTS cached_v_pledges_aid_modality;
CREATE TABLE cached_v_pledges_aid_modality AS SELECT * FROM v_pledges_aid_modality;
DROP TABLE IF EXISTS cached_v_pledges_contact1_address;
CREATE TABLE cached_v_pledges_contact1_address AS SELECT * FROM v_pledges_contact1_address;
DROP TABLE IF EXISTS cached_v_pledges_contact1_alternate;
CREATE TABLE cached_v_pledges_contact1_alternate AS SELECT * FROM v_pledges_contact1_alternate;
DROP TABLE IF EXISTS cached_v_pledges_contact1_email;
CREATE TABLE cached_v_pledges_contact1_email AS SELECT * FROM v_pledges_contact1_email;
DROP TABLE IF EXISTS cached_v_pledges_contact1_ministry;
CREATE TABLE cached_v_pledges_contact1_ministry AS SELECT * FROM v_pledges_contact1_ministry;
DROP TABLE IF EXISTS cached_v_pledges_contact1_name;
CREATE TABLE cached_v_pledges_contact1_name AS SELECT * FROM v_pledges_contact1_name;
DROP TABLE IF EXISTS cached_v_pledges_contact1_telephone;
CREATE TABLE cached_v_pledges_contact1_telephone AS SELECT * FROM v_pledges_contact1_telephone;
DROP TABLE IF EXISTS cached_v_pledges_contact1_title;
CREATE TABLE cached_v_pledges_contact1_title AS SELECT * FROM v_pledges_contact1_title;
DROP TABLE IF EXISTS cached_v_pledges_contact2_address;
CREATE TABLE cached_v_pledges_contact2_address AS SELECT * FROM v_pledges_contact2_address;
DROP TABLE IF EXISTS cached_v_pledges_contact2_alternate;
CREATE TABLE cached_v_pledges_contact2_alternate AS SELECT * FROM v_pledges_contact2_alternate;
DROP TABLE IF EXISTS cached_v_pledges_contact2_email;
CREATE TABLE cached_v_pledges_contact2_email AS SELECT * FROM v_pledges_contact2_email;
DROP TABLE IF EXISTS cached_v_pledges_contact2_ministry;
CREATE TABLE cached_v_pledges_contact2_ministry AS SELECT * FROM v_pledges_contact2_ministry;
DROP TABLE IF EXISTS cached_v_pledges_contact2_name;
CREATE TABLE cached_v_pledges_contact2_name AS SELECT * FROM v_pledges_contact2_name;
DROP TABLE IF EXISTS cached_v_pledges_contact2_telephone;
CREATE TABLE cached_v_pledges_contact2_telephone AS SELECT * FROM v_pledges_contact2_telephone;
DROP TABLE IF EXISTS cached_v_pledges_contact2_title;
CREATE TABLE cached_v_pledges_contact2_title AS SELECT * FROM v_pledges_contact2_title;
DROP TABLE IF EXISTS cached_v_pledges_date_hierarchy;
CREATE TABLE cached_v_pledges_date_hierarchy AS SELECT * FROM v_pledges_date_hierarchy;
DROP TABLE IF EXISTS cached_v_pledges_districts;
CREATE TABLE cached_v_pledges_districts AS SELECT * FROM v_pledges_districts;
DROP TABLE IF EXISTS cached_v_pledges_donor;
CREATE TABLE cached_v_pledges_donor AS SELECT * FROM v_pledges_donor;
DROP TABLE IF EXISTS cached_v_pledges_donor_group;
CREATE TABLE cached_v_pledges_donor_group AS SELECT * FROM v_pledges_donor_group;
DROP TABLE IF EXISTS cached_v_pledges_donor_type;
CREATE TABLE cached_v_pledges_donor_type AS SELECT * FROM v_pledges_donor_type;
DROP TABLE IF EXISTS cached_v_pledges_funding;
CREATE TABLE cached_v_pledges_funding AS SELECT * FROM v_pledges_funding;
DROP TABLE IF EXISTS cached_v_pledges_funding_st;
CREATE TABLE cached_v_pledges_funding_st AS SELECT * FROM v_pledges_funding_st;
DROP TABLE IF EXISTS cached_v_pledges_programs;
CREATE TABLE cached_v_pledges_programs AS SELECT * FROM v_pledges_programs;
DROP TABLE IF EXISTS cached_v_pledges_projects;
CREATE TABLE cached_v_pledges_projects AS SELECT * FROM v_pledges_projects;
DROP TABLE IF EXISTS cached_v_pledges_titles;
CREATE TABLE cached_v_pledges_titles AS SELECT * FROM v_pledges_titles;
DROP TABLE IF EXISTS cached_v_pledges_type_of_assistance;
CREATE TABLE cached_v_pledges_type_of_assistance AS SELECT * FROM v_pledges_type_of_assistance;
DROP TABLE IF EXISTS cached_v_pledges_zones;
CREATE TABLE cached_v_pledges_zones AS SELECT * FROM v_pledges_zones;

DROP TABLE IF EXISTS cached_v_sectorloc;
CREATE TABLE cached_v_sectorloc AS SELECT * FROM v_sectorloc;




DROP TABLE IF EXISTS cached_amp_activity;
CREATE TABLE cached_amp_activity AS  select * from amp_activity;

TRUNCATE cached_amp_activity_group;
INSERT INTO cached_amp_activity_group SELECT * FROM amp_activity_group WHERE amp_activity_last_version_id IS NOT NULL;

DROP TABLE IF EXISTS cached_v_donor_date_hierarchy;
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy limit 0;
ALTER TABLE cached_v_donor_date_hierarchy ALTER COLUMN quarter_name TYPE varchar(2);
insert into cached_v_donor_date_hierarchy SELECT * FROM v_donor_date_hierarchy;


DROP TABLE IF EXISTS cached_v_m_regions;
CREATE TABLE cached_v_m_regions AS SELECT * FROM v_regions_cached;

CREATE INDEX idx_amp_activity ON cached_v_m_regions (amp_activity_id);
CREATE INDEX idx_region_id ON cached_v_m_regions (region_id);

CREATE INDEX idx_st_activity ON cached_v_status (amp_activity_id);
CREATE INDEX idx_st_id ON cached_v_status (amp_status_id);


CREATE INDEX idx_mpsec_activity ON cached_v_m_sectors (amp_activity_id);
CREATE INDEX idx_mpsec_name ON cached_v_m_sectors (sectorname);
CREATE INDEX idx_mpsubsec_name ON cached_v_m_sectors (subsectorname);
CREATE INDEX idx_msubsubpsec_name ON cached_v_m_sectors (subsubsectorname);
CREATE INDEX idx_mpesc_id ON cached_v_m_sectors (amp_sector_id);
CREATE INDEX idx_mpesc_per ON cached_v_m_sectors (sector_percentage);

CREATE INDEX idx_mssec_activity ON cached_v_m_secondary_sectors (amp_activity_id);
CREATE INDEX idx_mssec_name ON cached_v_m_secondary_sectors (sectorname);
CREATE INDEX idx_mssubsec_name ON cached_v_m_secondary_sectors (subsectorname);
CREATE INDEX idx_mssubsubpsec_name ON cached_v_m_secondary_sectors (subsubsectorname);
CREATE INDEX idx_mssesc_id ON cached_v_m_secondary_sectors (amp_sector_id);
CREATE INDEX idx_mssesc_per ON cached_v_m_secondary_sectors (sector_percentage);


CREATE INDEX idx_psec_activity ON cached_v_sectors (amp_activity_id);
CREATE INDEX idx_psec_name ON cached_v_sectors (sectorname);
CREATE INDEX idx_pesc_id ON cached_v_sectors (amp_sector_id);
CREATE INDEX idx_pesc_per ON cached_v_sectors (sector_percentage);

CREATE INDEX idx_ssec_activity ON cached_v_secondary_sectors (amp_activity_id);
CREATE INDEX idx_ssec_name ON cached_v_secondary_sectors (sectorname);
CREATE INDEX idx_sesc_id ON cached_v_secondary_sectors (amp_sector_id);
CREATE INDEX idx_sesc_per ON cached_v_secondary_sectors (sector_percentage);

CREATE INDEX idx_psub_activity ON cached_v_sub_sectors (amp_activity_id);
CREATE INDEX idx_psub_name ON cached_v_sub_sectors (sectorname);
CREATE INDEX idx_psub_id ON cached_v_sub_sectors (amp_sector_id);
CREATE INDEX idx_psub_per ON cached_v_sub_sectors (sector_percentage);

CREATE INDEX idx_psubsub_activity ON cached_v_sub_sub_sectors (amp_activity_id);
CREATE INDEX idx_psubsub_name ON cached_v_sub_sub_sectors (name);
CREATE INDEX idx_psubsub_id ON cached_v_sub_sub_sectors (amp_sector_id);
CREATE INDEX idx_psubsub_per ON cached_v_sub_sub_sectors (sector_percentage);

CREATE INDEX idx_ssub_activity ON cached_v_secondary_sub_sectors (amp_activity_id);
CREATE INDEX idx_ssub_name ON cached_v_secondary_sub_sectors (sectorname);
CREATE INDEX idx_ssub_per ON cached_v_secondary_sub_sectors (sector_percentage);
CREATE INDEX idx_ssub_id ON cached_v_secondary_sub_sectors (amp_sector_id);

CREATE INDEX idx_npl0_activity ON cached_v_nationalobjectives_level_0 (amp_activity_id);
CREATE INDEX idx_npidl0_pidid ON cached_v_nationalobjectives_level_0 (amp_program_id);
CREATE INDEX idx_npl0_name ON cached_v_nationalobjectives_level_0 (name);

CREATE INDEX idx_npidl1_pidid ON cached_v_nationalobjectives_level_1 (amp_program_id);
CREATE INDEX idx_npl1_activity ON cached_v_nationalobjectives_level_1 (amp_activity_id);
CREATE INDEX idx_npl1_name ON cached_v_nationalobjectives_level_1 (name);
CREATE INDEX idx_npidl2_pidid ON cached_v_nationalobjectives_level_2 (amp_program_id);
CREATE INDEX idx_npl2_activity ON cached_v_nationalobjectives_level_2 (amp_activity_id);
CREATE INDEX idx_npl2_name ON cached_v_nationalobjectives_level_2 (name);
CREATE INDEX idx_npidl3_pidid ON cached_v_nationalobjectives_level_3 (amp_program_id);
CREATE INDEX idx_npl3_activity ON cached_v_nationalobjectives_level_3 (amp_activity_id);
CREATE INDEX idx_npl3_name ON cached_v_nationalobjectives_level_3 (name);
CREATE INDEX idx_npidl4_pidid ON cached_v_nationalobjectives_level_4 (amp_program_id);
CREATE INDEX idx_npl4_activity ON cached_v_nationalobjectives_level_4 (amp_activity_id);
CREATE INDEX idx_npl4_name ON cached_v_nationalobjectives_level_4 (name);
CREATE INDEX idx_npidl5_pidid ON cached_v_nationalobjectives_level_5 (amp_program_id);
CREATE INDEX idx_npl5_activity ON cached_v_nationalobjectives_level_5 (amp_activity_id);
CREATE INDEX idx_npl5_name ON cached_v_nationalobjectives_level_5 (name);
CREATE INDEX idx_npidl6_pidid ON cached_v_nationalobjectives_level_6 (amp_program_id);
CREATE INDEX idx_npl6_activity ON cached_v_nationalobjectives_level_6 (amp_activity_id);
CREATE INDEX idx_npl6_name ON cached_v_nationalobjectives_level_6 (name);
CREATE INDEX idx_npidl7_pidid ON cached_v_nationalobjectives_level_7 (amp_program_id);
CREATE INDEX idx_npl7_activity ON cached_v_nationalobjectives_level_7 (amp_activity_id);
CREATE INDEX idx_npl7_name ON cached_v_nationalobjectives_level_7 (name);
CREATE INDEX idx_npidl8_pidid ON cached_v_nationalobjectives_level_8 (amp_program_id);
CREATE INDEX idx_npl8_activity ON cached_v_nationalobjectives_level_8 (amp_activity_id);
CREATE INDEX idx_npl8_name ON cached_v_nationalobjectives_level_8 (name);

CREATE INDEX idx_ppl0_activity ON cached_v_primaryprogram_level_0 (amp_activity_id);
CREATE INDEX idx_ppidl0_pidid ON cached_v_primaryprogram_level_0 (amp_program_id);
CREATE INDEX idx_ppl0_name ON cached_v_primaryprogram_level_0 (name);
CREATE INDEX idx_ppl1_activity ON cached_v_primaryprogram_level_1 (amp_activity_id);
CREATE INDEX idx_ppidl1_pidid ON cached_v_primaryprogram_level_1 (amp_program_id);
CREATE INDEX idx_ppl1_name ON cached_v_primaryprogram_level_1 (name);
CREATE INDEX idx_ppl2_activity ON cached_v_primaryprogram_level_2 (amp_activity_id);
CREATE INDEX idx_ppidl2_pidid ON cached_v_primaryprogram_level_2 (amp_program_id);
CREATE INDEX idx_ppl2_name ON cached_v_primaryprogram_level_2 (name);
CREATE INDEX idx_ppl3_activity ON cached_v_primaryprogram_level_3 (amp_activity_id);
CREATE INDEX idx_ppidl3_pidid ON cached_v_primaryprogram_level_3 (amp_program_id);
CREATE INDEX idx_ppl3_name ON cached_v_primaryprogram_level_3 (name);
CREATE INDEX idx_ppl4_activity ON cached_v_primaryprogram_level_4 (amp_activity_id);
CREATE INDEX idx_ppidl4_pidid ON cached_v_primaryprogram_level_4 (amp_program_id);
CREATE INDEX idx_ppl4_name ON cached_v_primaryprogram_level_4 (name);
CREATE INDEX idx_ppl5_activity ON cached_v_primaryprogram_level_5 (amp_activity_id);
CREATE INDEX idx_ppidl5_pidid ON cached_v_primaryprogram_level_5 (amp_program_id);
CREATE INDEX idx_ppl5_name ON cached_v_primaryprogram_level_5 (name);
CREATE INDEX idx_ppl6_activity ON cached_v_primaryprogram_level_6 (amp_activity_id);
CREATE INDEX idx_ppidl6_pidid ON cached_v_primaryprogram_level_6 (amp_program_id);
CREATE INDEX idx_ppl6_name ON cached_v_primaryprogram_level_6 (name);
CREATE INDEX idx_ppl7_activity ON cached_v_primaryprogram_level_7 (amp_activity_id);
CREATE INDEX idx_ppidl7_pidid ON cached_v_primaryprogram_level_7 (amp_program_id);
CREATE INDEX idx_ppl7_name ON cached_v_primaryprogram_level_7 (name);
CREATE INDEX idx_ppl8_activity ON cached_v_primaryprogram_level_8 (amp_activity_id);
CREATE INDEX idx_ppidl8_pidid ON cached_v_primaryprogram_level_8 (amp_program_id);
CREATE INDEX idx_ppl8_name ON cached_v_primaryprogram_level_8 (name);

CREATE INDEX idx_spidl0_pidid ON cached_v_secondaryprogram_level_0 (amp_program_id);
CREATE INDEX idx_spl0_activity ON cached_v_secondaryprogram_level_0 (amp_activity_id);
CREATE INDEX idx_spl0_name ON cached_v_secondaryprogram_level_0 (name);
CREATE INDEX idx_spidl1_pidid ON cached_v_secondaryprogram_level_1 (amp_program_id);
CREATE INDEX idx_spl1_activity ON cached_v_secondaryprogram_level_1 (amp_activity_id);
CREATE INDEX idx_spl1_name ON cached_v_secondaryprogram_level_1 (name);
CREATE INDEX idx_spidl2_pidid ON cached_v_secondaryprogram_level_2 (amp_program_id);
CREATE INDEX idx_spl2_activity ON cached_v_secondaryprogram_level_2 (amp_activity_id);
CREATE INDEX idx_spl2_name ON cached_v_secondaryprogram_level_2 (name);
CREATE INDEX idx_spidl3_pidid ON cached_v_secondaryprogram_level_3 (amp_program_id);
CREATE INDEX idx_spl3_activity ON cached_v_secondaryprogram_level_3 (amp_activity_id);
CREATE INDEX idx_spl3_name ON cached_v_secondaryprogram_level_3 (name);
CREATE INDEX idx_spidl4_pidid ON cached_v_secondaryprogram_level_4 (amp_program_id);
CREATE INDEX idx_spl4_activity ON cached_v_secondaryprogram_level_4 (amp_activity_id);
CREATE INDEX idx_spl4_name ON cached_v_secondaryprogram_level_4 (name);
CREATE INDEX idx_spidl5_pidid ON cached_v_secondaryprogram_level_5 (amp_program_id);
CREATE INDEX idx_spl5_activity ON cached_v_secondaryprogram_level_5 (amp_activity_id);
CREATE INDEX idx_spl5_name ON cached_v_secondaryprogram_level_5 (name);
CREATE INDEX idx_spidl6_pidid ON cached_v_secondaryprogram_level_6 (amp_program_id);
CREATE INDEX idx_spl6_activity ON cached_v_secondaryprogram_level_6 (amp_activity_id);
CREATE INDEX idx_spl6_name ON cached_v_secondaryprogram_level_6 (name);
CREATE INDEX idx_spidl7_pidid ON cached_v_secondaryprogram_level_7 (amp_program_id);
CREATE INDEX idx_spl7_activity ON cached_v_secondaryprogram_level_7 (amp_activity_id);
CREATE INDEX idx_spl7_name ON cached_v_secondaryprogram_level_7 (name);
CREATE INDEX idx_spidl8_pidid ON cached_v_secondaryprogram_level_8 (amp_program_id);
CREATE INDEX idx_spl8_activity ON cached_v_secondaryprogram_level_8 (amp_activity_id);
CREATE INDEX idx_spl8_name ON cached_v_secondaryprogram_level_8 (name);


CREATE OR REPLACE VIEW v_donor_funding_cached AS
  select aa.amp_activity_id AS amp_activity_id,
         f.amp_funding_id AS amp_funding_id,
         fd.amp_fund_detail_id AS amp_fund_detail_id,
         fd.transaction_type AS transaction_type,
         fd.adjustment_type AS adjustment_type,
         fd.transaction_date AS transaction_date,
         fd.transaction_amount * (
         
         coalesce(rc.location_percentage, 100) / 100 *
         coalesce(pp.program_percentage, 100) / 100 *
         coalesce(sp.program_percentage, 100) / 100 *
         coalesce(np.program_percentage, 100) / 100 *
         coalesce(secs.sector_percentage,100) / 100 *
         coalesce(s.sector_percentage,100) / 100) AS transaction_amount,
         
         d.name AS donor_name,
         c.currency_code AS currency_code,
         cval.id AS terms_assist_id,
         cval.category_value AS terms_assist_name,
         fd.fixed_exchange_rate AS fixed_exchange_rate,
         b.org_grp_name AS org_grp_name,
         ot.org_type AS donor_type_name,
         cval2.category_value AS financing_instrument_name,
         cval2.id AS financing_instrument_id,
         d.amp_org_id AS org_grp_id,
         ot.amp_org_type_id AS org_type_id,
         fd.disbursement_order_rejected AS disb_ord_rej,
         s.sectorname AS p_sectorname,
         s.subsectorname AS p_sub_sectorname,
         s.subsubsectorname AS p_sub_sub_sectorname,
         s.sec_scheme_name AS p_amp_sec_scheme_name,
         

	 secs.sectorname AS s_sectorname,
         secs.subsectorname AS s_sub_sectorname,
         secs.subsubsectorname AS s_sub_sub_sectorname,
         secs.sec_scheme_name AS s_amp_sec_scheme_name,
         
         
         rc.Region AS region,
          pp.name AS primary_program_name,
          ppl1.name as ppl1_name,
          ppl2.name as ppl2_name,
          ppl3.name as ppl3_name,
          ppl4.name as ppl4_name,
          ppl5.name as ppl5_name,
          ppl6.name as ppl6_name,
          ppl7.name as ppl7_name,
          ppl8.name as ppl8_name,
          sp.name AS secondary_program_name,
          spl1.name as spl1_name,
          spl2.name as spl2_name,
          spl3.name as spl3_name,
          spl4.name as spl4_name,
          spl5.name as spl5_name,
          spl6.name as spl6_name,
          spl7.name as spl7_name,
          spl8.name as spl8_name,

          np.name AS national_program_name,
          npl1.name as npl1_name,
          npl2.name as npl2_name,
          npl3.name as npl3_name,
          npl4.name as npl4_name,
          npl5.name as npl5_name,
          npl6.name as npl6_name,
          npl7.name as npl7_name,
          npl8.name as npl8_name
          
  from cached_amp_activity aa join amp_funding f on aa.amp_activity_id = f.amp_activity_id
       join amp_funding_detail fd on f.amp_funding_id = fd.amp_funding_id
       join amp_category_value cval on f.type_of_assistance_category_va = cval.id
       join amp_currency c on fd.amp_currency_id = c.amp_currency_id
       left join amp_organisation d on f.amp_donor_org_id = d.amp_org_id
       join amp_org_group b on d.org_grp_id = b.amp_org_grp_id
       join amp_org_type ot on b.org_type = ot.amp_org_type_id
       join amp_category_value cval2 on f.financing_instr_category_value = cval2.id

       left join cached_v_m_sectors s on aa.amp_activity_id = s.amp_activity_id

       left join cached_v_m_secondary_sectors secs on aa.amp_activity_id = secs.amp_activity_id
       
       left join amp_sector_scheme ss on secs.amp_sector_scheme_id = ss.amp_sec_scheme_id
       join cached_v_m_regions rc on aa.amp_activity_id = rc.amp_activity_id


       left join cached_v_primaryprogram_level_0 pp on f.amp_activity_id = pp.amp_activity_id
       left join cached_v_primaryprogram_level_1 ppl1 on f.amp_activity_id = ppl1.amp_activity_id
       left join cached_v_primaryprogram_level_2 ppl2 on f.amp_activity_id = ppl2.amp_activity_id
       left join cached_v_primaryprogram_level_3 ppl3 on f.amp_activity_id = ppl3.amp_activity_id
       left join cached_v_primaryprogram_level_4 ppl4 on f.amp_activity_id = ppl4.amp_activity_id
       left join cached_v_primaryprogram_level_5 ppl5 on f.amp_activity_id = ppl5.amp_activity_id
       left join cached_v_primaryprogram_level_6 ppl6 on f.amp_activity_id = ppl6.amp_activity_id
       left join cached_v_primaryprogram_level_7 ppl7 on f.amp_activity_id = ppl7.amp_activity_id
       left join cached_v_primaryprogram_level_8 ppl8 on f.amp_activity_id = ppl8.amp_activity_id

       left join cached_v_secondaryprogram_level_0 sp on f.amp_activity_id = sp.amp_activity_id
       left join cached_v_secondaryprogram_level_1 spl1 on f.amp_activity_id = spl1.amp_activity_id
       left join cached_v_secondaryprogram_level_2 spl2 on f.amp_activity_id = spl2.amp_activity_id
       left join cached_v_secondaryprogram_level_3 spl3 on f.amp_activity_id = spl3.amp_activity_id
       left join cached_v_secondaryprogram_level_4 spl4 on f.amp_activity_id = spl4.amp_activity_id
       left join cached_v_secondaryprogram_level_5 spl5 on f.amp_activity_id = spl5.amp_activity_id
       left join cached_v_secondaryprogram_level_6 spl6 on f.amp_activity_id = spl6.amp_activity_id
       left join cached_v_secondaryprogram_level_7 spl7 on f.amp_activity_id = spl7.amp_activity_id
       left join cached_v_secondaryprogram_level_8 spl8 on f.amp_activity_id = spl8.amp_activity_id

       left join cached_v_nationalobjectives_level_0 np on f.amp_activity_id = np.amp_activity_id
       left join cached_v_nationalobjectives_level_1 npl1 on f.amp_activity_id = npl1.amp_activity_id
       left join cached_v_nationalobjectives_level_2 npl2 on f.amp_activity_id = npl2.amp_activity_id
       left join cached_v_nationalobjectives_level_3 npl3 on f.amp_activity_id = npl3.amp_activity_id
       left join cached_v_nationalobjectives_level_4 npl4 on f.amp_activity_id = npl4.amp_activity_id
       left join cached_v_nationalobjectives_level_5 npl5 on f.amp_activity_id = npl5.amp_activity_id
       left join cached_v_nationalobjectives_level_6 npl6 on f.amp_activity_id = npl6.amp_activity_id
       left join cached_v_nationalobjectives_level_7 npl7 on f.amp_activity_id = npl7.amp_activity_id
       left join cached_v_nationalobjectives_level_8 npl8 on f.amp_activity_id = npl8.amp_activity_id

 group by fd.transaction_type,
           fd.amp_fund_detail_id,
           fd.disbursement_order_rejected,
           fd.adjustment_type,
           fd.transaction_amount,
           fd.transaction_date,
           f.amp_funding_id,
           aa.amp_activity_id,
           s.amp_sector_id,
           s.sectorname,
           secs.amp_sector_id,
           secs.sectorname,
           secs.amp_activity_id,
           ss.amp_sec_scheme_id,
           ot.org_type,
           ot.amp_org_type_id,
           d.amp_org_id,
           cval2.id,
           cval2.category_value,
           cval.category_value,
           cval.id,
           b.org_grp_name,
           fd.fixed_exchange_rate,
           c.currency_code,
           d.name,
           rc.region_id,
           rc.Region,
           rc.amp_activity_id,
           pp.amp_program_id,
      	   ppl2.amp_program_id,
           ppl3.amp_program_id,
           ppl4.amp_program_id,
           ppl5.amp_program_id,
           ppl6.amp_program_id,
           ppl7.amp_program_id,
           ppl8.amp_program_id,
           sp.amp_program_id,
           spl2.amp_program_id,
           spl3.amp_program_id,
           spl4.amp_program_id,
           spl5.amp_program_id,
           spl6.amp_program_id,
           spl7.amp_program_id,
           spl8.amp_program_id,
           np.amp_program_id,
           npl1.amp_program_id,
           npl2.amp_program_id,
           npl3.amp_program_id,
           npl4.amp_program_id,
           npl5.amp_program_id,
           npl6.amp_program_id,
           npl7.amp_program_id,
           npl8.amp_program_id,
           rc.location_percentage,
           pp.program_percentage,
           sp.program_percentage,
           np.program_percentage,
           secs.sector_percentage,
           
           s.sector_percentage,
           s.sec_scheme_name,
	   pp.name ,
	   ppl1.name,
	   ppl2.name,
	   ppl3.name,
           ppl4.name,
           ppl5.name,
           ppl6.name,
           ppl7.name,
           ppl8.name,
           sp.name,
           spl1.name,
           spl2.name,
           spl3.name,
           spl4.name,
           spl5.name,
           spl6.name,
           spl7.name,
           spl8.name,
	   np.name,
           npl1.name,
           npl2.name,
           npl3.name,
           npl4.name,
           npl5.name,
           npl6.name,
           npl7.name,
           npl8.name,
           p_sub_sectorname,
           p_sub_sub_sectorname,
           s_sub_sectorname,
           s_sub_sub_sectorname,
           s_amp_sec_scheme_name
           
  order by aa.amp_activity_id,
           s.sectorname,
           fd.transaction_type,
           f.amp_funding_id;

DROP TABLE IF EXISTS cached_v_m_donor_funding;
CREATE TABLE cached_v_m_donor_funding AS SELECT * FROM v_donor_funding_cached;

CREATE INDEX idx_donor_activity ON cached_v_m_donor_funding (amp_activity_id);
CREATE INDEX idx_donor_name ON cached_v_m_donor_funding (donor_name);
CREATE INDEX idx_curr_code ON cached_v_m_donor_funding (currency_code);
CREATE INDEX idx_financ_intrum ON cached_v_m_donor_funding (financing_instrument_name);
CREATE INDEX idx_donor_type ON cached_v_m_donor_funding (donor_type_name);
CREATE INDEX idx_donor_group ON cached_v_m_donor_funding (org_grp_name);
CREATE INDEX idx_region_name ON cached_v_m_donor_funding (Region);
CREATE INDEX idx_pri_prog_name ON cached_v_m_donor_funding (primary_program_name);
CREATE INDEX idx_sec_prog_name ON cached_v_m_donor_funding (secondary_program_name);
CREATE INDEX idx_pri_sector_name ON cached_v_m_donor_funding (p_sectorname);
CREATE INDEX idx_nac_prog_name ON cached_v_m_donor_funding (national_program_name);
