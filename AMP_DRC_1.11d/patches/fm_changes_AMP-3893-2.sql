DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation';

DELETE FROM `amp_features_templates` WHERE feature IN (SELECT id FROM `amp_features_visibility` WHERE name ='Responsible Organisation');
DELETE FROM `amp_features_visibility` WHERE name='Responsible Organisation';



