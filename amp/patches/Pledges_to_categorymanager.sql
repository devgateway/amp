INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName)
VALUES('Pledges Types', b'0', b'1', 'pledges_types');


INSERT INTO amp_category_value(category_value, amp_category_class_id,index_column )
VALUES ('Reprogrammed funds',(select id from amp_category_class where category_name ='Pledges Types'),0);


INSERT INTO amp_category_value(category_value, amp_category_class_id,index_column)
VALUES ('New funds',(select id from amp_category_class where category_name ='Pledges Types'),0);


	