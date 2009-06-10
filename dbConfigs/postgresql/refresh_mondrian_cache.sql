DROP TABLE IF EXISTS cached_amp_activity;
CREATE TABLE cached_amp_activity as SELECT * FROM amp_activity;

DROP TABLE IF EXISTS cached_v_donor_date_hierarchy;
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy limit 0;
insert into cached_v_donor_date_hierarchy SELECT * FROM v_donor_date_hierarchy;

DROP TABLE IF EXISTS cached_v_donor_funding;
CREATE TABLE cached_v_donor_funding AS SELECT * FROM v_donor_funding_cached;
DROP TABLE IF EXISTS cached_v_regions;
CREATE TABLE cached_v_regions AS SELECT * FROM v_regions_cached;
DROP TABLE IF EXISTS cached_v_primary_program;
CREATE TABLE cached_v_primary_program AS SELECT * FROM v_primaryprogram_cached;
DROP TABLE IF EXISTS cached_v_secunday_program;
CREATE TABLE cached_v_secunday_program AS SELECT * FROM v_secondaryprogram_cached;
DROP TABLE IF EXISTS cached_v_status;
CREATE TABLE cached_v_status AS SELECT * FROM v_status;

CREATE INDEX idx_amp_activity ON cached_v_donor_date_hierarchy(amp_activity_id);
CREATE INDEX idx_fund_det_id ON cached_v_donor_date_hierarchy(amp_fund_detail_id);
CREATE INDEX idx_year ON cached_v_donor_date_hierarchy(year);
CREATE INDEX idx_month ON cached_v_donor_date_hierarchy(month);
CREATE INDEX idx_quarter ON cached_v_donor_date_hierarchy(quarter);

CREATE INDEX idx_amp_activity_cd ON cached_v_donor_funding(amp_activity_id);
CREATE INDEX idx_donor_name_cd  ON cached_v_donor_funding(donor_name);
CREATE INDEX idx_curr_code_cd  ON cached_v_donor_funding(currency_code);
CREATE INDEX idx_financ_intrum_cd  ON cached_v_donor_funding(financing_instrument_name);
CREATE INDEX idx_donor_type_cd  ON cached_v_donor_funding(donor_type_name);
CREATE INDEX idx_donor_group_cd  ON cached_v_donor_funding(org_grp_name);

CREATE INDEX idx_amp_activity_r ON cached_v_regions(amp_activity_id);
CREATE INDEX idx_region_id_r ON cached_v_regions(region_id);
CREATE INDEX idx_region_name_r ON cached_v_regions(Region);

CREATE INDEX idx_amp_activity_pp ON cached_v_primary_program(amp_activity_id);
CREATE INDEX idx_prog_name_pp ON cached_v_primary_program(name);
CREATE INDEX idx_prog_id_pp ON cached_v_primary_program(amp_program_id);
CREATE INDEX idx_prog_sett_pp ON cached_v_primary_program(program_setting);

CREATE INDEX idx_amp_activity_sp ON cached_v_secunday_program(amp_activity_id);
CREATE INDEX idx_prog_name_sp ON cached_v_secunday_program(name);
CREATE INDEX idx_prog_id_sp ON cached_v_secunday_program(amp_program_id);
CREATE INDEX idx_prog_sett_sp ON cached_v_secunday_program(program_setting);

CREATE INDEX idx_amp_activity_st ON cached_v_status(amp_activity_id);
CREATE INDEX idx_name_st ON cached_v_status(name);
CREATE INDEX idx_id_st ON cached_v_status(amp_status_id);