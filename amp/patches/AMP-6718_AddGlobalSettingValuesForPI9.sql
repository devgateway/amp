CREATE OR REPLACE VIEW `v_g_settings_BSO` AS 
	SELECT cv.id AS `id`,cv.category_value AS `value` 
	FROM amp_category_value cv, amp_category_class cc 
	WHERE cv.amp_category_class_id = cc.id 
	AND cc.keyName = 'financing_instrument';

INSERT INTO amp_global_settings (`settingsName`,`settingsValue`,`possibleValues`,`description`,`section`) 
	SELECT 'Budget Support for PI 9', '', 'v_g_settings_BSO','Select one or more Budget Support types that will be used for calculating PI 9.', 'general' FROM DUAL  
	WHERE NOT EXISTS (SELECT * FROM amp_global_settings WHERE settingsName LIKE 'Budget Support for PI 9');