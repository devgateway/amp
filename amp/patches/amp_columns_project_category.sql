CREATE OR REPLACE VIEW v_project_category AS
 SELECT amp_activity_id, category_value AS name,
	amp_categoryvalue_id AS amp_category_id FROM amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac
	WHERE  acv.id=aac.amp_categoryvalue_id AND acc.id=acv.amp_category_class_id AND acc.keyName='project_category';

insert into amp_columns
   	(columnName,cellType,extractorView)
       values ('Project Category', 'org.dgfoundation.amp.ar.cell.TextCell','v_project_category');
