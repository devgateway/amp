DROP TABLE IF EXISTS test_v_component_funding;
CREATE TABLE test_v_component_funding (
  amp_activity_id bigint(20) default NULL,
  amp_component_funding_id bigint(20) NOT NULL default '0',
  amp_fund_detail_id bigint(20) NOT NULL default '0',
  component_name varchar(255) character set latin1 default NULL,
  transaction_type int(11) default NULL,
  adjustment_type int(11) default NULL,
  transaction_date datetime default NULL,
  transaction_amount double default NULL,
  currency_id bigint(20) default NULL,
  currency_code varchar(255) character set latin1 default NULL,
  amp_component_id bigint(20) default NULL
) ;


DROP TABLE IF EXISTS test_v_component_type;
CREATE TABLE test_v_component_type (
  amp_activity_id bigint(20) NOT NULL,
  component_type varchar(255) default NULL,
  component_type_id bigint(20) NOT NULL default '0'
) ;


DROP TABLE IF EXISTS test_v_components;
CREATE TABLE test_v_components (
  amp_activity_id bigint(20) NOT NULL default '0',
  title varchar(255) character set latin1 default NULL,
  amp_component_id bigint(20) NOT NULL default '0'
) ;


DROP TABLE IF EXISTS test_v_donor_funding;
CREATE TABLE test_v_donor_funding (
  amp_activity_id bigint(20) default NULL,
  amp_funding_id bigint(20) NOT NULL default '0',
  amp_fund_detail_id bigint(20) NOT NULL default '0',
  donor_name varchar(255) character set latin1 default NULL,
  transaction_type int(11) default NULL,
  adjustment_type int(11) default NULL,
  transaction_date datetime default NULL,
  transaction_amount double default NULL,
  currency_code varchar(255) character set latin1 default NULL,
  terms_assist_id bigint(20) NOT NULL default '0',
  terms_assist_name varchar(255) character set latin1 default NULL,
  fixed_exchange_rate double default NULL,
  org_grp_name varchar(255) character set latin1 default NULL,
  donor_type_name varchar(255) character set latin1 default NULL,
  financing_instrument_name varchar(255) character set latin1 default NULL,
  financing_instrument_id bigint(20) NOT NULL default '0',
  org_grp_id bigint(20) NOT NULL default '0',
  org_type_id bigint(20) NOT NULL default '0',
  disb_ord_rej bit(1) default NULL
) ;

DROP TABLE IF EXISTS test_v_donors;
CREATE TABLE test_v_donors (
  amp_activity_id bigint(20) default NULL,
  name varchar(255) character set latin1 default NULL,
  amp_donor_org_id bigint(20) default NULL,
  org_grp_id bigint(20) default NULL,
  org_type_id bigint(20) default NULL
) ;


DROP TABLE IF EXISTS test_v_nationalobjectives_level_0;
CREATE TABLE test_v_nationalobjectives_level_0 (
  amp_activity_id bigint(20) NOT NULL,
  name varchar(255) character set latin1 default NULL,
  amp_program_id bigint(20) NOT NULL,
  program_percentage float default NULL
) ;


DROP TABLE IF EXISTS test_v_regional_funding;
CREATE TABLE test_v_regional_funding (
  amp_activity_id bigint(20) default NULL,
  amp_regional_funding_id bigint(20) NOT NULL default '0',
  amp_fund_detail_id bigint(20) NOT NULL default '0',
  region_name varchar(255) default NULL,
  transaction_type int(11) default NULL,
  adjustment_type int(11) default NULL,
  transaction_date datetime default NULL,
  transaction_amount double default NULL,
  currency_code varchar(255) character set latin1 default NULL,
  region_id bigint(20) default NULL
) ;

DROP TABLE IF EXISTS test_v_regions;
CREATE TABLE test_v_regions (
  amp_activity_id bigint(20) NOT NULL default '0',
  region varchar(255) character set latin1 default NULL,
  region_id bigint(20) default NULL,
  location_percentage double default NULL,
  location_id bigint(20) default NULL,
  zone_id bigint(20) default NULL
) ;


DROP TABLE IF EXISTS test_v_sectors;
CREATE TABLE test_v_sectors (
  amp_activity_id bigint(20) default NULL,
  sectorname varchar(256) default NULL,
  amp_sector_id bigint(20) default NULL,
  sector_percentage double default NULL,
  amp_sector_scheme_id bigint(20) default NULL,
  sec_scheme_name varchar(255) character set latin1 default NULL
) ;


DROP TABLE IF EXISTS test_v_sub_sectors;
CREATE TABLE test_v_sub_sectors (
  amp_activity_id bigint(20) default NULL,
  sectorname varchar(256) default NULL,
  amp_sector_id bigint(20) default NULL,
  sector_percentage double default NULL,
  amp_sec_scheme_id bigint(20) default NULL,
  sec_scheme_name varchar(255) character set latin1 default NULL
) ;


