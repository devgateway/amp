CREATE or replace VIEW  `v_sector_group` 
AS select 
	`f`.`activity` AS `amp_activity_id`,
	`o`.`name` AS `name`,
	`f`.`organisation` AS `amp_org_id` 
    from (`amp_org_role` `f` join `amp_organisation` `o` join `amp_role` `r`
		on((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code`="SG"))) 
	order by `f`.`activity`,`o`.`name`;
	
CREATE  or replace  VIEW  `v_regional_group` 
AS select 
	`f`.`activity` AS `amp_activity_id`,
	`o`.`name` AS `name`,
	`f`.`organisation` AS `amp_org_id` 
    from (`amp_org_role` `f` join `amp_organisation` `o` join `amp_role` `r`
		on((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code`="RG"))) 
	order by `f`.`activity`,`o`.`name`;
	
insert into amp_columns_order values (17,"Sector Group",17);
insert into amp_columns_order values (18,"Regional Group",18);
insert into amp_columns(columnName, aliasName,cellType, extractorView) values("Sector Group", "sector_group","org.dgfoundation.amp.ar.cell.TextCell", "v_sector_group");
insert into amp_columns(columnName, aliasName,cellType, extractorView) values("Regional Group", "regional_group","org.dgfoundation.amp.ar.cell.TextCell", "v_sector_group");
