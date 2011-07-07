UPDATE amp_columns SET columnName='On/Off/Treasury Budget' WHERE columnName='On/Off/Treasure Budget';
UPDATE amp_category_value SET category_value='Treasury' WHERE category_value='Treasure';
UPDATE amp_fields_visibility SET name='On/Off/Treasury Budget' WHERE name='On/Off/Treasure Budget';


CREATE or REPLACE VIEW `v_on_off_budget` AS 
		SELECT `a`.`amp_activity_id` AS `amp_activity_id`,
		IF(acv.id is null,"Unallocated", acv.category_value) AS `budget` 
		FROM 
		amp_activities_categoryvalues aac  
		 JOIN amp_category_value acv on aac.amp_categoryvalue_id=acv.id 
		 JOIN amp_category_class acc on (acc.id=acv.amp_category_class_id AND acc.keyName="activity_budget")
		 RIGHT JOIN amp_activity a ON  a.amp_activity_id=aac.amp_activity_id
		order by `a`.`amp_activity_id`;
		
UPDATE amp_category_value cv, amp_category_class cc SET cv.category_value='Off Budget' where cc.id=cv.amp_category_class_id and cv.category_value = 'Off' and cc.keyName='activity_budget';
UPDATE amp_category_value cv, amp_category_class cc SET cv.category_value='On Budget' where cc.id=cv.amp_category_class_id and cv.category_value = 'On' and cc.keyName='activity_budget';