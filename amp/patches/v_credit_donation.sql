 CREATE OR REPLACE VIEW `v_credit_donation` AS
 select
    `a`.`amp_activity_id` AS `amp_activity_id`,
    'Credito' AS `name`,
    0 AS `id`
  from `amp_activity` `a`
  join `amp_category_value` `cv` on `a`.`credit_type_id` = `cv`.`id` and (`cv`.`category_value` = 'Comercial' or `cv`.`category_value` = 'Concesional')
union
  select
    `a`.`amp_activity_id` AS `amp_activity_id`,
    'Donacion' AS `name`,
    1 AS `id`
  from `amp_activity` `a`
  join `amp_category_value` `cv` on `a`.`credit_type_id` = `cv`.`id` and `cv`.`category_value` = 'Donacion';


insert into `amp_columns` (columnName, cellType, extractorView)
values ('Credit/Donation', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_credit_donation');

