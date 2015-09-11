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
--recreate v_m_sectors
				CREATE OR REPLACE VIEW v_m_sectors AS
				SELECT sa.amp_activity_id, 
				getsectorname(getparentsectorid(s.amp_sector_id)) AS sectorname, 
				getsectorname(getparentsubsectorid(s.amp_sector_id)) AS subsectorname,
				getsectorname(getparentsubsubsectorid(s.amp_sector_id)) AS subsubsectorname,
				getparentsectorid(s.amp_sector_id) AS amp_sector_id, 
				sum(sa.sector_percentage) AS sector_percentage, 
				s.amp_sec_scheme_id AS amp_sector_scheme_id, 
				ss.sec_scheme_name
				   FROM amp_sector_scheme ss
				   JOIN amp_classification_config cc ON cc.name::text = 'Primary'::text AND cc.classification_id = ss.amp_sec_scheme_id
				   JOIN amp_sector s ON s.amp_sec_scheme_id = ss.amp_sec_scheme_id
				   JOIN amp_activity_sector sa ON sa.amp_sector_id = s.amp_sector_id AND sa.classification_config_id = cc.id
				  GROUP BY sa.amp_activity_id, getparentsectorid(s.amp_sector_id), s.amp_sec_scheme_id, ss.sec_scheme_name,getparentsubsectorid(s.amp_sector_id),getparentsubsubsectorid
				
				(s.amp_sector_id)
				  ORDER BY sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id));


DROP TABLE IF EXISTS cached_v_m_sectors CASCADE;
CREATE TABLE cached_v_m_sectors AS SELECT * FROM v_m_sectors;


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

--recreate v_m_secondary_sectors
				CREATE OR REPLACE VIEW v_m_secondary_sectors AS
				SELECT sa.amp_activity_id, 
				getsectorname(getparentsectorid(s.amp_sector_id)) AS sectorname, 
				getparentsectorid(s.amp_sector_id) AS amp_sector_id, 
				getsectorname(getparentsubsectorid(s.amp_sector_id)) AS subsectorname,
				getsectorname(getparentsubsubsectorid(s.amp_sector_id)) AS subsubsectorname,
				sum(sa.sector_percentage) AS sector_percentage, 
				s.amp_sec_scheme_id AS amp_sector_scheme_id,
				ss.sec_scheme_name
				FROM amp_activity_sector sa, amp_sector s, amp_sector_scheme ss
				WHERE s.amp_sector_id = sa.amp_sector_id AND (s.amp_sec_scheme_id IN ( SELECT amp_classification_config.classification_id
				           FROM amp_classification_config
				          WHERE amp_classification_config.name::text = 'Secondary'::text)) AND s.amp_sec_scheme_id = ss.amp_sec_scheme_id
				  GROUP BY sa.amp_activity_id, 
				  getsectorname(getparentsectorid(s.amp_sector_id)), 
				  getparentsectorid(s.amp_sector_id), 
				  s.amp_sec_scheme_id,
				  getparentsubsectorid(s.amp_sector_id),
				  getparentsubsubsectorid(s.amp_sector_id),
				  ss.sec_scheme_name
				  ORDER BY sa.amp_activity_id, getsectorname(getparentsectorid(s.amp_sector_id));

DROP TABLE IF EXISTS cached_v_m_secondary_sectors;
CREATE TABLE cached_v_m_secondary_sectors AS SELECT * FROM v_m_secondary_sectors;

DROP TABLE IF EXISTS cached_v_secondary_sub_sectors;
CREATE TABLE cached_v_secondary_sub_sectors AS SELECT * FROM v_secondary_sub_sectors;

--DROP TABLE IF EXISTS cached_v_convenio_numcont;
--CREATE TABLE cached_v_convenio_numcont AS SELECT * FROM v_convenio_numcont;
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
--DROP TABLE IF EXISTS cached_v_code_chapitre;
--CREATE TABLE cached_v_code_chapitre AS SELECT * FROM v_code_chapitre;
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

