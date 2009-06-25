CREATE OR REPLACE VIEW `v_credit_donation` AS
    (select `f`.`amp_activity_id` AS `amp_activity_id`,
	          (case when `val`.`category_value` = convert('Donaci�n' using utf8) then convert('Donaci�n' using utf8) else convert('Credito' using utf8) end) AS `name`,
	          (case when `val`.`category_value` = convert('Donaci�n' using utf8) then 0 else 1 end) AS `id`
     from (`amp_funding` `f`
     join `amp_category_value` `val`)
     where (`f`.`financing_instr_category_value` = `val`.`id`))
     union
     (select `eu`.`amp_activity_id` AS `amp_activity_id`,
	          (case when `val`.`category_value` = convert('Donaci�n' using utf8) then convert('Donaci�n' using utf8) else convert('Credito' using utf8) end) AS `name`,
	          (case when `val`.`category_value` = convert('Donaci�n' using utf8) then 0 else 1 end) AS `id`
     from ((`amp_eu_activity` `eu`
     join `amp_eu_activity_contributions` `eu_con`)
     join `amp_category_value` `val`)
     where ((`eu_con`.`eu_activity_id` = `eu`.`id`) and
     (`eu_con`.`FINANCING_INSTR_CATEGORY_VALUE` = `val`.`id`)))
     order by `amp_activity_id`,`name`
  ;

