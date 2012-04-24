DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Executing Agency Percentage');
DELETE FROM `amp_fields_visibility` WHERE name = 'Executing Agency Percentage';

DELETE FROM `amp_features_templates` WHERE feature IN (SELECT id FROM `amp_features_visibility` WHERE name ='Donnor Agency');
DELETE FROM `amp_features_visibility` WHERE name='Donnor Agency';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation';

UPDATE `amp_columns` SET columnName='Responsible Organization Groups' WHERE columnName='Responsible Organisation Groups';
UPDATE `amp_columns` SET columnName='Responsible Organization' WHERE columnName='Responsible Organisation';

DELETE FROM `amp_features_templates` WHERE feature IN (SELECT id FROM `amp_features_visibility` WHERE name ='Responsible Organisation');
DELETE FROM `amp_features_visibility` WHERE name='Responsible Organisation';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation Add Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation Remove Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation Groups');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation Groups';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Beneficiary Agency Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Beneficiary Agency Add Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Beneficiary Agency Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Beneficiary Agency Remove Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Contracting Agency Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Contracting Agency Add Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Contracting Agency Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Contracting Agency Remove Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Executing Agency Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Executing Agency Add Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Executing Agency Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Executing Agency Remove Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Implementing Agency Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Implementing Agency Add Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Implementing Agency Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Implementing Agency Remove Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Regional Group Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Regional Group Add Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Regional Group Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Regional Group Remove Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Sector Group Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Sector Group Add Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Sector Group Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Sector Group Remove Organizations Button';