DROP TABLE IF EXISTS test_v_titles;
CREATE TABLE test_v_titles (
  amp_activity_id bigint(20) NOT NULL default '0',
  name varchar(255) character set latin1 default NULL,
  title_id bigint(20) NOT NULL default '0',
  draft bit(1) default NULL,
  status varchar(255) character set latin1 default NULL
) ;


DROP TABLE IF EXISTS test_v_computed_dates;
CREATE TABLE test_v_computed_dates (
  amp_activity_id bigint(20) NOT NULL default '0',
  activity_close_date datetime default NULL,
  actual_start_date datetime default NULL,
  actual_approval_date datetime default NULL,
  activity_approval_date datetime default NULL,
  proposed_start_date datetime default NULL,
  actual_completion_date datetime default NULL,
  proposed_completion_date datetime default NULL
) ;

INSERT INTO test_v_component_funding (amp_activity_id, amp_component_funding_id,
 amp_fund_detail_id, component_name, transaction_type, adjustment_type, transaction_date,
 transaction_amount, currency_id, currency_code, amp_component_id) VALUES
  (1,1,1,'Technical Assistance',0,1,'2009-01-01',1000,21,'USD',90001),
  (1,1,1,'Technical Assistance',1,1,'2009-01-01',2000,21,'USD',90001),
  (1,1,1,'Technical Assistance',0,0,'2009-01-01',3000,21,'USD',90001),
  (1,1,1,'Technical Assistance',1,0,'2009-01-01',4000,21,'USD',90001),
  
  (1,1,1,'Technical Assistance',0,1,'2010-01-01',1000,21,'USD',90001),
  (1,1,1,'Technical Assistance',1,1,'2010-01-01',2000,21,'USD',90001),
  (1,1,1,'Technical Assistance',0,0,'2010-01-01',3000,21,'USD',90001),
  (1,1,1,'Technical Assistance',1,0,'2010-01-01',4000,21,'USD',90001),
  
  (1,2,2,'Infrastructure support',0,1,'2009-01-01',1000,21,'USD',90002),
  (1,2,2,'Infrastructure support',1,1,'2009-01-01',2000,21,'USD',90002),
  (1,2,2,'Infrastructure support',0,0,'2009-01-01',3000,21,'USD',90002),
  (1,2,2,'Infrastructure support',1,0,'2009-01-01',4000,21,'USD',90002),
  
  (1,2,2,'Infrastructure support',0,1,'2010-01-01',1000,21,'USD',90002),
  (1,2,2,'Infrastructure support',1,1,'2010-01-01',2000,21,'USD',90002),
  (1,2,2,'Infrastructure support',0,0,'2010-01-01',3000,21,'USD',90002),
  (1,2,2,'Infrastructure support',1,0,'2010-01-01',4000,21,'USD',90002),
  
  (2,3,3,'Technical Assistance',0,1,'2009-01-01',5000,21,'USD',90001),
  (2,3,3,'Technical Assistance',1,1,'2009-01-01',6000,21,'USD',90001),
  (2,3,3,'Technical Assistance',0,0,'2009-01-01',7000,21,'USD',90001),
  (2,3,3,'Technical Assistance',1,0,'2009-01-01',8000,21,'USD',90001),
  
  (2,3,3,'Technical Assistance',0,1,'2010-01-01',5000,21,'USD',90001),
  (2,3,3,'Technical Assistance',1,1,'2010-01-01',6000,21,'USD',90001),
  (2,3,3,'Technical Assistance',0,0,'2010-01-01',7000,21,'USD',90001),
  (2,3,3,'Technical Assistance',1,0,'2010-01-01',8000,21,'USD',90001),
  
  (2,3,3,'Infrastructure support',0,1,'2009-01-01',5000,21,'USD',90002),
  (2,3,3,'Infrastructure support',1,1,'2009-01-01',6000,21,'USD',90002),
  (2,3,3,'Infrastructure support',0,0,'2009-01-01',7000,21,'USD',90002),
  (2,3,3,'Infrastructure support',1,0,'2009-01-01',8000,21,'USD',90002),
  
  (2,3,3,'Infrastructure support',0,1,'2010-01-01',5000,21,'USD',90002),
  (2,3,3,'Infrastructure support',1,1,'2010-01-01',6000,21,'USD',90002),
  (2,3,3,'Infrastructure support',0,0,'2010-01-01',7000,21,'USD',90002),
  (2,3,3,'Infrastructure support',1,0,'2010-01-01',8000,21,'USD',90002);

INSERT INTO test_v_component_type (amp_activity_id, component_type, component_type_id)
 VALUES
  (1,'tipo 1',90001),
  (2,'tipo 2',90002);


