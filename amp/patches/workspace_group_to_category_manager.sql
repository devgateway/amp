
INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Workspace Group', b'0', b'0', 'workspace_group');

insert into amp_category_value (category_value, amp_category_class_id, index_column) (select 'Government', id, '0' from amp_category_class where keyname='workspace_group');
insert into amp_category_value (category_value, amp_category_class_id, index_column) (select 'Line Ministries', id, '1' from amp_category_class where keyname='workspace_group');
insert into amp_category_value (category_value, amp_category_class_id, index_column) (select 'Donor', id, '2' from amp_category_class where keyname='workspace_group');
