
DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Responsible Organisation Groups');
DELETE FROM `amp_fields_visibility` WHERE name = 'Responsible Organisation Groups';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Beneficiary Agency Add Organizations Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Beneficiary Agency Add Organizations Button';

