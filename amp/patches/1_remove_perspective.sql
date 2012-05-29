DELETE fd.*  FROM `amp_funding_detail` fd, `amp_perspective` p WHERE fd.perspective_id = p.amp_perspective_id and p.code != 'MA';
DELETE fd.* FROM `amp_component_funding` fd, `amp_perspective` p WHERE fd.perspective_id = p.amp_perspective_id and p.code != 'MA';
DELETE fd.* FROM `amp_regional_funding` fd, `amp_perspective` p WHERE fd.perspective_id = p.amp_perspective_id and p.code != 'MA';
ALTER TABLE `amp_funding_detail` DROP COLUMN `perspective_id`;
ALTER TABLE `amp_component_funding` DROP COLUMN `perspective_id`;
ALTER TABLE `amp_regional_funding` DROP COLUMN `perspective_id`;
DELETE FROM `amp_global_settings` WHERE possibleValues = 'v_global_settings_perspective';
ALTER TABLE `amp_application_settings` DROP COLUMN `def_perspective`;
ALTER TABLE `amp_application_settings` DROP COLUMN `perspective_id`;
DROP VIEW IF EXISTS  `v_global_settings_perspective`;
CREATE OR REPLACE VIEW v_donor_funding AS
  select `f`.`amp_activity_id` AS `amp_activity_id`,
         `f`.`amp_funding_id` AS `amp_funding_id`,
         `fd`.`amp_fund_detail_id` AS `amp_fund_detail_id`,
         `d`.`name` AS `donor_name`,
         `fd`.`transaction_type` AS `transaction_type`,
         `fd`.`adjustment_type` AS `adjustment_type`,
         `fd`.`transaction_date` AS `transaction_date`,
         `fd`.`transaction_amount` AS `transaction_amount`,
         `c`.`currency_code` AS `currency_code`,
         `cval`.`category_value` AS `terms_assist_name`,
         `fd`.`fixed_exchange_rate` AS `fixed_exchange_rate`,
         `b`.`org_grp_name` AS `org_grp_name`,
         `ot`.`org_type` AS `donor_type_name`,
         `cval2`.`category_value` AS `financing_instrument_name`,
         cval2.`id` as financing_instrument_id
  from `amp_funding` `f`,
       `amp_funding_detail` `fd`,
       `amp_category_value` `cval`,
       `amp_currency` `c`,
       `amp_organisation` `d`,
       `amp_org_group` `b`,
       `amp_org_type` `ot`,
       `amp_category_value` `cval2`
  where `cval2`.`id` = `f`.`financing_instr_category_value` and
        `c`.`amp_currency_id` = `fd`.`amp_currency_id` and
        `f`.`amp_funding_id` = `fd`.`AMP_FUNDING_ID` and
        `cval`.`id` = `f`.`financing_instr_category_value` and
        `d`.`amp_org_id` = `f`.`amp_donor_org_id` and
        `d`.`org_grp_id` = `b`.`amp_org_grp_id` and
        `ot`.`amp_org_type_id` = `d`.`org_type_id`
  order by `f`.`amp_activity_id`;