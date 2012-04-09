delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Financial Progress');
delete from amp_features_visibility where name = 'Financial Progress';

delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'Financial Progress');
delete from amp_modules_visibility where name = 'Financial Progress';