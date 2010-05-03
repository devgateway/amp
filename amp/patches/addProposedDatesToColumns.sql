CREATE OR REPLACE VIEW  `v_actual_proposed_date` AS
  select
    `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
    `amp_activity`.`proposed_approval_date` AS `proposed_approval_date`
  from
    `amp_activity`
  order by
    `amp_activity`.`amp_activity_id`;
    
insert into amp_columns
   	(columnName,cellType,extractorView)
       values ('Proposed Approval Date', 'org.dgfoundation.amp.ar.cell.DateCell','v_actual_proposed_date');


CREATE OR REPLACE VIEW  `v_proposed_completion_date` AS
  select
    `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
    `amp_activity`.`proposed_completion_date` AS `proposed_completion_date`
  from
    `amp_activity`
  order by
    `amp_activity`.`amp_activity_id`;




insert into amp_columns
   	(columnName,cellType,extractorView)
       values ('Proposed Completion Date', 'org.dgfoundation.amp.ar.cell.DateCell','v_proposed_completion_date');