CREATE OR REPLACE VIEW v_zones_special AS 
 SELECT ra.amp_activity_id, getlocationname(getlocationidbyimplloc(l.location_id, 'Region'::character varying)) AS region_name, getlocationname(getlocationidbyimplloc(l.location_id, 'Zone'::character varying)) AS location_name, getlocationidbyimplloc(l.location_id, 'Zone'::character varying) AS location_id, sum(ra.location_percentage) AS location_percentage
   FROM amp_activity_location ra, amp_location l
  WHERE ra.amp_location_id = l.amp_location_id AND getlocationidbyimplloc(l.location_id, 'Zone'::character varying) IS NOT NULL
  GROUP BY ra.amp_activity_id, getlocationidbyimplloc(l.location_id, 'Region'::character varying), getlocationidbyimplloc(l.location_id, 'Zone'::character varying);



DROP TABLE IF EXISTS cached_amp_activity;
CREATE TABLE cached_amp_activity AS  select * from amp_activity;

DROP TABLE IF EXISTS cached_amp_activity_group;
CREATE TABLE cached_amp_activity_group AS  select * from amp_activity_group;

DROP TABLE IF EXISTS cached_v_donor_date_hierarchy;
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy limit 0;
ALTER TABLE cached_v_donor_date_hierarchy ALTER COLUMN quarter_name TYPE varchar(2);
insert into cached_v_donor_date_hierarchy SELECT * FROM v_donor_date_hierarchy;

--crate v_regions_cached

CREATE OR REPLACE VIEW v_regions_cached AS SELECT aa.amp_activity_id, al.region_location_id AS region_id, sum(lp.location_percentage) AS location_percentage, acvl.location_name AS region FROM (((amp_activity aa LEFT JOIN amp_activity_location lp ON ((aa.amp_activity_id = lp.amp_activity_id))) LEFT JOIN amp_location al ON ((lp.amp_location_id = al.amp_location_id))) LEFT JOIN amp_category_value_location acvl ON ((al.region_location_id = acvl.id))) GROUP BY aa.amp_activity_id, al.region_location_id, acvl.location_name ORDER BY aa.amp_activity_id, al.region_location_id;;;

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

DROP VIEW v_donor_funding_cached;
CREATE OR REPLACE VIEW v_donor_funding_cached AS
select 
	aa.amp_activity_id AS amp_activity_id,
	f.amp_funding_id AS amp_funding_id,
	fd.amp_fund_detail_id AS amp_fund_detail_id,
	fd.transaction_type AS transaction_type,
	fd.adjustment_type AS adjustment_type,
	fd.transaction_date AS transaction_date,
	fd.transaction_amount * (
		CAST (coalesce(cro.percentage, 100) as NUMERIC) / CAST (100 as NUMERIC) *
		CAST (coalesce(cia.percentage, 100) as NUMERIC) / CAST (100 as NUMERIC) *
		CAST (coalesce(rc.location_percentage, 100) as NUMERIC) / CAST (100 as NUMERIC) *
		CAST (coalesce(npl1.percentage, 100) as NUMERIC) / CAST (100 as NUMERIC) *
		CAST (coalesce(s.sector_percentage,100) as NUMERIC) / CAST (100 as NUMERIC)
		) AS transaction_amount,     
	transaction_amount * (1::double precision / getexchangewithfixed(currency_code, transaction_date, fixed_exchange_rate)) AS transaction_amount_in_base,
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
	s.sec_scheme_name AS p_amp_sec_scheme_name,
	rc.region_name AS region,
	rc.location_name AS zone,
	npl1.name as npl1_name,
	cro.name as RO_name,
	cro.amp_org_id AS ro_id,
	cia.name as IA_name
