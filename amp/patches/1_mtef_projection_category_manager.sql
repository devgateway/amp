INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('MTEF Projection', b'0', b'0', 'mtef_projection');

insert into amp_category_value (category_value, amp_category_class_id, index_column) (select 'projection', id, '0' from amp_category_class where keyname='mtef_projection');
insert into amp_category_value (category_value, amp_category_class_id, index_column) (select 'pipeline', id, '1' from amp_category_class where keyname='mtef_projection');