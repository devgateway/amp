START TRANSACTION;

UPDATE amp_fields_visibility SET name = 'Document Notes' WHERE name = 'Document Comment';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name = 'Document Language');
DELETE FROM `amp_fields_visibility` WHERE name = 'Document Language';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name = 'Remove Web Resource Button');
DELETE FROM `amp_fields_visibility` WHERE name = 'Remove Web Resource Button';

COMMIT;