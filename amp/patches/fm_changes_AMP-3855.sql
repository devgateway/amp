delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'Document Management');
delete from amp_features_templates where feature in (select id from amp_features_visibility where parent in (select id from amp_modules_visibility where name = 'Document Management'));
delete from amp_features_visibility where parent in ( select id from amp_modules_visibility where name = 'Document Management');
delete from amp_modules_visibility where name = 'Document Management';
delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'Documents Management');
delete from amp_modules_visibility where name = 'Documents Management';
delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'DOCUMENTS MANAGEMENT');
delete from amp_modules_visibility where name = 'DOCUMENTS MANAGEMENT';