from cached_amp_activity aa 
	join amp_funding f on aa.amp_activity_id = f.amp_activity_id
	join amp_funding_detail fd on f.amp_funding_id = fd.amp_funding_id
	join amp_category_value cval on f.type_of_assistance_category_va = cval.id
	join amp_currency c on fd.amp_currency_id = c.amp_currency_id
	left join amp_organisation d on f.amp_donor_org_id = d.amp_org_id
	join amp_org_group b on d.org_grp_id = b.amp_org_grp_id
	join amp_org_type ot on b.org_type = ot.amp_org_type_id
	join amp_category_value cval2 on f.financing_instr_category_value = cval2.id
	left join cached_v_m_sectors s on aa.amp_activity_id = s.amp_activity_id
	left join v_zones_special rc on aa.amp_activity_id = rc.amp_activity_id
	left join cached_v_responsible_organisation cro on aa.amp_activity_id = cro.amp_activity_id
	left join cached_v_implementing_agency cia on aa.amp_activity_id = cia.amp_activity_id
	left join cached_v_nationalobjectives_level_1 npl1 on f.amp_activity_id = npl1.amp_activity_id
  where aa.approval_status in('approved','startedapproved') and aa.amp_team_id is not null and aa.draft = false 
  group by fd.transaction_type,
           fd.amp_fund_detail_id,
           fd.disbursement_order_rejected,
           fd.adjustment_type,
           fd.transaction_amount,
           fd.transaction_date,
           f.amp_funding_id,
           aa.amp_activity_id,
           s.sectorname,
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
           rc.region_name,
           rc.location_name,
           rc.amp_activity_id,
           cro.name,
           cia.name,
           npl1.amp_program_id,
           rc.location_percentage,
           cro.percentage,
		   cro.amp_org_id,
           cia.percentage,
           npl1.program_percentage,
           s.sector_percentage,
           s.sec_scheme_name,
           npl1.name
order by aa.amp_activity_id,
           s.sectorname,
           fd.transaction_type,
           f.amp_funding_id;

DROP TABLE IF EXISTS cached_v_m_donor_funding;
CREATE TABLE cached_v_m_donor_funding AS SELECT * FROM v_donor_funding_cached;

DROP TABLE IF EXISTS cached_v_m_donor_funding;
CREATE TABLE cached_v_m_donor_funding AS SELECT * FROM v_donor_funding_cached;

CREATE INDEX idx_donor_activity ON cached_v_m_donor_funding (amp_activity_id);
CREATE INDEX idx_donor_name ON cached_v_m_donor_funding (donor_name);
CREATE INDEX idx_curr_code ON cached_v_m_donor_funding (currency_code);
CREATE INDEX idx_financ_intrum ON cached_v_m_donor_funding (financing_instrument_name);
CREATE INDEX idx_donor_type ON cached_v_m_donor_funding (donor_type_name);
CREATE INDEX idx_donor_group ON cached_v_m_donor_funding (org_grp_name);
CREATE INDEX idx_region_name ON cached_v_m_donor_funding (Region);
CREATE INDEX idx_pri_sector_name ON cached_v_m_donor_funding (p_sectorname);

DROP TABLE IF EXISTS pentaho_dependence_aid_by_ministry;
CREATE TABLE pentaho_dependence_aid_by_ministry AS (
SELECT cvmdf.ro_id AS ministry_id,
cvmdf.ro_name AS ministry_name,
(SELECT EXTRACT(YEAR FROM ped.value_date)) AS year_value,
ped.budget_value,
(SELECT 
	SUM ((1/getExchangeWithFixed(cvmdf2.currency_code,cvmdf2.transaction_date,cvmdf2.fixed_exchange_rate))* cvmdf2.transaction_amount *getExchange('USD',cvmdf2.transaction_date)) AS suma 
	FROM cached_v_m_donor_funding cvmdf2 
	WHERE cvmdf2.ro_id = ped.amp_org_id 
	AND (SELECT EXTRACT(YEAR FROM ped.value_date))=(SELECT EXTRACT(YEAR FROM cvmdf2.transaction_date))
	AND cvmdf2.adjustment_type = getCategoryValueId('Planned') 
	AND cvmdf2.amp_activity_id IN (SELECT DISTINCT amp_activity_id FROM amp_activities_categoryvalues WHERE amp_categoryvalue_id = getCategoryValueIdbyclass('On Budget','activity_budget'))
	AND cvmdf2.transaction_type = 1	
) AS measure_value
FROM cached_v_m_donor_funding cvmdf 
INNER JOIN pentaho_external_data ped ON cvmdf.ro_id = ped.amp_org_id
GROUP BY cvmdf.ro_id, ped.value_date, year_value, ped.budget_value, cvmdf.ro_name, ped.amp_org_id
);

