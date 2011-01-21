DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Title');
DELETE FROM `amp_fields_visibility` WHERE name = 'Title';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Code');
DELETE FROM `amp_fields_visibility` WHERE name = 'Code';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Type');
DELETE FROM `amp_fields_visibility` WHERE name = 'Type';


DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Description');
DELETE FROM `amp_fields_visibility` WHERE name = 'Description';


DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Save Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Save Button';


DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Cancel Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Cancel Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Name');
DELETE FROM `amp_fields_visibility` WHERE name = 'Name';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Code');
DELETE FROM `amp_fields_visibility` WHERE name = 'Code';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Enable checkbox');
DELETE FROM `amp_fields_visibility` WHERE name = 'Enable checkbox';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Save Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Save Button';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Close Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Close Button';
