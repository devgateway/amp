update `amp_global_settings` set section = 'general';
update `amp_global_settings` set section = 'general', description = 'Enable or disable X-Level features' where settingsName = 'Activity Levels';
update `amp_global_settings` set section = 'general', description = 'Disable or choose number of days between automatic audit logger cleanup' where settingsName = 'Automatic Audit Logger Cleanup';
update `amp_global_settings` set section = 'general', description = 'Country scheme used by AMP' where settingsName = 'Default Country';
update `amp_global_settings` set section = 'general', description = 'The maximum amount of time, in seconds, that a session is allowed to continue without any activity' where settingsName = 'Max Inactive Session Interval';
update `amp_global_settings` set section = 'general', description = 'Enable or disable Public Portfolio' where settingsName = 'Public View';
update `amp_global_settings` set section = 'general', description = 'Displays only on-budget, off-budget, or all activities in Public View' where settingsName = 'Public View Budget Filter';
update `amp_global_settings` set section = 'general', description = 'Site domain for reference activities in messaging system' where settingsName = 'Site Domain';
update `amp_global_settings` set section = 'general', description = 'Sets the default template for the Feature Manager' where settingsName = 'Visibility Template';


update `amp_global_settings` set section = 'funding', description = 'Initial hour for automatic currency rate updates' where settingsName = 'Daily Currency Rates Update Hour';
update `amp_global_settings` set section = 'funding', description = 'Default separator used to separate decimals' where settingsName = 'Default Decimal Separator';
update `amp_global_settings` set section = 'funding', description = 'Default separator for importing files into the Currency Rate Manager' where settingsName = 'Default Exchange Rate Separator';
update `amp_global_settings` set section = 'funding', description = 'Default separator used to separate number groupings' where settingsName = 'Default Grouping Separator';
update `amp_global_settings` set section = 'funding', description = 'Default number format for all funding items' where settingsName = 'Default Number Format';
update `amp_global_settings` set section = 'funding', description = 'Enable or disable daily automatic currency rate updates' where settingsName = 'Enabled Daily Currency Rates Update';
update `amp_global_settings` set section = 'funding' where settingsName = 'Totals include planned';
update `amp_global_settings` set section = 'funding' where settingsName = 'Amounts in Thousands';



update `amp_global_settings` set section = 'date', description = 'The current fiscal year for setting MTEF projections' where settingsName = 'Current Fiscal Year';
update `amp_global_settings` set section = 'date', description = 'Default fiscal calendar' where settingsName = 'Default Calendar';
update `amp_global_settings` set section = 'date', description = 'The default format for all dates' where settingsName = 'Default Date Format';
update `amp_global_settings` set section = 'date', description = 'Select last day of the fiscal year' where settingsName = 'Fiscal Year End Date';
update `amp_global_settings` set section = 'date', description = 'The number of years that appear in all date drop-downs starting from the Year Range Start date' where settingsName = 'Number of Years in Range';
update `amp_global_settings` set section = 'date', description = 'The first year that appears in all date drop-downs' where settingsName = 'Year Range Start';
update `amp_global_settings` set section = 'date' where settingsName = 'Fiscal Year End Date (please enter in the following format: YYYY/MM/DD)';
update `amp_global_settings` set section = 'date', description = 'Default start year for Change Range option' where settingsName = 'Start Year Default Value';
update `amp_global_settings` set section = 'date', description = 'Default end year for Change Range option' where settingsName = 'End Year Default Value';



update `amp_global_settings` set section = 'user', description = 'Order by which components are sorted' where settingsName = 'Components Sort Order';
update `amp_global_settings` set section = 'user', description = 'Component Type by default when Component Type is disabled in the Feature Manager' where settingsName = 'Default Component Type';
update `amp_global_settings` set section = 'user', description = 'Disables or enables filtering reports by specific months as well as years' where settingsName = 'Filter reports by month';
update `amp_global_settings` set section = 'user', description = 'The maximum file size, in megabyes, of content uploaded to Resouces and activities' where settingsName = 'Maximum File Size';
update `amp_global_settings` set section = 'user', description = 'Program that opens by default in NPD' where settingsName = 'NPD Default Program';
update `amp_global_settings` set section = 'user', description = 'View component funding data sorted by year' where settingsName = 'Show Component Funding by Year';




