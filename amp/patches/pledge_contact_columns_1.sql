DELETE FROM `amp_columns` where columnName = 'Point of Contact';
DELETE FROM `amp_columns` where columnName = 'Alternate Contact';

DROP VIEW IF EXISTS v_pledges_contact;
DROP VIEW IF EXISTS v_pledges_alternate_contact;

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 1 - Name', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact1_name');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 1 - Address', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact1_address');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 1 - Email', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact1_Email');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 1 - Ministry', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact1_Ministry');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES(' Pledge Contact 1 - Title', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact1_Title');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 1 - Telephone', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact1_Telephone');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES(' Pledge Contact 1 - Alternate Contact', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact1_Alternate');


INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 2 - Name', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact2_name');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 2 - Address', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact2_address');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 2 - Email', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact2_Email');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 2 - Ministry', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact2_Ministry');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES(' Pledge Contact 2 - Title', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact2_Title');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledge Contact 2 - Telephone', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact2_Telephone');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES(' Pledge Contact 2 - Alternate Contact', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact2_Alternate');



INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Pledge Contact 1', max(`indexOrder`) + 1 from `amp_columns_order`;

INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Pledge Contact 2', max(`indexOrder`) + 1 from `amp_columns_order`;


CREATE or REPLACE VIEW v_pledges_contact1_address as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactAddress_1` AS `address`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;
    
CREATE or REPLACE VIEW v_pledges_contact1_Email as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactAlternativeEmail` AS `email`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;
    
CREATE or REPLACE VIEW v_pledges_contact1_Ministry as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactMinistry` AS `ministry`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;
    
    
CREATE or REPLACE VIEW v_pledges_contact1_Title as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactTitle` AS `Title`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;
    

CREATE or REPLACE VIEW v_pledges_contact1_Telephone as 
select
    pf.`id` as  `pledge_id`,
    pf.`contactTelephone_1` AS `Telephone`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;
    
CREATE or REPLACE VIEW v_pledges_contact1_name as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactName` AS `name`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;


CREATE or REPLACE VIEW v_pledges_contact1_Alternate as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactAlternativeName` AS `altername`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;


CREATE or REPLACE VIEW v_pledges_contact2_address as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactAddress_1` AS `address`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;

CREATE or REPLACE VIEW v_pledges_contact2_Email as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactAlternativeEmail_1` AS `email`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;

CREATE or REPLACE VIEW v_pledges_contact2_Ministry as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactMinistry_1` AS `ministry`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;


CREATE or REPLACE VIEW v_pledges_contact2_Title as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactTitle_1` AS `Title`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;


CREATE or REPLACE VIEW v_pledges_contact2_Telephone as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactTelephone_1` AS `Telephone`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;

CREATE or REPLACE VIEW v_pledges_contact2_name as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactName_1` AS `name`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;
    
  CREATE or REPLACE VIEW v_pledges_contact2_Alternate as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactAlternativeName_1` AS `altername`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;