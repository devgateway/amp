delete from amp_message_state where message_id in (select amp_message_id from amp_message 
where message_clazz='t' and related_trigger_name like '-1' or related_trigger_name like 'org.digijava.module.message.triggers.UserRegistrationTrigger'); 

delete from amp_message where message_clazz like 't' 
and related_trigger_name like '-1' or related_trigger_name like 'org.digijava.module.message.triggers.UserRegistrationTrigger' ; 
