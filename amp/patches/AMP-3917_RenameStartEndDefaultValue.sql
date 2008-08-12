START TRANSACTION;

UPDATE amp_global_settings SET settingsName = 'Change Range Default Start Value' WHERE settingsName = 'Start Year Default Value';
UPDATE amp_global_settings SET settingsName = 'Change Range Default End Value' WHERE settingsName = 'End Year Default Value';

COMMIT;