CREATE OR REPLACE VIEW v_activity_budget AS SELECT amp_activity_id, category_value AS name, 
	amp_categoryvalue_id AS budget_id FROM amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac
	WHERE  acv.id=aac.amp_categoryvalue_id AND acc.id=acv.amp_category_class_id AND acc.keyName='activity_budget';