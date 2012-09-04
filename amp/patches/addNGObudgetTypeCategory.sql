INSERT INTO `amp_category_class`(category_name,is_ordered,keyName,is_multiselect) 
SELECT 'NGO Budget Type', false, 'ngo_budget_type',false from dual where 
(select count(id) from amp_category_class where category_name like 'ngo_budget_type')=0;

INSERT INTO `amp_category_value` (`category_value`,`amp_category_class_id`,index_column)
SELECT 'Annual budget of internal/administrative functioning',(select id from amp_category_class where category_name like 'ngo_budget_type'),0 FROM DUAL
 UNION 
SELECT 'Total Amount',(select id from amp_category_class where category_name like 'ngo_budget_type'),1 FROM DUAL
 UNION 
SELECT 'Organization personal resources',(select id from amp_category_class where category_name like 'ngo_budget_type'),2 FROM DUAL
 UNION 
SELECT 'Donors resources',(select id from amp_category_class where category_name like 'ngo_budget_type'),3 FROM DUAL;
