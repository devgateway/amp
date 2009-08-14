UPDATE amp_global_settings SET  description = 'The default value in a dropdown for year START selection. Enter -1 to disable it'
	WHERE settingsName ='Start Year Default Value';

UPDATE amp_global_settings SET  description = 'The default value in a dropdown for year END selection. Enter -1 to disable it'
	WHERE settingsName ='End Year Default Value';