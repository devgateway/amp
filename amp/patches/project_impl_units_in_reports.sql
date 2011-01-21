/*create crecord in amp_columns*/
insert into amp_columns (columnName, cellType, extractorView) 
(select 'Project Implementing Unit','org.dgfoundation.amp.ar.cell.TextCell','v_project_impl_unit' 
from dual where (select count(columnId) from amp_columns where extractorView="v_project_impl_unit")=0);

/*create corresponding view*/
CREATE OR REPLACE VIEW `v_project_impl_unit` AS 
select acc.`amp_activity_id` AS amp_activity_id,
acv.category_value AS `name`,
acv.id AS `proj_impl_unit_id` from amp_activities_categoryvalues acc, amp_category_value acv,
amp_category_class ac where acv.id=amp_categoryvalue_id and ac.id=acv.amp_category_class_id and 
ac.keyName='project_impl_unit';