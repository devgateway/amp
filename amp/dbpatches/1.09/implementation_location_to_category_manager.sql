INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
VALUES('Implementation Location', b'0', b'0', 'implementation_location');

INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'Country', cl.id, 0 FROM amp_category_class cl WHERE cl.keyName='implementation_location';

INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'Region', cl.id, 1 FROM amp_category_class cl WHERE cl.keyName='implementation_location';

INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'Zone', cl.id, 2 FROM amp_category_class cl WHERE cl.keyName='implementation_location';

INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'District', cl.id, 3 FROM amp_category_class cl WHERE cl.keyName='implementation_location';

