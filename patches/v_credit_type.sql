 CREATE OR REPLACE VIEW `v_type_of_credit` AS
  select
    `a`.`amp_activity_id` AS `amp_activity_id`,
    `cv`.`category_value` AS `name`,
    `cv`.`id` AS `id`
  from `amp_activity` `a`
  join `amp_category_value` `cv` on `a`.`credit_type_id` = `cv`.`id`
  order by `a`.`amp_activity_id`,`cv`.`category_value`;


insert into `amp_columns` (columnName, aliasName, cellType, extractorView)
values ('Credit Type', 'credit_type_id', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_type_of_credit');