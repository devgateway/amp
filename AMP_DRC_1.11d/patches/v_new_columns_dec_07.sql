CREATE or replace VIEW  `v_implementing_agency` 
AS select 
	`f`.`activity` AS `amp_activity_id`,
	`o`.`name` AS `name`,
	`f`.`organisation` AS `amp_org_id` 
    from (`amp_org_role` `f` join `amp_organisation` `o` join `amp_role` `r`
		on((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code`="IA"))) 
	order by `f`.`activity`,`o`.`name`;
	
CREATE  or replace  VIEW  `v_contracting_agency` 
AS select 
	`f`.`activity` AS `amp_activity_id`,
	`o`.`name` AS `name`,
	`f`.`organisation` AS `amp_org_id` 
    from (`amp_org_role` `f` join `amp_organisation` `o` join `amp_role` `r`
		on((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code`="CA"))) 
	order by `f`.`activity`,`o`.`name`;
	
CREATE  or replace  VIEW  `v_beneficiary_agency` 
AS select 
	`f`.`activity` AS `amp_activity_id`,
	`o`.`name` AS `name`,
	`f`.`organisation` AS `amp_org_id` 
    from (`amp_org_role` `f` join `amp_organisation` `o` join `amp_role` `r`
		on((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code`="BA"))) 
	order by `f`.`activity`,`o`.`name`;
	
insert into amp_columns_order values (14,"Implementing Agency",14);
insert into amp_columns_order values (15,"Contracting Agency",15);
insert into amp_columns_order values (16,"Beneficiary Agency",16);
insert into amp_columns(columnName, aliasName,cellType, extractorView) values("Implementing Agency", "implementing_agency","org.dgfoundation.amp.ar.cell.TextCell", "v_implementing_agency");
insert into amp_columns(columnName, aliasName,cellType, extractorView) values("Contracting Agency", "contracting_agency","org.dgfoundation.amp.ar.cell.TextCell", "v_contracting_agency");
insert into amp_columns(columnName, aliasName,cellType, extractorView) values("Beneficiary Agency", "beneficiary_agency","org.dgfoundation.amp.ar.cell.TextCell", "v_beneficiary_agency");