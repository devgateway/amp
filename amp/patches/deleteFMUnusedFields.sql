delete from amp_modules_templates where module=(select id from amp_modules_visibility where name='Message Manager');
delete from amp_features_templates where feature=(select id from amp_features_visibility where name='Job Manager');

delete from amp_feature_source where module_id=(select id from amp_modules_visibility where name='Message Manager');
delete from amp_feature_source where feature_id=(select id from amp_features_visibility where name='Job Manager');

delete from amp_modules_visibility where name='Message Manager' ;
delete from amp_features_visibility where name='Job Manager';