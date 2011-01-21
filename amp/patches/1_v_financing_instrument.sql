
CREATE OR REPLACE VIEW `v_financing_instrument` AS 
	( SELECT `f`.`amp_activity_id` AS `amp_activity_id`,`val`.`category_value` AS `name`,`f`.`financing_instr_category_value_id` AS `amp_modality_id`  
	FROM amp_funding f, amp_category_value val WHERE f.financing_instr_category_value_id = val.id ) 
	UNION
	( SELECT eu.amp_activity_id, val.category_value, eu_con.financing_instr_category_value_id 
	FROM amp_eu_activity eu, amp_eu_activity_contributions eu_con, amp_category_value val 
	WHERE eu_con.eu_activity_id = eu.id AND eu_con.financing_instr_category_value_id = val.id )
	ORDER BY `amp_activity_id`,`name` ;
	
