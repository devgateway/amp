CREATE OR REPLACE VIEW `v_donor_commitment_date` AS 
select  `f`.`amp_activity_id`,`fd`.`transaction_date`
from amp_funding f, amp_funding_detail fd where 
`f`.`amp_funding_id` = `fd`.`AMP_FUNDING_ID` and fd.transaction_type=0
order by `f`.`amp_activity_id`, fd.transaction_date;