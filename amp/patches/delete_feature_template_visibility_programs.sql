delete a from amp_features_templates a inner join amp_features_visibility b on a.feature=b.id where b.name='Programs';
delete from amp_features_visibility where name='Programs';