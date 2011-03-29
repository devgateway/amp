INSERT INTO amp_category_class (category_name, keyName, description, is_multiselect, is_ordered) 
	VALUES ("Activity Budget", "activity_budget", "Please make sure you have values 'Off' and 'On' in this category !", false, true);
	
INSERT INTO amp_category_value (category_value, amp_category_class_id, index_column )
	SELECT "Off",id,0 FROM amp_category_class WHERE keyName="activity_budget";
INSERT INTO amp_category_value (category_value, amp_category_class_id, index_column )
	SELECT "On",id,1 FROM amp_category_class WHERE keyName="activity_budget";
INSERT INTO amp_category_value (category_value, amp_category_class_id, index_column )
	SELECT "Treasure",id,2 FROM amp_category_class WHERE keyName="activity_budget";
	
INSERT INTO amp_activities_categoryvalues (amp_activity_id, amp_categoryvalue_id) 
	SELECT a.amp_activity_id, acv.id FROM amp_activity a, amp_category_value acv, amp_category_class acc 
		WHERE acv.amp_category_class_id=acc.id AND a.budget=acv.index_column AND 
			acc.keyName="activity_budget";
			

CREATE or REPLACE VIEW `v_on_off_budget` AS 
		SELECT `a`.`amp_activity_id` AS `amp_activity_id`,
		IF(acv.category_value="On","yes",IF(acv.category_value="Off","no",IF(acv.id is null,"unallocated", acv.category_value))) AS `budget` 
		
		FROM 
		amp_activities_categoryvalues aac  
		 JOIN amp_category_value acv on aac.amp_categoryvalue_id=acv.id 
		 JOIN amp_category_class acc on (acc.id=acv.amp_category_class_id AND acc.keyName="activity_budget")
		 RIGHT JOIN amp_activity a ON  a.amp_activity_id=aac.amp_activity_id
		
		order by `a`.`amp_activity_id`;
		
