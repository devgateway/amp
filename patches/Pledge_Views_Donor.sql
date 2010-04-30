
  CREATE OR REPLACE VIEW v_pledges_donor as
  select
    `f`.`id` AS `pledge_id`,
    `o`.`name` AS `name`,
    `f`.`amp_org_id` AS `amp_donor_org_id`,
    `o`.`org_grp_id` AS `org_grp_id`,
    `o`.`org_type_id` AS `org_type_id`
  from
    (`amp_funding_pledges` `f` join `amp_organisation` `o`)
  where
    (`f`.`amp_org_id` = `o`.`amp_org_id`)
  order by
    `f`.`id`,`o`.`name`;
  
