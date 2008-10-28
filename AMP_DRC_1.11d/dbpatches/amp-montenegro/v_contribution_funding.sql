CREATE OR REPLACE VIEW `v_contribution_funding` AS 
select `eu`.`amp_activity_id` AS `amp_activity_id`,
`eu`.`id` AS `amp_funding_id`,
`euc`.`id` AS `amp_funding_detail_id`,
o.name AS `donor_name`,
`euc`.`amount` AS `transaction_amount`,
`euc`.`transaction_date` AS `transaction_date`,
`c`.`currency_code` AS `currency_code`,
`ta`.`terms_assist_name` AS `terms_assist_name`,
m.name as `financing_instrument_name`,
`getExchange`(`c`.`currency_code`,`euc`.`transaction_date`) AS `exchange_rate`
from amp_eu_activity eu, amp_eu_activity_contributions euc, amp_currency c, amp_terms_assist ta, amp_modality m, amp_organisation o
where eu.id=euc.eu_activity_id and euc.amount_currency=c.amp_currency_id and ta.amp_terms_assist_id=euc.financing_type_id and
m.amp_modality_id = euc.financing_instrument_id and o.amp_org_id=euc.donor_id
order by amp_activity_id;