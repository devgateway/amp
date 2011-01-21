delete from amp_modules_templates where module in (select id from amp_modules_visibility where name  LIKE 'Project Id And Planning');
delete from amp_features_visibility where parent in ( select id from amp_modules_visibility where name LIKE 'Project Id And Planning');
delete from amp_modules_visibility where name LIKE 'Project Id And Planning';