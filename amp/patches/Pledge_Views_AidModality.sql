 CREATE OR REPLACE VIEW v_pledges_aid_modality as
 select
    `f`.`pledge_id` AS `pledge_id`,
    `val`.`category_value` AS `name`,
    `f`.`aid_modality` AS `amp_modality_id`
  from
    `amp_funding_pledges_details` `f`
    join `amp_category_value` `val` on f.`aid_modality` = val.`id`;
