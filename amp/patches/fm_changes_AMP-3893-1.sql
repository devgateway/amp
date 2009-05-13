DELETE FROM `amp_features_templates` WHERE feature IN (SELECT id FROM `amp_features_visibility` WHERE name ='Donnor Agency');
DELETE FROM `amp_features_visibility` WHERE name='Donnor Agency';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation';

UPDATE `amp_columns` SET columnName='Responsible Organization Groups' WHERE columnName='Responsible Organisation Groups';
UPDATE `amp_columns` SET columnName='Responsible Organization' WHERE columnName='Responsible Organisation';

