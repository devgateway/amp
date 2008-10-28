DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Activity Budget');
DELETE FROM `amp_fields_visibility` WHERE name = 'Activity Budget';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Date Team Leader');
DELETE FROM `amp_fields_visibility` WHERE name = 'Date Team Leader';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Team');
DELETE FROM `amp_fields_visibility` WHERE name = 'Team';

UPDATE `amp_columns` SET columnName='Organizations and Project ID' WHERE columnName='Project Id';
DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Project Id');
DELETE FROM `amp_fields_visibility` WHERE name = 'Project Id';