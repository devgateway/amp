INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledges Zones', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_zones');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledges Districts', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_districts');

CREATE OR REPLACE VIEW v_pledges_regions AS 
select 
    `ra`.`pledge_id` AS `pledge_id`,
    `getLocationName`(`getLocationIdByImplLoc`(`cvl`.`id`,'Region')) AS `location_name`,
    `getLocationIdByImplLoc`(`cvl`.`id`,'Region') AS `location_id`,
    sum(`ra`.`location_percentage`) AS `location_percentage` 
  from 
     ((`amp_funding_pledges_location` `ra` join `amp_category_value_location` `cvl` on((`ra`.`location_id` = `cvl`.`id`)))) 
  where 
    ((`ra`.`location_id` = `cvl`.`id`) and (`getLocationIdByImplLoc`(`cvl`.`id`,'Region') is not null)) 
  group by 
    `ra`.`pledge_id`,`getLocationIdByImplLoc`(`cvl`.`id`,'Region');
    
CREATE OR REPLACE VIEW v_pledges_zones AS 
select 
    `ra`.`pledge_id` AS `pledge_id`,
    `getLocationName`(`getLocationIdByImplLoc`(`cvl`.`id`,'Zone')) AS `location_name`,
    `getLocationIdByImplLoc`(`cvl`.`id`,'Zone') AS `location_id`,
    sum(`ra`.`location_percentage`) AS `location_percentage` 
  from 
     ((`amp_funding_pledges_location` `ra` join `amp_category_value_location` `cvl` on((`ra`.`location_id` = `cvl`.`id`)))) 
  where 
    ((`ra`.`location_id` = `cvl`.`id`) and (`getLocationIdByImplLoc`(`cvl`.`id`,'Zone') is not null)) 
  group by 
    `ra`.`pledge_id`,`getLocationIdByImplLoc`(`cvl`.`id`,'Zone');
    
CREATE OR REPLACE VIEW v_pledges_districts AS 
select 
    `ra`.`pledge_id` AS `pledge_id`,
    `getLocationName`(`getLocationIdByImplLoc`(`cvl`.`id`,'District')) AS `location_name`,
    `getLocationIdByImplLoc`(`cvl`.`id`,'District') AS `location_id`,
    sum(`ra`.`location_percentage`) AS `location_percentage` 
  from 
     ((`amp_funding_pledges_location` `ra` join `amp_category_value_location` `cvl` on((`ra`.`location_id` = `cvl`.`id`)))) 
  where 
    ((`ra`.`location_id` = `cvl`.`id`) and (`getLocationIdByImplLoc`(`cvl`.`id`,'District') is not null)) 
  group by 
    `ra`.`pledge_id`,`getLocationIdByImplLoc`(`cvl`.`id`,'District');
    
    