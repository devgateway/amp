CREATE OR REPLACE  VIEW `v_mode_of_payment` AS
  select
    `a`.`amp_activity_id` AS `amp_activity_id`,
    `val`.`category_value` AS `mode_of_payment_name`,
    `val`.`id` AS `mode_of_payment_code`
  from
    `amp_activity` `a`, `amp_funding` `fund`,`amp_category_value` `val`, amp_category_class c
  where
    (`fund`.`amp_activity_id` = `a`.`amp_activity_id`) and (`val`.`id` = `fund`.`mode_of_payment_category_va`) and 
    ( c.keyName="mode_of_payment" and c.id=val.amp_category_class_id )
  group by
    `a`.`amp_activity_id`,`val`.`id`
  order by
    `a`.`amp_activity_id`,`val`.`category_value`;
    
INSERT IGNORE INTO amp_columns(columnName, aliasName, cellType, extractorView) VALUES ("Mode of Payment", "modeOfPayment", "org.dgfoundation.amp.ar.cell.TrnTextCell", "v_mode_of_payment"); 
