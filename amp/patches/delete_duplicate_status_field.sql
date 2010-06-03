/*delete duplicate statis visibility field*/
delete from amp_fields_templates where field = (select a.id from amp_fields_visibility a where name = 'status');
delete from amp_fields_visibility  where name = 'status';