delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Financial Progress Tab');
delete from amp_fields_visibility where name = 'Financial Progress Tab';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Contract of Disbursement Order' and parent in (select id from amp_features_visibility where name = 'Disbursement'));
delete from amp_fields_visibility where name = 'Contract of Disbursement Order' and parent in (select id from amp_features_visibility where name = 'Disbursement');

delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Disbursements');
delete from amp_features_visibility where name = 'Disbursements';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Add Commitment Button');
delete from amp_fields_visibility where name = 'Add Commitment Button';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Add Disbursement Button');
delete from amp_fields_visibility where name = 'Add Disbursement Button';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Adjustment Type Commitment');
delete from amp_fields_visibility where name = 'Adjustment Type Commitment';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Adjustment Type Expenditure');
delete from amp_fields_visibility where name = 'Adjustment Type Expenditure';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Amount Commitment');
delete from amp_fields_visibility where name = 'Amount Commitment';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Amount Expenditure');
delete from amp_fields_visibility where name = 'Amount Expenditure';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Cumulative Commitment');
delete from amp_fields_visibility where name = 'Cumulative Commitment';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Cumulative Disbursement');
delete from amp_fields_visibility where name = 'Cumulative Disbursement';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Currency Commitment');
delete from amp_fields_visibility where name = 'Currency Commitment';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Currency Expenditure');
delete from amp_fields_visibility where name = 'Currency Expenditure';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Date Commitment');
delete from amp_fields_visibility where name = 'Date Commitment';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Date Expenditure');
delete from amp_fields_visibility where name = 'Date Expenditure';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Total Committed');
delete from amp_fields_visibility where name = 'Total Committed';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Total Disbursed');
delete from amp_fields_visibility where name = 'Total Disbursed';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Total Expended');
delete from amp_fields_visibility where name = 'Total Expended';