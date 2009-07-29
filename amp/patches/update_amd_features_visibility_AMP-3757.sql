START TRANSACTION;

UPDATE amp_features_visibility SET name = 'Project Coordinator Contact Information' WHERE name = 'Project Coordinator';

DELETE FROM amp_fields_templates WHERE field IN (SELECT id FROM amp_fields_visibility WHERE parent IN (SELECT id FROM amp_features_visibility WHERE name = 'Mofed Contact Information'));
                    
DELETE FROM amp_fields_visibility WHERE parent IN (SELECT id FROM amp_features_visibility WHERE name = 'Mofed Contact Information');

DELETE FROM amp_features_templates WHERE feature IN (SELECT id FROM amp_features_visibility WHERE name = 'Mofed Contact Information');
                    
DELETE FROM amp_features_visibility WHERE name = 'Mofed Contact Information';

DELETE FROM amp_fields_templates WHERE field IN (SELECT id FROM amp_fields_visibility WHERE name = 'Sector Ministry Fax Number');

DELETE FROM amp_fields_visibility WHERE name = 'Sector Ministry Fax Number';

UPDATE amp_features_visibility SET name = 'Sector Ministry Contact Information' WHERE name = 'Sector Ministry Contact';

COMMIT;