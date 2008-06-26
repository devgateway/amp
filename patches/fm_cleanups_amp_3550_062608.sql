delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Sectors Percentage");
delete from amp_fields_visibility where name="Sectors Percentage";

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Sector");
delete from amp_fields_visibility where name="Sector";

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Primary");
delete from amp_fields_visibility where name="Primary";

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Sub-Sector");
delete from amp_fields_visibility where name="Sub-Sector";

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Sub-Sub-Sector");
delete from amp_fields_visibility where name="Sub-Sub-Sector";
