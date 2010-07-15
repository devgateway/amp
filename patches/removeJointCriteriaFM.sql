delete FROM amp_fields_templates where feature=(SELECT id FROM amp_fields_visibility where name like 'jointCriteria');
delete from amp_fields_visibility where name like 'jointCriteria';