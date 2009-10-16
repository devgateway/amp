CREATE OR REPLACE  VIEW `v_funding_status` AS
  select
    `a`.`amp_activity_id` AS `amp_activity_id`,
    `val`.`category_value` AS `funding_status_name`,
    `val`.`id` AS `funding_status_code`
  from
    `amp_activity` `a`, `amp_funding` `fund`,`amp_category_value` `val`, amp_category_class c
  where
    (`fund`.`amp_activity_id` = `a`.`amp_activity_id`) and (`val`.`id` = `fund`.`funding_status_category_va`) and 
    ( c.keyName="funding_status" and c.id=val.amp_category_class_id )
  group by
    `a`.`amp_activity_id`,`val`.`id`
  order by
    `a`.`amp_activity_id`,`val`.`category_value`;
    
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView) 
	SELECT "Funding Status", "fundingStatus", "org.dgfoundation.amp.ar.cell.TrnTextCell", "v_funding_status" 
		FROM DUAL WHERE 
		(SELECT count(columnId) FROM amp_columns WHERE extractorView="v_funding_status") = 0;