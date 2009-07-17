DELETE FROM `amp_features_templates` WHERE feature IN (SELECT id FROM `amp_features_visibility` WHERE name ='Commitments');
DELETE FROM `amp_features_visibility` WHERE name='Commitments';


