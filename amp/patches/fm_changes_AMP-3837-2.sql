DELETE FROM `amp_report_column` WHERE columnId IN (SELECT columnId FROM `amp_columns` WHERE columnName ='Changed Date');
DELETE FROM `amp_columns` WHERE columnName ='Changed Date';
DROP VIEW IF EXISTS v_changed_date;
DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Changed Date');
DELETE FROM `amp_fields_visibility` WHERE name = 'Changed Date';


