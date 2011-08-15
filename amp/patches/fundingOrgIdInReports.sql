/*create crecord in amp_columns*/
insert into amp_columns (columnName, cellType, extractorView) 
(select 'Funding Organization Id','org.dgfoundation.amp.ar.cell.TextCell','v_funding_org_id' 
from dual where (select count(columnId) from amp_columns where extractorView="v_funding_org_id")=0);

/*create corresponding view*/
CREATE  or replace VIEW `v_funding_org_id` AS select `f`.`amp_activity_id` AS `amp_activity_id`, 
`f`.`financing_id` AS `funding_org_id` from (`amp_funding` `f` join `amp_activity` `a`) where 
(`f`.`amp_activity_id` = `a`.`amp_activity_id` and `f`.`financing_id` is not NULL and `f`.`financing_id`!='') 
order by `f`.`amp_activity_id` ;

