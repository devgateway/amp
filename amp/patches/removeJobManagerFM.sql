delete FROM amp_features_templates where feature=(SELECT id FROM amp_features_visibility where name like 'Job Manager');
delete from amp_features_visibility where name like 'Job Manager';