UPDATE amp_global_settings SET  possibleValues = 't_static_year' 
	WHERE settingsName ='Year Range Start';
UPDATE amp_global_settings SET  possibleValues = 't_static_range' 
	WHERE settingsName ='Number of Years in Range'; 
UPDATE amp_global_settings SET  possibleValues = 't_year_default_start', 
	description = 'The default value in a dropdown for year START selection. It can be disabled.'
	WHERE settingsName ='Start Year Default Value';
UPDATE amp_global_settings SET  possibleValues = 't_year_default_end', 
	description = 'The default value in a dropdown for year END selection. It can be disabled.' 
	WHERE settingsName ='End Year Default Value'; 

