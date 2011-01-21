DELETE FROM amp_fields_templates WHERE field = (SELECT id FROM amp_fields_visibility WHERE name='Components Total Amount Commitments');
DELETE FROM amp_fields_visibility WHERE name='Components Total Amount Commitments';
DELETE FROM amp_fields_templates WHERE field = (SELECT id FROM amp_fields_visibility WHERE name='Components Total Amount Disbursements');
DELETE FROM amp_fields_visibility WHERE name='Components Total Amount Disbursements';
DELETE FROM amp_fields_templates WHERE field = (SELECT id FROM amp_fields_visibility WHERE name='Components Total Amount Expenditures');
DELETE FROM amp_fields_visibility WHERE name='Components Total Amount Expenditures';
