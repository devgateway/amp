UPDATE `amp_columns` SET ColumnName = 'Activity Created By' WHERE ColumnName = 'Activity Creator';
DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Activity Creator');
DELETE FROM `amp_fields_visibility` WHERE name = 'Activity Creator';