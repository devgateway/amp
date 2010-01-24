INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
VALUES('Activity Status', b'0', b'1', 'activity_status');

INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'Cancelled / Suspended',cl.id,0 FROM amp_category_class cl WHERE cl.keyName='activity_status';
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'Completed',cl.id,1 FROM amp_category_class cl WHERE cl.keyName='activity_status';
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'Considered',cl.id,2 FROM amp_category_class cl WHERE cl.keyName='activity_status';
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'Ongoing',cl.id,3 FROM amp_category_class cl WHERE cl.keyName='activity_status';
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'Planned',cl.id,4 FROM amp_category_class cl WHERE cl.keyName='activity_status';
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT 'Proposed',cl.id,5 FROM amp_category_class cl WHERE cl.keyName='activity_status';


INSERT INTO amp_activities_categoryvalues(amp_activity_id,amp_categoryvalue_id) 
SELECT act.amp_activity_id, val.id FROM amp_activity act, amp_status sta, amp_category_value val
WHERE act.amp_status_id=sta.amp_status_id AND sta.name=val.category_value;

CREATE OR REPLACE VIEW v_status AS SELECT amp_activity_id, category_value AS name, 
	amp_categoryvalue_id AS amp_status_id FROM amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac
	WHERE  acv.id=aac.amp_categoryvalue_id AND acc.id=acv.amp_category_class_id AND acc.keyName='activity_status';
	