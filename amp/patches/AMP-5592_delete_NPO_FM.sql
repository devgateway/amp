DELETE FROM amp_fields_templates WHERE EXISTS ( select a.name from amp_fields_visibility a where a.id = amp_fields_templates.field and a.name = 'National Plan Objective' );
delete from amp_fields_visibility where name = 'National Plan Objective';
DELETE FROM amp_fields_templates WHERE EXISTS ( select a.name from amp_fields_visibility a where a.id = amp_fields_templates.field and a.name = 'National Plan Objectives' );
delete from amp_fields_visibility where name = 'National Plan Objectives';