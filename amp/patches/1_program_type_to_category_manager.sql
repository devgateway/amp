
INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Program Type', b'0', b'0', 'program_type');

SET @counter := -1;
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT ty.title, acc.id, @counter := @counter + 1 FROM amp_category_class acc, amp_program_type ty WHERE acc.keyName='program_type'); 

UPDATE amp_theme th, amp_category_value val SET th.type_category_value_id = val.id 
	WHERE th.type = val.category_value;