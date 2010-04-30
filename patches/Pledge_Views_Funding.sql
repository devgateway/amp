CREATE OR REPLACE VIEW   v_pledges_funding_st  as
 SELECT   `f`.`id` AS `pledge_id`,
         `d`.`name` AS `donor_name`,
         `fd`.`pledge_id` AS `amp_fund_detail_id`,
          CASE afd.`transaction_type` WHEN 0 THEN 5 WHEN 1 THEN 6 END
          AS `transaction_type`,
          1 AS `adjustment_type`,
         `fd`.`funding_date` AS `transaction_date`,
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
        `ot`.`amp_org_type_id` = `d`.`org_type_id`
  order by `f`.`id`;

 

