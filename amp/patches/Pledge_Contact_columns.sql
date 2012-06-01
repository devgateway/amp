INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Point of Contact', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_contact');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Alternate Contact', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_alternate_contact'); 
 
CREATE or REPLACE VIEW v_pledges_contact as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactName` AS `contactName`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;
    
CREATE or REPLACE VIEW v_pledges_alternate_contact as 
select
    pf.`id` AS `pledge_id`,
    pf.`contactName_1` AS `contactName`,
    pf.`id` AS `contactName_id`
  from
    `amp_funding_pledges` pf
  order by
    pf.`id`;