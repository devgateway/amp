insert INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Activity Level', b'0', b'0', 'activity_level');

insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT 'Level 1',acc.id,0 FROM amp_category_class acc WHERE acc.keyName='activity_level'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT 'Level 2',acc.id,1 FROM amp_category_class acc WHERE acc.keyName='activity_level'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT 'Level 3',acc.id,2 FROM amp_category_class acc WHERE acc.keyName='activity_level'); 		

