--Composite patch
--Apply to version - AMP 1.05a

-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.24-standard


INSERT INTO `amp_columns` (`columnId`,`columnName`,`aliasName`,`cellType`,`extractorView`) VALUES 
 (24,'Physical Progress','physical_progress','org.dgfoundation.amp.ar.cell.TextCell','v_physical_progress');  
  
UPDATE amp_measures SET measureName='Undisbursed Balance' WHERE measureId=7;
UPDATE amp_measures SET aliasName='Undisbursed Balance' WHERE measureId=7;

ALTER TABLE dg_cms_content_item MODIFY FILE_CONTENT longblob;
 
drop function getExchange;
delimiter $
CREATE FUNCTION getExchange(currency char(3), cdate datetime) RETURNS double
begin
declare r double;
DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN END;
if currency='USD' then return 1;
end if;
select exchange_rate into r from amp_currency_rate where to_currency_code=currency and exchange_rate_date=cdate;
if r is not null then return r;
end if;
select exchange_rate into r from amp_currency_rate where to_currency_code=currency and exchange_rate_date<=cdate order by exchange_rate_date desc limit 1;
if r is not null then return r;
end if;
select exchange_rate into r from amp_currency_rate where to_currency_code=currency and exchange_rate_date>=cdate order by exchange_rate_date limit 1;
if r is not null then return r;
end if;
return 1;
END;$
delimiter ;

ALTER TABLE DG_MESSAGE MODIFY MESSAGE_UTF8 TEXT;

CREATE OR REPLACE VIEW `v_actors`  AS 
select `ai`.`amp_activity_id` AS `amp_activity_id`,`act`.`name` AS `name`,
`act`.`amp_actor_id` AS `amp_actor_id` from 
(((`amp_activity` `a` join `amp_measure` `m`) join `amp_issues` `ai`) 
join `amp_actor` `act`) where ((`ai`.`amp_activity_id` = `a`.`amp_activity_id`) and
 (`ai`.`amp_issue_id` = `m`.`amp_issue_id`) and 
 (`act`.`amp_measure_id` = `m`.`amp_measure_id`)) order by `ai`.`amp_activity_id`;
 
CREATE OR REPLACE VIEW `v_actual_approval_date` AS 
select `amp_activity`.`amp_activity_id` AS 
`amp_activity_id`,`amp_activity`.`actual_approval_date` AS 
`actual_approval_date` from `amp_activity` order by `amp_activity`.`amp_activity_id`;

CREATE OR REPLACE VIEW `v_component_funding` AS 
select `f`.`activity_id` AS `amp_activity_id`,
`f`.`amp_component_funding_id` AS `amp_component_funding_id`,
`f`.`amp_component_funding_id` AS `amp_fund_detail_id`,
`c`.`title` AS `component_name`,
`f`.`transaction_type` AS `transaction_type`,
`f`.`adjustment_type` AS `adjustment_type`,
date_format(`f`.`transaction_date`,_latin1'%Y-%m-%d') AS `transaction_date`,
`f`.`transaction_amount` AS `transaction_amount`,
`f`.`currency_id` AS `currency_id`,
`cu`.`currency_code` AS `currency_code`,
`getExchange`(`cu`.`currency_code`,`f`.`transaction_date`) AS `exchange_rate`,
p.code as perspective_code
from amp_perspective p, ((`amp_component_funding` `f` join `amp_components` `c`) join `amp_currency` `cu`) 
where p.amp_perspective_id=f.perspective_id and ((`cu`.`amp_currency_id` = `f`.`currency_id`) 
and (`f`.`amp_component_id` = `c`.`amp_component_id`)) 
order by `f`.`activity_id`;

CREATE OR REPLACE VIEW `v_donor_commitment_date` AS 
select  `f`.`amp_activity_id`,`fd`.`transaction_date`
from amp_funding f, amp_funding_detail fd where 
`f`.`amp_funding_id` = `fd`.`AMP_FUNDING_ID` and fd.transaction_type=0
order by `f`.`amp_activity_id`, fd.transaction_date;

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
 order by `f`.`amp_activity_id`;
 
CREATE OR REPLACE VIEW `v_financing_instrument` AS 
select `f`.`amp_activity_id` AS `amp_activity_id`,
`m`.`name` AS `name`,`f`.`amp_modality_id` AS `amp_modality_id` from 
(`amp_funding` `f` join `amp_modality` `m` on((`f`.`amp_modality_id` = `m`.`amp_modality_id`))) 
order by `f`.`amp_activity_id`,`m`.`name`;

CREATE OR REPLACE VIEW `v_issues` AS 
select `ai`.`amp_activity_id` AS `amp_activity_id`,`ai`.`name` AS `name`,
`ai`.`amp_issue_id` AS `amp_issue_id` from (`amp_issues` `ai` join `amp_activity` `a`) 
where (`ai`.`amp_activity_id` = `a`.`amp_activity_id`) order by `ai`.`amp_activity_id`;

CREATE OR REPLACE VIEW `v_measures_taken` AS 
select `ai`.`amp_activity_id` AS `amp_activity_id`,
`m`.`name` AS `name`,`m`.`amp_measure_id` AS `amp_measure_id` 
from ((`amp_activity` `a` join `amp_measure` `m`) join `amp_issues` `ai`) 
where ((`ai`.`amp_activity_id` = `a`.`amp_activity_id`) and 
(`ai`.`amp_issue_id` = `m`.`amp_issue_id`)) order by `ai`.`amp_activity_id`;

CREATE OR REPLACE VIEW v_physical_progress AS
select p.amp_activity_id, p.description, p.amp_pp_id FROM
amp_physical_performance p order by p.reporting_date;

CREATE OR REPLACE VIEW `v_regional_funding` AS select 
`f`.`activity_id` AS `amp_activity_id`,
`f`.`amp_regional_funding_id` AS `amp_regional_funding_id`,
`f`.`amp_regional_funding_id` AS `amp_fund_detail_id`,
`r`.`name` AS `region_name`,
`f`.`transaction_type` AS `transaction_type`,
`f`.`adjustment_type` AS `adjustment_type`,
date_format(`f`.`transaction_date`,_latin1'%Y-%m-%d') AS `transaction_date`,
`f`.`transaction_amount` AS `transaction_amount`,`c`.`currency_code` AS `currency_code`,
`getExchange`(`c`.`currency_code`,`f`.`transaction_date`) AS `exchange_rate`,
p.code as perspective_code
from amp_perspective p,((`amp_regional_funding` `f` join `amp_region` `r`) join `amp_currency` `c`) where 
p.amp_perspective_id=f.perspective_id and
((`c`.`amp_currency_id` = `f`.`currency_id`) and (`f`.`region_id` = `r`.`amp_region_id`)) order by `f`.`activity_id`;

CREATE OR REPLACE VIEW `v_teams` AS 
select `a`.`amp_activity_id` AS `amp_activity_id`,`t`.`name` AS 
`name`,`t`.`amp_team_id` AS `amp_team_id` from 
(`amp_activity` `a` join `amp_team` `t`) where 
(`a`.`amp_team_id` = `t`.`amp_team_id`) 
order by `a`.`amp_activity_id`,`t`.`amp_team_id`;