CREATE OR REPLACE VIEW v_pledges_date_hierarchy as
  select
    year(`fd`.`funding_date`) AS `year`,
    month(`fd`.`funding_date`) AS `month`,
    cast(monthname(`fd`.`funding_date`) as char charset utf8) AS `month_name`,
    quarter(`fd`.`funding_date`) AS `quarter`,
    concat(_utf8'Q',
    cast(quarter(`fd`.`funding_date`) as char charset utf8)) AS `quarter_name`,
    pl.id as pledgeid,
    fd.id as amp_fund_detail_id
 from `amp_funding_pledges_details` fd JOIN `amp_funding_pledges` pl ON fd.`pledge_id` = pl.`id`
  where pl.`id` = fd.`pledge_id`;