INSERT INTO test_v_components (amp_activity_id, title, amp_component_id) VALUES
  (1,'Technical Assistance',90001),
  (1,'Infrastructure support',90002),
  (2,'Technical Assistance',90001),
  (2,'Infrastructure support',90002);

INSERT INTO test_v_donor_funding (amp_activity_id, amp_funding_id, amp_fund_detail_id,
 donor_name, transaction_type, adjustment_type, transaction_date, transaction_amount,
 currency_code, terms_assist_id, terms_assist_name, fixed_exchange_rate, org_grp_name,
 donor_type_name, financing_instrument_name, financing_instrument_id, org_grp_id,
 org_type_id, disb_ord_rej) VALUES
  (1,1,100,'World Bank',0,1,'2009-01-18',1000,'USD',80,'Grant',NULL,'World Bank Group',
  'Multilateral','...',86,119,3,NULL),
  (1,1,100,'World Bank',1,1,'2009-01-30',2000,'USD',81,'In-kind',NULL,'World Bank Group',
  'Multilateral','Budget Support',84,6646,1,NULL),
  (1,1,100,'World Bank',0,0,'2009-01-24',3000,'USD',81,'In-kind',NULL,'World Bank Group',
  'Multilateral','Budget Support',84,6646,1,NULL),
  (1,1,100,'World Bank',1,0,'2009-01-17',4000,'USD',81,'In-kind',NULL,'World Bank Group',
  'Multilateral','Budget Support',84,6646,1,NULL),
  (1,2,120,'ACDI',0,1,'2010-01-25',1000,'USD',81,'In-kind',NULL,'ACDI','Multilateral',
  'Budget Support',84,6646,1,NULL),
  (1,2,120,'ACDI',1,1,'2010-01-31',2000,'USD',80,'Grant',NULL,'ACDI','Multilateral','...',86,6646,1
  ,NULL),
  (1,2,120,'ACDI',0,0,'2010-01-30',3000,'USD',80,'Grant',NULL,'ACDI','Multilateral','...',86,6646,1
  ,NULL),
  (1,2,120,'ACDI',1,0,'2010-01-16',4000,'USD',80,'Grant',NULL,'ACDI','Multilateral','...',86,6646,1
  ,NULL),
  (2,3,130,'World Bank',0,1,'2009-01-18',5000,'USD',80,'Grant',NULL,'World Bank Group',
  'Multilateral','...',86,119,3,NULL),
  (2,3,130,'World Bank',1,1,'2009-01-30',6000,'USD',81,'In-kind',NULL,'World Bank Group',
  'Multilateral','Budget Support',84,6646,1,NULL),
  (2,3,130,'World Bank',0,0,'2009-01-24',7000,'USD',81,'In-kind',NULL,'World Bank Group',
  'Multilateral','Budget Support',84,6646,1,NULL),
  (2,3,130,'World Bank',1,0,'2009-01-17',8000,'USD',81,'In-kind',NULL,'World Bank Group',
  'Multilateral','Budget Support',84,6646,1,NULL),
  (2,4,140,'ACDI',0,1,'2010-01-25',5000,'USD',81,'In-kind',NULL,'ACDI','Multilateral',
  'Budget Support',84,6646,1,NULL),
  (2,4,140,'ACDI',1,1,'2010-01-31',6000,'USD',80,'Grant',NULL,'ACDI','Multilateral','...',86,6646,1
  ,NULL),
  (2,4,140,'ACDI',0,0,'2010-01-30',7000,'USD',80,'Grant',NULL,'ACDI','Multilateral','...',86,6646,1
  ,NULL),
  (2,4,140,'ACDI',1,0,'2010-01-16',8000,'USD',80,'Grant',NULL,'ACDI','Multilateral','...',86,6646,1
  ,NULL);

