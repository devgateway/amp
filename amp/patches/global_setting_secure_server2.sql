delete from amp_global_settings where settingsName like "Secure Server";
insert into amp_global_settings(settingsName,settingsValue,possibleValues, description, section) 
values('Secure Server','off','t_secure_values',
'Should the sever be secured through SSL', 'general');
