delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'My Messages');
delete from amp_features_visibility where parent in ( select id from amp_modules_visibility where name = 'My Messages');
delete from amp_modules_visibility where name = 'My Messages';