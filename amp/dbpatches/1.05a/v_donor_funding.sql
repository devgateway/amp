CREATE OR REPLACE VIEW `v_donor_funding` AS select 
`f`.`amp_activity_id` AS `amp_activity_id`,
`f`.`amp_funding_id` AS `amp_funding_id`,
`fd`.`amp_fund_detail_id` AS `amp_fund_detail_id`,
 d.name as `donor_name`,
`fd`.`transaction_type` AS `transaction_type`,
`fd`.`adjustment_type` AS `adjustment_type`,
 date_format(`fd`.`transaction_date`,_latin1'%Y-%m-%d') AS `transaction_date`,
`fd`.`transaction_amount` AS `transaction_amount`,`c`.`currency_code` AS `currency_code`,
`ta`.`terms_assist_name` AS `terms_assist_name`,`getExchange`(`c`.`currency_code`,`fd`.`transaction_date`) 
AS `exchange_rate`,
p.code AS perspective_code
 from  ((((`amp_funding` `f` join `amp_funding_detail` `fd`) join `amp_terms_assist` `ta`) 
join `amp_currency` `c`) join `amp_organisation` d), amp_perspective p where ((`c`.`amp_currency_id` = `fd`.`amp_currency_id`) 
and (`f`.`amp_funding_id` = `fd`.`AMP_FUNDING_ID`) and p.amp_perspective_id=fd.perspective_id and
(`ta`.`amp_terms_assist_id` = `f`.`amp_terms_assist_id`) and (d.amp_org_id=f.amp_donor_org_id))
 order by `f`.`amp_activity_id`