CREATE or replace VIEW `v_executing_agency` AS
  select
    `f`.`activity` AS `amp_activity_id`,
    `o`.`name` AS `name`,
    `f`.`organisation` AS `amp_org_id`,
     f.`percentage` as percentage
  from
    ((`amp_org_role` `f` join `amp_organisation` `o`) join `amp_role` `r` on(((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code` = _utf8'EA'))))
  order by
    `f`.`activity`,`o`.`name`;