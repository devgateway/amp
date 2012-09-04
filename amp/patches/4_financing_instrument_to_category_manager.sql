
INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Financing Instrument', b'0', b'0', 'financing_instrument');

SET @counter := -1;
INSERT INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	SELECT md.name, acc.id, @counter := @counter + 1 FROM amp_category_class acc, amp_modality md WHERE acc.keyName='financing_instrument';


UPDATE amp_funding af, amp_modality md, amp_category_value acv SET af.financing_instr_category_value_id = acv.id 
	WHERE af.amp_modality_id = md.amp_modality_id AND md.name = acv.category_value;
	
UPDATE amp_eu_activity_contributions eucon, amp_category_value acv, amp_modality md SET eucon.financing_instr_category_value_id = acv.id 
	WHERE md.name = acv.category_value AND eucon.financing_instrument_id = md.amp_modality_id;

UPDATE amp_activity a, amp_category_value acv, amp_modality md SET a.amp_categ_val_modality_id = acv.id 
	WHERE a.amp_modality_id = md.amp_modality_id AND md.name = acv.category_value; 
	
UPDATE amp_eu_activity_contributions eu_con, amp_terms_assist ta, amp_category_value acv SET eu_con.financing_type_categ_val_id = acv.id 
	WHERE ta.terms_assist_name = acv.category_value AND eu_con.financing_type_id = ta.amp_terms_assist_id;

CREATE OR REPLACE VIEW `v_financing_instrument` AS 
	SELECT `f`.`amp_activity_id` AS `amp_activity_id`,`val`.`category_value` AS `name`,`f`.`financing_instr_category_value_id` AS `amp_modality_id` 
	FROM amp_funding f, amp_category_value val WHERE f.financing_instr_category_value_id = val.id 
	ORDER BY `f`.`amp_activity_id`,`val`.`category_value`;
	

CREATE OR REPLACE VIEW `v_contribution_funding` AS 
select `eu`.`amp_activity_id` AS `amp_activity_id`,
`eu`.`id` AS `amp_funding_id`,
`euc`.`id` AS `amp_funding_detail_id`,
o.name AS `donor_name`,
`euc`.`amount` AS `transaction_amount`,
`euc`.`transaction_date` AS `transaction_date`,
`c`.`currency_code` AS `currency_code`,
`acv_term`.`category_value` AS `terms_assist_name`,
acv_mod.category_value as `financing_instrument_name`,
`getExchange`(`c`.`currency_code`,`euc`.`transaction_date`) AS `exchange_rate`
from amp_eu_activity eu, amp_eu_activity_contributions euc, amp_currency c, amp_category_value acv_term, amp_category_value acv_mod, amp_organisation o 
where eu.id=euc.eu_activity_id and euc.amount_currency=c.amp_currency_id and acv_term.id=euc.financing_type_id and 
acv_mod.id = euc.financing_instrument_id and o.amp_org_id=euc.donor_id 
order by amp_activity_id; 

