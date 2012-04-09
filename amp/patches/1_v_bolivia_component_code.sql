CREATE OR REPLACE VIEW `v_bolivia_component_code` AS 
	select a.amp_activity_id,c.code  from amp_activity_components a inner join amp_components c on a.amp_component_id=c.amp_component_id
	ORDER BY a.`amp_activity_id`;
	
