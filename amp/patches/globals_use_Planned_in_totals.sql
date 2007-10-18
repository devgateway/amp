DELETE FROM amp_global_settings WHERE settingsName='Totals include planned';

INSERT INTO amp_global_settings(settingsName, settingsValue, possibleValues, description)
	VALUES('Totals include planned', 'Off', 'v_global_settings_public_view' , 'Use planned comitments to calculate total cost of activity');
	

