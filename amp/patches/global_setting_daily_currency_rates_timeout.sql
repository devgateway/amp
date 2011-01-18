insert into amp_global_settings(settingsName,settingsValue,possibleValues, description, section) values(
'Daily Currency Rates Update Timeout','4','t_timeout_currency_update','Waiting time for web service connection','funding');

update amp_global_settings set settingsName='Daily Currency Rates Update Enabled' where settingsName='Enabled Daily Currency Rates Update';