INSERT INTO test_v_donors (amp_activity_id, name, amp_donor_org_id, org_grp_id,
 org_type_id) VALUES
  (1,'World Bank',1,1,1),
  (1,'ACDI',2,1,1),
  (2,'World Bank',1,1,1),
  (2,'ACDI',2,1,1);
  
 INSERT INTO test_v_nationalobjectives_level_0 (amp_activity_id, name, amp_program_id,
 program_percentage) VALUES
  (1,'Maternity program',1,70),
  (1,'Health program',2,30),
  (2,'Maternity program',3,50),
  (2,'Health program',3,50);

  INSERT INTO test_v_regional_funding (amp_activity_id, amp_regional_funding_id,
 amp_fund_detail_id, region_name, transaction_type, adjustment_type, transaction_date,
 transaction_amount, currency_code, region_id) VALUES
  (1,1,1,'Dakar',0,1,'2009-01-01',1000,'USD',2),
  (1,1,1,'Dakar',1,1,'2009-01-01',2000,'USD',2),
  (1,1,1,'Dakar',0,0,'2009-01-01',3000,'USD',2),
  (1,1,1,'Dakar',1,0,'2009-01-01',4000,'USD',2),
  (1,1,1,'Dakar',0,1,'2010-01-01',1000,'USD',2),
  (1,1,1,'Dakar',1,1,'2010-01-01',2000,'USD',2),
  (1,1,1,'Dakar',0,0,'2010-01-01',3000,'USD',2),
  (1,1,1,'Dakar',1,0,'2010-01-01',4000,'USD',2),
  (1,1,1,'Nairobi',0,1,'2009-01-01',1000,'USD',1),
  (1,1,1,'Nairobi',1,1,'2009-01-01',2000,'USD',1),
  (1,1,1,'Nairobi',0,0,'2009-01-01',3000,'USD',1),
  (1,1,1,'Nairobi',1,0,'2009-01-01',4000,'USD',1),
  (1,1,1,'Nairobi',0,1,'2010-01-01',1000,'USD',1),
  (1,1,1,'Nairobi',1,1,'2010-01-01',2000,'USD',1),
  (1,1,1,'Nairobi',0,0,'2010-01-01',3000,'USD',1),
  (1,1,1,'Nairobi',1,0,'2010-01-01',4000,'USD',1),
  (2,1,1,'Dakar',0,1,'2009-01-01',5000,'USD',2),
  (2,1,1,'Dakar',1,1,'2009-01-01',6000,'USD',2),
  (2,1,1,'Dakar',0,0,'2009-01-01',7000,'USD',2),
  (2,1,1,'Dakar',1,0,'2009-01-01',8000,'USD',2),
  (2,1,1,'Dakar',0,1,'2010-01-01',5000,'USD',2),
  (2,1,1,'Dakar',1,1,'2010-01-01',6000,'USD',2),
  (2,1,1,'Dakar',0,0,'2010-01-01',7000,'USD',2),
  (2,1,1,'Dakar',1,0,'2010-01-01',8000,'USD',2),
  (2,1,1,'Nairobi',0,1,'2009-01-01',5000,'USD',1),
  (2,1,1,'Nairobi',1,1,'2009-01-01',6000,'USD',1),
  (2,1,1,'Nairobi',0,0,'2009-01-01',7000,'USD',1),
  (2,1,1,'Nairobi',1,0,'2009-01-01',8000,'USD',1),
  (2,1,1,'Nairobi',0,1,'2010-01-01',5000,'USD',1),
  (2,1,1,'Nairobi',1,1,'2010-01-01',6000,'USD',1),
  (2,1,1,'Nairobi',0,0,'2010-01-01',7000,'USD',1),
  (2,1,1,'Nairobi',1,0,'2010-01-01',8000,'USD',1);

INSERT INTO test_v_regions (amp_activity_id, region, region_id, location_percentage,
 location_id, zone_id) VALUES
  (1,'Dakar',2,70,1,1),
  (1,'Nairobi',1,30,1,1),
  (2,'Dakar',2,25,1,1),
  (2,'Nairobi',1,75,1,1);

INSERT INTO test_v_sectors (amp_activity_id, sectorname, amp_sector_id, sector_percentage
, amp_sector_scheme_id, sec_scheme_name) VALUES
  (1,'Agriculture',9000001,20,4,'Malawi Sectors'),
  (2,'Agriculture',9000001,30,4,'Malawi Sectors'),
  (1,'Education ',9000002,80,4,'Malawi Sectors'),
  (2,'Education ',9000002,70,4,'Malawi Sectors');

INSERT INTO test_v_sub_sectors (amp_activity_id, sectorname, amp_sector_id,
 sector_percentage, amp_sec_scheme_id, sec_scheme_name) VALUES
  (1,'Livestock',9000006,10,1,'1'),
  (2,'Livestock',9000006,10,1,'1'),
  (1,'Irrigation',9000005,10,1,'1'),
  (2,'Irrigation',9000005,20,1,'1'),
  (1,'Basic Education',9000003,50,1,'1'),
  (2,'Basic Education',9000003,35,1,'1'),
  (1,'Secondary Education',9000004,30,1,'1'),
  (2,'Secondary Education',9000004,35,1,'1');



INSERT INTO test_v_titles (amp_activity_id, name, title_id, draft, status) VALUES
  (1,'Test 1',1,False,'started'),
  (2,'Test 2',2,False,'started');

INSERT INTO test_v_computed_dates (amp_activity_id, activity_close_date, actual_start_date, actual_approval_date, activity_approval_date, proposed_start_date, actual_completion_date, proposed_completion_date) VALUES (1,'2009-12-01 12:45:30','2009-02-01 12:45:28','2009-06-01 12:45:34','2009-06-01 12:45:37','2009-04-01 12:45:40','2009-11-01 13:03:25','2009-08-01 13:03:25');