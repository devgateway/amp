INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledges Programs', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_programs');

CREATE OR REPLACE VIEW v_pledges_programs AS 
select 
    `p`.`id` AS `pledge_id`,
    `t`.`name` AS `name`,
    `fp`.`amp_program_id` AS `amp_program_id`,
    `fp`.`program_percentage` AS `program_percentage` 
  from 
    ((`amp_funding_pledges` `p` join `amp_funding_pledges_program` `fp` on(((`p`.`id` = `fp`.`pledge_id`)))) join `amp_theme` `t` on((`t`.`amp_theme_id` = `fp`.`amp_program_id`))) 
  order by 
    `p`.`id`,`t`.`name`;