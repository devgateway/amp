START TRANSACTION;

DELETE FROM `amp_features_templates` WHERE feature IN (SELECT id FROM `amp_features_visibility` WHERE name = 'My Tasks');
DELETE FROM `amp_features_visibility` WHERE name = 'My Tasks';

DELETE FROM `amp_features_templates` WHERE feature IN (SELECT id FROM `amp_features_visibility` WHERE name = 'My Messages');
DELETE FROM `amp_features_visibility` WHERE name = 'My Messages';

DELETE FROM `amp_modules_templates` WHERE module IN (SELECT id FROM `amp_modules_visibility` WHERE name = 'Desktop Sections');
DELETE FROM `amp_modules_visibility` WHERE name = 'Desktop Sections';

COMMIT;