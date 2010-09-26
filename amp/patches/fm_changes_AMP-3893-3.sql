DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation Add Organizations Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation Remove Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation Remove Organizations Button';
