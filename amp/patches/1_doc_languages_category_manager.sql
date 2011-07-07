
INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Document Languages', b'0', b'0', 'document_languages');


INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'English', acc.id, 0 FROM amp_category_class acc WHERE acc.keyName='document_languages';
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'French', acc.id, 1 FROM amp_category_class acc WHERE acc.keyName='document_languages';
