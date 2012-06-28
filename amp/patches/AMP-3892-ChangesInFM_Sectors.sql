start transaction;

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Primary');
delete from amp_fields_visibility where name = 'Primary';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Secondary');
delete from amp_fields_visibility where name = 'Secondary';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Primary Sub-Sector');
delete from amp_fields_visibility where name = 'Primary Sub-Sector';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Primary Sub-Sub-Sector');
delete from amp_fields_visibility where name = 'Primary Sub-Sub-Sector';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Secondary Sub-Sector');
delete from amp_fields_visibility where name = 'Secondary Sub-Sector';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Secondary Sub-Sub-Sector');
delete from amp_fields_visibility where name = 'Secondary Sub-Sub-Sector';

delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Sub-Sector');
delete from amp_fields_visibility where name = 'Sub-Sector';

update amp_columns set columnName = 'Primary Sector Sub-Sector' where columnName = 'Primary Sub-Sector';
update amp_columns set columnName = 'Primary Sector Sub-Sub-Sector' where columnName = 'Primary Sub-Sub-Sector';
update amp_columns set columnName = 'Secondary Sector Sub-Sector' where columnName = 'Secondary Sub-Sector';
update amp_columns set columnName = 'Secondary Sector Sub-Sub Sector' where columnName = 'Secondary Sub-Sub Sector';

commit;