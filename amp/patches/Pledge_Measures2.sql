insert into amp_measures
	(measureName, aliasName, type, expression, description) 
values 
	('Actual Pledge','Actual Pledge','P',null, null);
	
insert into amp_measures
	(measureName, aliasName, type, expression, description) 
values 
	('Commitment Gap','Commitment Gap','P','commitmentGap',
	'Total Pledge - Total Actual Commitments');
	

insert into amp_measures
	(measureName, aliasName, type, expression, description) 
values 
	('Percentage of Disbursement','Percentage of Disbursement','P','percentageOfDisbursement',
	'(Total Actual Disbursements for Year,Quarter,Month / Total Actual Disbursements) * 100');
	
	
delete from amp_report_measures where measureId in (select measureId from amp_measures where measureName like "%Pledges%");
delete from amp_measures where measureName like "%Pledges%";
insert into amp_measures
	(measureName, aliasName, type, expression, description) 
values 
	('Actual Commitments','Actual Commitments','P',null, null),
	('Actual Disbursements','Actual Disbursements','P',null, null),
	('Planned Commitments','Pledges Planned Commitments','P',null, null),
	('Planned Disbursements','Pledges Planned Disbursements','P',null, null);

CREATE OR REPLACE VIEW   v_pledges_funding_st  AS 
	SELECT   `f`.`id` AS `pledge_id`,
         `d`.`name` AS `donor_name`,
         `fd`.`pledge_id` AS `amp_fund_detail_id`,
          `afd`.`transaction_type` AS `transaction_type`,
          `afd`.`adjustment_type` AS `adjustment_type`,
         `afd`.`transaction_date` AS `transaction_date`,
         `afd`.`transaction_amount` AS `transaction_amount`,
         `c`.`currency_code` AS `currency_code`,
         `cval`.`id` AS `terms_assist_id`,
         `cval`.`category_value` AS `terms_assist_name`,
         `b`.`org_grp_name` AS `org_grp_name`,
         `ot`.`org_type` AS `donor_type_name`,
         `cval2`.`category_value` AS `financing_instrument_name`,
         `cval2`.`id` AS `financing_instrument_id`,
         `d`.`amp_org_id` AS `org_id`,
         `d`.`org_grp_id` AS `org_grp_id`,
         `ot`.`amp_org_type_id` AS `org_type_id` 
	from `amp_funding_pledges` `f` 
       join `amp_funding_pledges_details` `fd` 
       join `amp_category_value` `cval` 
       join `amp_currency` `c` 
       join `amp_organisation` `d` 
       join `amp_org_group` `b` 
       join `amp_org_type` `ot` 
       join `amp_category_value` `cval2` 
       join `amp_funding_detail` afd ON afd.`pledge_id` = f.`id` 
	where `cval2`.`id` = fd.`aid_modality` and 
        `c`.`amp_currency_id` = `fd`.`currency` and 
        `f`.`id` = `fd`.`pledge_id` and 
        `cval`.`id` = fd.`type_of_assistance` and 
        `d`.`amp_org_id` = `f`.`amp_org_id` and 
        `d`.`org_grp_id` = `b`.`amp_org_grp_id` and 
        `ot`.`amp_org_type_id` = `b`.`org_type`
	
UNION ALL
	SELECT   `f`.`id` AS `pledge_id`,
         `d`.`name` AS `donor_name`,
         `fd`.`pledge_id` AS `amp_fund_detail_id`,
          7 AS `transaction_type`,
          1 AS `adjustment_type`,
         `fd`.`funding_date` AS `transaction_date`,
         `fd`.`amount` AS `transaction_amount`,
         `c`.`currency_code` AS `currency_code`,
         `cval`.`id` AS `terms_assist_id`,
         `cval`.`category_value` AS `terms_assist_name`,
         `b`.`org_grp_name` AS `org_grp_name`,
         `ot`.`org_type` AS `donor_type_name`,
         `cval2`.`category_value` AS `financing_instrument_name`,
         `cval2`.`id` AS `financing_instrument_id`,
         `d`.`amp_org_id` AS `org_id`,
         `d`.`org_grp_id` AS `org_grp_id`,
         `ot`.`amp_org_type_id` AS `org_type_id`
	from `amp_funding_pledges` `f` 
       join `amp_funding_pledges_details` `fd` 
       join `amp_category_value` `cval` 
       join `amp_currency` `c` 
       join `amp_organisation` `d` 
       join `amp_org_group` `b` 
       join `amp_org_type` `ot` 
       join `amp_category_value` `cval2` 
    where `cval2`.`id` = fd.`aid_modality` and 
        `c`.`amp_currency_id` = `fd`.`currency` and 
        `f`.`id` = `fd`.`pledge_id` and 
        `cval`.`id` = fd.`type_of_assistance` and 
        `d`.`amp_org_id` = `f`.`amp_org_id` and 
        `d`.`org_grp_id` = `b`.`amp_org_grp_id` and 
        `ot`.`amp_org_type_id` = `b`.`org_type`
	order by `pledge_id`;
	