DELETE FROM amp_global_settings WHERE settingsName LIKE "Allow Multiple Sectors";
INSERT INTO amp_global_settings (settingsName,settingsValue,possibleValues,description) VALUES
 ("Multi-Sector Selecting","On","v_global_settings_public_view","Posibility to select multiple sectors in Add Activity Form");
