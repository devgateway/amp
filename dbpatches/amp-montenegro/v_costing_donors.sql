CREATE OR REPLACE VIEW `v_costing_donors` AS 
select `eu`.`amp_activity_id` AS `amp_activity_id`,`o`.`name`
 AS `name`,`euc`.`donor_id` AS `donor_id` 
 FROM amp_activity a, amp_eu_activity eu, amp_eu_activity_contributions euc, amp_organisation o
 where a.amp_activity_id=eu.amp_activity_id and eu.id=euc.eu_activity_id and
 euc.donor_id=o.amp_org_id order by o.name;
