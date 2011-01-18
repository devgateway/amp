delete FROM amp_fields_templates where field=(SELECT id FROM amp_fields_visibility where name like 'Email');
delete from amp_fields_visibility where name like 'Email';

delete FROM amp_fields_templates where field=(SELECT id FROM amp_fields_visibility where name like 'Fax Number');
delete from amp_fields_visibility where name like 'Fax Number';

delete FROM amp_fields_templates where field=(SELECT id FROM amp_fields_visibility where name like 'First name');
delete from amp_fields_visibility where name like 'First name';

delete FROM amp_fields_templates where field=(SELECT id FROM amp_fields_visibility where name like 'Last Name');
delete from amp_fields_visibility where name like 'Last Name';

delete FROM amp_fields_templates where field=(SELECT id FROM amp_fields_visibility where name like 'Organization');
delete from amp_fields_visibility where name like 'Organization';

delete FROM amp_fields_templates where field=(SELECT id FROM amp_fields_visibility where name like 'Phone Number');
delete from amp_fields_visibility where name like 'Phone Number';