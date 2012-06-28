INSERT INTO `amp_columns`(columnName, aliasName, cellType, extractorView) VALUES ('Component description','description','org.dgfoundation.amp.ar.cell.TextCell','v_component_description'),
 ('Physical progress title','title','org.dgfoundation.amp.ar.cell.TextCell','v_physical_title'),
 ('Physical progress description','description','org.dgfoundation.amp.ar.cell.TextCell','v_physical_description');
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_component_description` AS select `a`.`amp_activity_id` AS `amp_activity_id`,`c`.`description` AS `description` from (`amp_components` `c` join `amp_activity_components` `a` on((`a`.`amp_component_id` = `c`.`amp_component_id`))) order by `a`.`amp_activity_id`;
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_physical_title` AS select `a`.`amp_activity_id` AS `amp_activity_id`,`a`.`description` AS `description` from `amp_physical_performance` `a` order by `a`.`amp_activity_id`;
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_physical_description` AS select `a`.`amp_activity_id` AS `amp_activity_id`,`a`.`description` AS `description` from `amp_physical_performance` `a` order by `a`.`amp_activity_id`;

