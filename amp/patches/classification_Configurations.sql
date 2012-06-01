INSERT INTO AMP_CLASSIFICATION_CONFIG(name,multisector,is_primary_sector,classification_id)
VALUES("Primary",true,true,(SELECT settingsValue FROM amp_global_settings WHERE settingsName='Default Sector Scheme'));
INSERT INTO AMP_CLASSIFICATION_CONFIG(name,multisector,is_primary_sector,classification_id)
VALUES("Secondary",true,false,(SELECT settingsValue FROM amp_global_settings WHERE settingsName='Default Sector Scheme'));
UPDATE AMP_ACTIVITY_SECTOR set classification_config_id=(SELECT id FROM AMP_CLASSIFICATION_CONFIG WHERE name='Primary') where classification_config_id is null;
DELETE FROM amp_global_settings where settingsName in('Multi-Sector Selecting','Default Sector Scheme');