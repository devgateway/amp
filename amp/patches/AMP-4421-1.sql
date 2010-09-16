DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Cumulative Disbursement');
DELETE FROM `amp_fields_visibility` WHERE name = 'Cumulative Disbursement';

DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Cumulative Commitment');
DELETE FROM `amp_fields_visibility` WHERE name = 'Cumulative Commitment';

