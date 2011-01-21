DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Planning Ministry Rank');
DELETE FROM `amp_fields_visibility` WHERE name = 'Planning Ministry Rank';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Proposed Completion Dates');
DELETE FROM `amp_fields_visibility` WHERE name = 'Proposed Completion Dates';