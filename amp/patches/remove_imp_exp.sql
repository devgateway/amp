delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Activity Imp&Exp' );
delete from amp_fields_visibility where name = 'Activity Imp&Exp' ;

delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'Activity Imp&Exp' );
delete from amp_modules_visibility where name = 'Activity Imp&Exp' ;