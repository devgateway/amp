call drop_table_if_exist ('cached_amp_activity');
CREATE TABLE cached_amp_activity as select * from amp_activity;
call drop_table_if_exist ('cached_v_donor_date_hierarchy');
call drop_table_if_exist ('cached_v_sectors');
CREATE TABLE cached_v_sectors AS SELECT * FROM v_sectors_cached;
call drop_table_if_exist ('cached_amp_activity');
CREATE TABLE cached_amp_activity as select * from amp_activity;
call drop_table_if_exist ('cached_v_donor_date_hierarchy');
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy where rownum=0;
alter table cached_v_donor_date_hierarchy modify quarter_name varchar(2);
insert into cached_v_donor_date_hierarchy SELECT * FROM v_donor_date_hierarchy;
call drop_table_if_exist ('cached_v_donor_funding');
CREATE TABLE cached_v_donor_funding AS SELECT * FROM v_donor_funding_cached;
call drop_table_if_exist ('cached_v_regions');
CREATE TABLE cached_v_regions AS SELECT * FROM v_regions_cached;
call drop_table_if_exist ('cached_v_primary_program');
CREATE TABLE cached_v_primary_program AS SELECT * FROM v_primaryprogram_cached;
call drop_table_if_exist ('cached_v_secundary_program'); 
CREATE TABLE cached_v_secundary_program AS SELECT * FROM v_secondaryprogram_cached;
call drop_table_if_exist ('cached_v_status'); 
CREATE TABLE cached_v_status AS SELECT * FROM v_status;

CREATE INDEX idx_amp_activity_dd ON CACHED_V_DONOR_DATE_HIERARCHY(AMP_ACTIVITY_ID);
CREATE INDEX idx_fund_det_id_dd ON CACHED_V_DONOR_DATE_HIERARCHY(AMP_FUND_DETAIL_ID);
CREATE INDEX idx_year_dd ON CACHED_V_DONOR_DATE_HIERARCHY("year");
CREATE INDEX idx_month_dd ON CACHED_V_DONOR_DATE_HIERARCHY("month");
CREATE INDEX idx_quarter_dd ON CACHED_V_DONOR_DATE_HIERARCHY("QUARTER");

CREATE INDEX idx_amp_activity_vdf ON CACHED_V_DONOR_FUNDING(AMP_ACTIVITY_ID);
CREATE INDEX idx_donor_name_vdf ON CACHED_V_DONOR_FUNDING(DONOR_NAME);
CREATE INDEX idx_curr_code_vdf ON CACHED_V_DONOR_FUNDING(CURRENCY_CODE);
CREATE INDEX idx_financ_intrum_vdf ON CACHED_V_DONOR_FUNDING(FINANCING_INSTRUMENT_NAME);
CREATE INDEX idx_donor_type_vdf ON CACHED_V_DONOR_FUNDING(DONOR_TYPE_NAME);
CREATE INDEX idx_donor_group_vdf ON CACHED_V_DONOR_FUNDING(ORG_GRP_NAME);

CREATE INDEX idx_amp_activity_vs ON CACHED_V_SECTORS(AMP_ACTIVITY_ID);
CREATE INDEX idx_sector_id_vs ON CACHED_V_SECTORS(AMP_SECTOR_ID);
CREATE INDEX idx_sector_name_vs ON CACHED_V_SECTORS(SECTORNAME);
CREATE INDEX idx_trans_amount_vs ON CACHED_V_SECTORS(TRANSACTION_AMOUNT);
CREATE INDEX idx_trans_type_vs ON CACHED_V_SECTORS(TRANSACTION_TYPE);
CREATE INDEX idx_adj_type_vs ON CACHED_V_SECTORS(ADJUSTMENT_TYPE);
CREATE INDEX idx_currency_code_vs ON CACHED_V_SECTORS(CURRENCY_CODE);

CREATE INDEX idx_amp_activity_r ON CACHED_V_REGIONS(AMP_ACTIVITY_ID);
CREATE INDEX idx_region_id_r ON CACHED_V_REGIONS(REGION_ID);
CREATE INDEX idx_region_name_r ON CACHED_V_REGIONS(REGION);

CREATE INDEX idx_amp_activity_pp ON CACHED_V_PRIMARY_PROGRAM(AMP_ACTIVITY_ID);
CREATE INDEX idx_prog_name_pp ON CACHED_V_PRIMARY_PROGRAM(NAME);
CREATE INDEX idx_prog_id_pp ON CACHED_V_PRIMARY_PROGRAM(AMP_PROGRAM_ID);
CREATE INDEX idx_prog_sett_pp ON CACHED_V_PRIMARY_PROGRAM(PROGRAM_SETTING);

CREATE INDEX idx_amp_activity_vsp ON CACHED_V_SECUNDARY_PROGRAM(AMP_ACTIVITY_ID);
CREATE INDEX idx_prog_name_vsp ON CACHED_V_SECUNDARY_PROGRAM(NAME);
CREATE INDEX idx_prog_id_vsp ON CACHED_V_SECUNDARY_PROGRAM(AMP_PROGRAM_ID);
CREATE INDEX idx_prog_sett_vsp ON CACHED_V_SECUNDARY_PROGRAM(PROGRAM_SETTING);

CREATE INDEX idx_amp_activity_st ON CACHED_V_STATUS(AMP_ACTIVITY_ID);
CREATE INDEX idx_st_name_st ON CACHED_V_STATUS(NAME);
CREATE INDEX idx_st_id_st ON CACHED_V_STATUS(AMP_STATUS_ID);
