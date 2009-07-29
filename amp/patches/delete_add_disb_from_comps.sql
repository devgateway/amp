delete from amp_fields_templates where field in (select id from amp_fields_visibility where parent in (select id from amp_features_visibility where name="Disbursements of Components"));
delete from amp_fields_visibility where parent in (select id from amp_features_visibility where name="Disbursements of Components");
delete from amp_features_templates where feature in (select id from amp_features_visibility where name="Disbursements of Components");
delete from amp_features_visibility where name="Disbursements of Components"