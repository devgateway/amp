CREATE or replace VIEW `v_budget_sector` AS select `a`.`amp_activity_id` AS `amp_activity_id`,concat(`bs`.`code`,' - ',`bs`.`sector_name`) AS `budget_sector` from (`amp_activity` `a` join `amp_budget_sector` `bs` on((`a`.`budget_sector` = `bs`.`budged_sector_id`))) order by `a`.`amp_activity_id`;
insert into amp_columns(columnName, cellType, extractorview) values("Budget Sector", "org.dgfoundation.amp.ar.cell.TextCell","v_budget_sector");

CREATE or replace VIEW v_budget_organization AS select  `a`.`amp_activity_id` AS `amp_activity_id`, concat(`org`.`name`, ' - ', `org`.`budget_org_code`) AS `budget_sector` from  (`amp_activity` `a` join `amp_organisation` `org` on((`a`.`budget_organization` = `org`.`amp_org_id`))) order by  `a`.`amp_activity_id`;
insert into amp_columns(columnName, cellType, extractorview) values("Budget Organization", "org.dgfoundation.amp.ar.cell.TextCell","v_budget_organization");

CREATE or replace VIEW `v_budget_department` AS select `a`.`amp_activity_id` AS `amp_activity_id`,concat(`dep`.`code`,' - ',`dep`.`name`) AS `budget_sector` from (`amp_activity` `a` join `amp_departments` `dep` on((`a`.`budget_department` = `dep`.`id_department`))) order by `a`.`amp_activity_id`;
insert into amp_columns(columnName, cellType, extractorview) values("Budget Department", "org.dgfoundation.amp.ar.cell.TextCell","v_budget_department");

CREATE or replace VIEW `v_budget_program` AS select  `a`.`amp_activity_id` AS `amp_activity_id`,concat(`prog`.`theme_code`,' - ',`prog`.`name`) AS `budget_sector` from (`amp_activity` `a` join `amp_theme` `prog` on((`a`.`budget_program` = `prog`.`amp_theme_id`))) order by `a`.`amp_activity_id`;
insert into amp_columns(columnName, cellType, extractorview) values("Budget Program", "org.dgfoundation.amp.ar.cell.TextCell","v_budget_program");
