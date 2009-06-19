DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Contracting Agency Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Contracting Agency Remove Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Executing Agency Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Executing Agency Add Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Executing Agency Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Executing Agency Remove Organizations Button';





