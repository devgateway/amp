delete from amp_features_templates where feature=(select id from amp_features_visibility where name='Org Profile Widgets');
delete from amp_features_visibility where name='Org Profile Widgets';