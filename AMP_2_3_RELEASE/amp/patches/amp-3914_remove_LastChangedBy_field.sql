START TRANSACTION;

UPDATE `amp_columns` SET columnName = 'Activity Updated By' WHERE columnName = 'Last changed by';
DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name = 'Last changed by');
DELETE FROM `amp_fields_visibility` WHERE name = 'Last changed by';

COMMIT;