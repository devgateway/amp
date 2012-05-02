delete from amp_fields_templates where field in (select id from amp_fields_visibility where name in ("Project Performance","Project Risk"));
delete from amp_fields_visibility where name in ("Project Performance","Project Risk");
