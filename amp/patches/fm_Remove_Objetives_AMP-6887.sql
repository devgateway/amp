DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Objectives');
DELETE FROM `amp_fields_visibility` WHERE name = 'Objectives';

