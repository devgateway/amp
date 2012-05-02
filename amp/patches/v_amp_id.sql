CREATE OR REPLACE VIEW `v_amp_id` AS
  select
    `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
    `amp_activity`.`amp_id` AS `amp_id`
  from
    `amp_activity`;