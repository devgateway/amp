CREATE OR REPLACE  VIEW `v_terms_assist` AS
  select
    `a`.`amp_activity_id` AS `amp_activity_id`,
    `val`.`category_value` AS `terms_assist_name`,
    `val`.`id` AS `terms_assist_code`
  from
    ((`amp_activity` `a` join `amp_funding` `fund`) join `amp_category_value` `val`)
  where
    ((`fund`.`amp_activity_id` = `a`.`amp_activity_id`) and (`val`.`id` = `fund`.`type_of_assistance_category_va`))
  group by
    `a`.`amp_activity_id`,`val`.`id`
  order by
    `a`.`amp_activity_id`,`val`.`category_value`;