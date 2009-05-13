CREATE OR REPLACE VIEW v_component_type AS 
 SELECT aac.amp_activity_id AS amp_activity_id, 
  ct.name AS component_type, 
  ct.type_id AS component_type_id 
  FROM amp_components c, amp_component_type ct, amp_activity_components aac 
  WHERE c.type=ct.type_id AND aac.amp_component_id = c.amp_component_id;
  