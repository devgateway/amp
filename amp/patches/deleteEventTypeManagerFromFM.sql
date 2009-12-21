delete from amp_modules_templates where module=(select id from amp_modules_visibility where name='Event Type Manager');

delete from amp_feature_source where module_id=(select id from amp_modules_visibility where name='Event Type Manager');

delete from amp_modules_visibility where name='Event Type Manager' ;