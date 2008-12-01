DROP TABLE IF EXISTS cached_v_sectors;
CREATE TABLE cached_v_sectors AS SELECT * FROM v_sectors;
DROP TABLE IF EXISTS cached_amp_activity;
CREATE TABLE cached_amp_activity AS SELECT * FROM amp_activity;
DROP TABLE IF EXISTS cached_v_donor_date_hierarchy;
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy;
DROP TABLE IF EXISTS cached_v_donor_funding;
CREATE TABLE cached_v_donor_funding AS SELECT * FROM v_donor_funding;