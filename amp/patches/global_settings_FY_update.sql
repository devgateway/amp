DELETE FROM amp_global_settings WHERE settingsName='Current Fiscal Year';

INSERT INTO amp_global_settings(settingsName, settingsValue, possibleValues, description)
	VALUES('Current Fiscal Year', '2007', 'v_global_settings_current_fiscal_year' , 'Select current fiscal year');
	
INSERT INTO amp_global_settings(settingsName, settingsValue, possibleValues, description)
	VALUES('Fiscal Year End Date', '01/07', 't_Date_No_Year' , 'Select fiscal year end date');