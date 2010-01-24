
INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Financial Instrument', b'0', b'0', 'financial_instrument');

insert into amp_category_value (category_value, amp_category_class_id, index_column) (select 'GBS', id, '0' from amp_category_class where keyname='financial_instrument');
insert into amp_category_value (category_value, amp_category_class_id, index_column) (select 'SBS', id, '1' from amp_category_class where keyname='financial_instrument');
insert into amp_category_value (category_value, amp_category_class_id, index_column) (select 'Basket', id, '2' from amp_category_class where keyname='financial_instrument');
insert into amp_category_value (category_value, amp_category_class_id, index_column) (select 'DPS on Budget', id, '3' from amp_category_class where keyname='financial_instrument');
