Update `amp_measures` set aliasName = 'Pledge Planed Disbursements'
where measureName = 'Planed Disbursements' and type = 'P';

Update `amp_measures` set measureName = 'Planned Disbursements'
where measureName = 'Planed Disbursements' and type = 'P';

Update `amp_measures` set measureName= 'Planned Commitments'
where measureName = 'Planed Commitments' and type = 'P';

insert into amp_measures
	(measureName, aliasName, type, expression, description) 
values 
	('Planned Commitments','Pledges Planned Commitments','P',null, null),
	('Planned Disbursements','Pledges Planned Disbursements','P',null, null);


delete from amp_features_templates where feature = (select id from amp_features_visibility where name = 'Planed Disbursements');
delete from amp_features_visibility where name = 'Planed Disbursements';

delete from amp_features_templates where feature = (select id from amp_features_visibility where name = 'Planed Commitments');
delete from amp_features_visibility where name = 'Planed Commitments';

delete from amp_features_templates where feature = (select id from amp_features_visibility where name = 'Pledge Planed Commitments');
delete from amp_features_visibility where name = 'Pledge Planed Commitments';

delete from amp_features_templates where feature = (select id from amp_features_visibility where name = 'Pledge Planed Disbursements');
delete from amp_features_visibility where name = 'Pledge Planed Disbursements';