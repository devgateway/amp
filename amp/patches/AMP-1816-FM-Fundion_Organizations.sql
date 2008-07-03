INSERT INTO `amp_features_visibility`(name, parent, hasLevel) VALUES ('Funding Information',(SELECT id FROM `amp_modules_visibility` WHERE name= 'Funding'),true);

UPDATE `amp_columns_order` SET columnName = 'Funding Information' WHERE columnName = 'Funding Organizations';

UPDATE `amp_fields_visibility` SET parent = (SELECT id FROM `amp_features_visibility` WHERE name = 'Funding Information') WHERE parent =(SELECT id FROM `amp_features_visibility` where name = 'Funding Organizations');

DELETE FROM `amp_features_templates` WHERE `amp_features_templates`.`feature` = (SELECT `amp_features_visibility`.`id` FROM `amp_features_visibility` WHERE `amp_features_visibility`.`name` = 'Funding Organizations');

DELETE FROM `amp_features_visibility` WHERE name = 'Funding Organizations';