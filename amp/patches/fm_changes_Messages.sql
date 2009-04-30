DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name='Inbox');
DELETE FROM `amp_fields_visibility` WHERE name='Inbox';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name='Inbox');
DELETE FROM `amp_fields_visibility` WHERE name='Inbox';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name='Sent');
DELETE FROM `amp_fields_visibility` WHERE name='Sent';




