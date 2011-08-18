DELETE FROM amp_global_settings WHERE settingsName='Number of Years in Range';
DELETE FROM amp_global_settings WHERE settingsName='Year Range Start';

INSERT INTO amp_global_settings(settingsName, settingsValue, possibleValues, description)
	VALUES('Year Range Start', '1980', 't_Year' , 'The first year in a dropdown for year selection');
	
INSERT INTO amp_global_settings(settingsName, settingsValue, possibleValues, description)
	VALUES('Number of Years in Range', '100', 't_Integer' , 'Number of years in a dropdown starting from Year Start Range');
	
INSERT INTO amp_global_settings(settingsName, settingsValue, possibleValues, description)
	VALUES('Start Year Default Value', '2001', 't_Year' , 'The default value in a dropdown for year START selection. Enter -1 to disable it');
	
INSERT INTO amp_global_settings(settingsName, settingsValue, possibleValues, description)
	VALUES('End Year Default Value', '2007', 't_Year' , 'The default value in a dropdown for year END selection. Enter -1 to disable it');