start transaction;

insert into amp_global_settings (settingsName, settingsValue, possibleValues, description, section)
values ('Activate Auto Login', 'false', 't_Boolean', 'Enable or disable the Auto Login option.', 'general');

commit;