INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
VALUES('Type of Assistence', b'0', b'1', 'type_of_assistence');

SET @counter := -1;
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT ata.terms_assist_name,acc.id,@counter := @counter + 1 FROM amp_category_class acc, amp_terms_assist ata WHERE acc.keyName='type_of_assistence';



UPDATE amp_funding af, amp_terms_assist ats, amp_category_value acv SET af.type_of_assistance_category_value_id = acv.id 
	WHERE af.amp_terms_assist_id = ats.amp_terms_assist_id AND ats.terms_assist_name = acv.category_value;




	