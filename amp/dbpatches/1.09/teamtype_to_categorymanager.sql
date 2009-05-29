INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
VALUES('Team Type', b'0', b'1', 'team_type');

INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT "Bilateral", cl.id, 0 AS counter FROM amp_category_class cl  WHERE cl.keyName='team_type';
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT "Multilateral", cl.id, 1 AS counter FROM amp_category_class cl  WHERE cl.keyName='team_type';

	
UPDATE amp_team at, amp_category_value acv SET at.type_categoryvalue_id=acv.id 
	WHERE acv.category_value=at.type;
