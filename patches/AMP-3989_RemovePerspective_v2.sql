start transaction;

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Perspective Disbursement');
delete from amp_fields_visibility where name = 'Perspective Disbursement';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Perspective Commitment');
delete from amp_fields_visibility where name = 'Perspective Commitment';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Perspective Expenditure');
delete from amp_fields_visibility where name = 'Perspective Expenditure';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Regional Funding Perspective Commitments');
delete from amp_fields_visibility where name = 'Regional Funding Perspective Commitments';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Regional Funding Perspective Disbursements');
delete from amp_fields_visibility where name = 'Regional Funding Perspective Disbursements';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Regional Funding Perspective Expenditures');
delete from amp_fields_visibility where name = 'Regional Funding Perspective Expenditures';

commit;