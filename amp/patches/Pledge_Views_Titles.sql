CREATE OR REPLACE VIEW v_pledges_titles as
     select
    `amp_funding_pledges`.`id` AS `pledge_id`,
    `amp_funding_pledges`.`title` AS `title`,
    `amp_funding_pledges`.`id` AS `title_id`
  from
    `amp_funding_pledges`
  order by
    `amp_funding_pledges`.`id`;


  
