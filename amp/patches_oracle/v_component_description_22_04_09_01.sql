CREATE OR REPLACE  VIEW v_component_description 
AS 
select distinct a.amp_activity_id AS amp_activity_id,b.description AS title,b.amp_component_id AS amp_component_id 
from amp_activity a , amp_components b , amp_component_funding c
where ((a.amp_activity_id = c.activity_id) 
and (b.amp_component_id = c.amp_component_id));