CREATE OR REPLACE VIEW v_pledges_date_hierarchy as
     select
          `fd`.`year` AS `year`,
          `pl`.`id` AS `pledgeid`,
          `fd`.`id` AS `amp_fund_detail_id`
   from `amp_funding_pledges_details` `fd`
        join `amp_funding_pledges` `pl` on `fd`.`pledge_id` = `pl`.`id`
   where
        `pl`.`id` = `fd`.`pledge_id` and fd.`year` IS NOT NULL;
   