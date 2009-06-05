DROP TABLE IF EXISTS cached_v_sectors;
CREATE TABLE cached_v_sectors AS SELECT * FROM v_sectors_cached;
DROP TABLE IF EXISTS cached_amp_activity;
CREATE TABLE cached_amp_activity LIKE amp_activity;
INSERT INTO cached_amp_activity SELECT * FROM amp_activity;

DROP TABLE IF EXISTS cached_v_donor_date_hierarchy;
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy limit 0,0;
alter table cached_v_donor_date_hierarchy modify quarter_name varchar(2);
insert into cached_v_donor_date_hierarchy SELECT * FROM v_donor_date_hierarchy;

DROP TABLE IF EXISTS cached_v_donor_funding;
CREATE TABLE cached_v_donor_funding AS SELECT * FROM v_donor_funding;
DROP TABLE IF EXISTS cached_v_regions;
CREATE TABLE cached_v_regions AS SELECT * FROM `v_regions_cached`;
DROP TABLE IF EXISTS cached_v_primary_program;
CREATE TABLE cached_v_primary_program AS SELECT * FROM `v_primaryprogram_cached`;
DROP TABLE IF EXISTS cached_v_secunday_program;
CREATE TABLE cached_v_secunday_program AS SELECT * FROM `v_secondaryprogram_cached`;
DROP TABLE IF EXISTS cached_v_status;
CREATE TABLE cached_v_status AS SELECT * FROM v_status;

CREATE INDEX idx_amp_activity ON cached_v_donor_date_hierarchy(amp_activity_id);
CREATE INDEX idx_fund_det_id ON cached_v_donor_date_hierarchy(amp_fund_detail_id);
CREATE INDEX idx_year ON cached_v_donor_date_hierarchy(year);
CREATE INDEX idx_month ON cached_v_donor_date_hierarchy(month);
CREATE INDEX idx_quarter ON cached_v_donor_date_hierarchy(quarter);

CREATE INDEX idx_amp_activity ON `cached_v_donor_funding`(amp_activity_id);
CREATE INDEX idx_donor_name ON `cached_v_donor_funding`(donor_name);
CREATE INDEX idx_curr_code ON `cached_v_donor_funding`(currency_code);
CREATE INDEX idx_financ_intrum ON `cached_v_donor_funding`(financing_instrument_name);
CREATE INDEX idx_donor_type ON `cached_v_donor_funding`(donor_type_name);
CREATE INDEX idx_donor_group ON `cached_v_donor_funding`(org_grp_name);

CREATE INDEX idx_amp_activity ON `cached_v_sectors`(amp_activity_id);
CREATE INDEX idx_sector_id ON `cached_v_sectors`(amp_sector_id);
CREATE INDEX idx_sector_name ON `cached_v_sectors`(sectorname);
CREATE INDEX idx_trans_amount ON `cached_v_sectors`(transaction_amount);
CREATE INDEX idx_trans_type ON `cached_v_sectors`(transaction_type);
CREATE INDEX idx_adj_type ON `cached_v_sectors`(adjustment_type);
CREATE INDEX idx_currency_code ON `cached_v_sectors`(currency_code);

CREATE INDEX idx_amp_activity ON `cached_v_regions`(amp_activity_id);
CREATE INDEX idx_region_id ON `cached_v_regions`(region_id);
CREATE INDEX idx_region_name ON `cached_v_regions`(Region);

CREATE INDEX idx_amp_activity ON `cached_v_primary_program`(amp_activity_id);
CREATE INDEX idx_prog_name ON `cached_v_primary_program`(name);
CREATE INDEX idx_prog_id ON `cached_v_primary_program`(amp_program_id);
CREATE INDEX idx_prog_sett ON `cached_v_primary_program`(program_setting);

CREATE INDEX idx_amp_activity ON `cached_v_secunday_program`(amp_activity_id);
CREATE INDEX idx_prog_name ON `cached_v_secunday_program`(name);
CREATE INDEX idx_prog_id ON `cached_v_secunday_program`(amp_program_id);
CREATE INDEX idx_prog_sett ON `cached_v_secunday_program`(program_setting);

CREATE INDEX idx_amp_activity ON `cached_v_status`(amp_activity_id);
CREATE INDEX idx_st_name ON `cached_v_status`(name);
CREATE INDEX idx_st_id ON `cached_v_status`(amp_status_id);
