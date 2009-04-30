UPDATE `amp_columns` SET ColumnName = 'Current Completion Date' WHERE ColumnName = 'Actual Completion Date';
DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Actual Completion Date');
DELETE FROM `amp_fields_visibility` WHERE name = 'Actual Completion Date';


