create or replace view v_g_settings_currency as select currency_code as id, currency_name as value from amp_currency;

insert into amp_global_settings (settingsName, settingsValue, possibleValues, description, section ) 
	values ("Base Currency", "USD", "v_g_settings_currency", "The base currency. All conversions are done through this currency.", "funding");