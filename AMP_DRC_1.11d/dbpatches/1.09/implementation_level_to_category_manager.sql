INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
VALUES('Implementation Level', b'0', b'1', 'implementation_level');

SET @counter := -1;
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT al.name, cl.id, @counter := @counter+1 AS counter FROM amp_category_class cl, amp_level al  WHERE cl.keyName='implementation_level';
	



INSERT INTO amp_activities_categoryvalues(amp_activity_id, amp_categoryvalue_id) 
	SELECT act.amp_activity_id, val.id FROM amp_activity act, amp_level lev, amp_category_value val
	WHERE act.amp_level_id=lev.amp_level_id AND lev.name=val.category_value;

CREATE OR REPLACE VIEW v_implementation_level AS SELECT amp_activity_id, category_value AS name, 
	amp_categoryvalue_id AS level_code FROM amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac
	WHERE  acv.id=aac.amp_categoryvalue_id AND acc.id=acv.amp_category_class_id AND acc.keyName='implementation_level';
	