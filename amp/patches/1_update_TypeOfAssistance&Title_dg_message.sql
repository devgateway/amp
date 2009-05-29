update dg_message set message_key="viz:TypeOfAssistance" where message_key="viz:TypeofAssistance";
update dg_message set message_key="viz:Title" where message_key="viz:title";
DELETE FROM amp_fields_templates WHERE EXISTS ( select a.name from amp_fields_visibility a where 
a.id = amp_fields_templates.field and a.name = 'Type of Assistance' );
delete from amp_fields_visibility where name = 'Type of Assistance';
