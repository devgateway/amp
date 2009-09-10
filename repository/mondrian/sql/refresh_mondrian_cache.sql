DROP TABLE IF EXISTS cached_amp_activity;
CREATE TABLE cached_amp_activity LIKE amp_activity;
INSERT INTO cached_amp_activity SELECT * FROM amp_activity;

DROP TABLE IF EXISTS cached_v_donor_date_hierarchy;
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy limit 0,0;
alter table cached_v_donor_date_hierarchy modify quarter_name varchar(2);
insert into cached_v_donor_date_hierarchy SELECT * FROM v_donor_date_hierarchy;

DROP TABLE IF EXISTS cached_v_status;
CREATE TABLE cached_v_status AS SELECT * FROM v_status;

DROP TABLE IF EXISTS `cached_v_regions`;
CREATE TABLE cached_v_regions AS SELECT * FROM `v_regions_cached`;

DROP TABLE IF EXISTS `cached_v_primary_program`;
CREATE TABLE cached_v_primary_program AS SELECT * FROM `v_primaryprogram`;

DROP TABLE IF EXISTS `cached_v_secondary_program`;
CREATE TABLE cached_v_secondary_program AS SELECT * FROM `v_secondaryprogram`;

DROP TABLE IF EXISTS `cached_v_national_program`;
CREATE TABLE cached_v_national_program AS SELECT * FROM `v_nationalobjectives_level_1`;

DROP TABLE IF EXISTS `cached_v_sub_sector`;
CREATE TABLE cached_v_sub_sector AS SELECT * FROM `v_sub_sectors`;

DROP TABLE IF EXISTS `cached_v_primary_sector`;
CREATE TABLE `cached_v_primary_sector` AS SELECT * FROM `v_sectors`;

DROP TABLE IF EXISTS `cached_v_secondary_sector`;
CREATE TABLE `cached_v_secondary_sector` AS SELECT * FROM `v_secondary_sectors`;

DROP TABLE IF EXISTS `cached_v_sec_sub_sector`;
CREATE TABLE cached_v_sec_sub_sector AS SELECT * FROM `v_secondary_sub_sectors`;

DROP TABLE IF EXISTS cached_v_donor_funding;
CREATE TABLE cached_v_donor_funding AS SELECT * FROM `v_donor_funding_cached`;

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
CREATE INDEX idx_region_name ON `cached_v_donor_funding`(Region);
CREATE INDEX idx_pri_prog_name ON `cached_v_donor_funding`(primary_program_name);
CREATE INDEX idx_sec_prog_name ON `cached_v_donor_funding`(secondary_program_name);
CREATE INDEX idx_nac_prog_name ON `cached_v_donor_funding`(national_program_name);
CREATE INDEX idx_pri_sector_name ON `cached_v_donor_funding`(p_sectorname);

CREATE INDEX idx_amp_activity ON `cached_v_status`(amp_activity_id);
CREATE INDEX idx_st_name ON `cached_v_status`(name);
CREATE INDEX idx_st_id ON `cached_v_status`(amp_status_id);
