UPDATE `amp_columns` SET ColumnName = 'Activity Created On' WHERE ColumnName = 'Creation Date';
DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Creation Date');
DELETE FROM `amp_fields_visibility` WHERE name = 'Creation Date';


