delete FROM amp_fields_templates where field=(SELECT id FROM amp_fields_visibility where name like 'jointCriteria');
delete from amp_fields_visibility where name like 'jointCriteria';