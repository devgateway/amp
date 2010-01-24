delete from amp_features_templates where feature=(select id from amp_features_visibility where name="yuiampmenuitemlabel");
delete from amp_features_visibility where name="yuiampmenuitemlabel";
