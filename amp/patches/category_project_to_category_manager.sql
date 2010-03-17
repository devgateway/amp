INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName)
VALUES('Project Category', b'0', b'0', 'project_category');

INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column, field_type ) 
	SELECT 'Government', cl.id, 0, 0 FROM amp_category_class cl WHERE cl.keyName='project_category';

INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column, field_type )
	SELECT 'Independent Structure Under Government Control/Supervision', cl.id, 1, 0 FROM amp_category_class cl WHERE cl.keyName='project_category';

INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column, field_type )
	SELECT 'Independent Executing Agency (not related to the Government)', cl.id, 2, 0 FROM amp_category_class cl WHERE cl.keyName='project_category';