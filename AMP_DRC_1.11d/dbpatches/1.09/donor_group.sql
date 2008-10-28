DROP VIEW IF EXISTS `v_donor_groups`;
CREATE OR REPLACE VIEW `v_donor_groups` AS 
	select `a`.`amp_activity_id` AS `amp_activity_id`,`b`.`org_grp_name` AS `name`,`b`.`amp_org_grp_id` AS `amp_org_grp_id` 
	from ((`amp_funding` `a` join `amp_organisation` `c`) join `amp_org_group` `b`) 
	where 
	((`a`.`amp_donor_org_id` = `c`.`amp_org_id`) and (`c`.`org_grp_id` = `b`.`amp_org_grp_id`));
	

	
	