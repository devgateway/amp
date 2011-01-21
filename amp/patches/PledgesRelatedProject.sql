CREATE OR REPLACE VIEW `v_pledges_projects` as  select fp.`id` as pledge_id,ac.`name` as title,ac.`amp_activity_id` as amp_activity_id from `amp_funding_pledges` fp join `amp_funding_detail` fd on fd.`pledge_id` = fp.`id` join `amp_funding` fu on fd.`amp_funding_id` = fu.`amp_funding_id`  join `amp_activity` ac on fu.`amp_activity_id` = ac.`amp_activity_id` group by ac.`amp_activity_id`;
 
INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Related Projects', max(`indexOrder`) + 1 from `amp_columns_order`;

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Related Projects', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_projects');