DROP TABLE IF EXISTS pentaho_off_budget_vs_state_budget;
CREATE TABLE pentaho_off_budget_vs_state_budget AS (
SELECT cvmdf.ro_id AS ministry_id,
cvmdf.ro_name AS ministry_name,
(SELECT EXTRACT(YEAR FROM ped.value_date)) AS year_value,
ped.budget_value,
(SELECT 
	SUM ((1/getExchangeWithFixed(cvmdf2.currency_code,cvmdf2.transaction_date,cvmdf2.fixed_exchange_rate))* cvmdf2.transaction_amount *getExchange('USD',cvmdf2.transaction_date)) AS suma 
	FROM cached_v_m_donor_funding cvmdf2 
	WHERE cvmdf2.ro_id = ped.amp_org_id 
	AND (SELECT EXTRACT(YEAR FROM ped.value_date))=(SELECT EXTRACT(YEAR FROM cvmdf2.transaction_date))
	AND cvmdf2.adjustment_type = getCategoryValueId('Actual') 
	AND cvmdf2.amp_activity_id IN (SELECT DISTINCT amp_activity_id FROM amp_activities_categoryvalues WHERE amp_categoryvalue_id = getCategoryValueIdbyclass('Off Budget','activity_budget'))
	AND cvmdf2.transaction_type = 1
	AND cvmdf2.org_type_id <> 10
	AND cvmdf2.terms_assist_id <> 172
) AS measure_value
FROM cached_v_m_donor_funding cvmdf 
INNER JOIN pentaho_external_data ped ON cvmdf.ro_id = ped.amp_org_id
GROUP BY cvmdf.ro_id, ped.value_date, year_value, ped.budget_value, cvmdf.ro_name, ped.amp_org_id
);

DROP TABLE IF EXISTS pentaho_absorption_rate;
CREATE TABLE pentaho_absorption_rate AS (
SELECT cvmdf.ro_id AS ministry_id,
cvmdf.ro_name AS ministry_name,
(SELECT EXTRACT(YEAR FROM ped.value_date)) AS year_value,
ped.expenditure_ministry_value,
(SELECT 
	SUM ((1/getExchangeWithFixed(cvmdf2.currency_code,cvmdf2.transaction_date,cvmdf2.fixed_exchange_rate))* cvmdf2.transaction_amount *getExchange('USD',cvmdf2.transaction_date)) AS suma 
	FROM cached_v_m_donor_funding cvmdf2 
	WHERE cvmdf2.ro_id = ped.amp_org_id 
	AND (SELECT EXTRACT(YEAR FROM ped.value_date))=(SELECT EXTRACT(YEAR FROM cvmdf2.transaction_date))
	AND cvmdf2.adjustment_type = getCategoryValueId('Actual') 
	AND cvmdf2.transaction_type = 1	
) AS measure_value
FROM cached_v_m_donor_funding cvmdf 
INNER JOIN pentaho_external_data ped ON cvmdf.ro_id = ped.amp_org_id
GROUP BY cvmdf.ro_id, ped.value_date, year_value, ped.expenditure_ministry_value, cvmdf.ro_name, ped.amp_org_id
);

