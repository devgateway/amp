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







