CREATE OR REPLACE VIEW `v_g_settings_countries` AS 
 select l.iso as id, l.location_name as value from amp_category_value_location l, amp_category_value v, amp_category_class c
 WHERE c.keyName="implementation_location" AND v.category_value="Country" AND v.amp_category_class_id=c.id 
 AND l.parent_category_value=v.id AND l.iso is not null AND length(l.iso)>0 order by l.location_name;