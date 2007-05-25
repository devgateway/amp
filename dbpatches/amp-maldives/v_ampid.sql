-- Aid Management Platform 
-- (C) 2007 Development Gateway Foundation
-- Maldives Implementation (1.07) RC2


CREATE OR REPLACE VIEW  `v_ampid` AS 
select `amp_activity`.`amp_activity_id` AS `amp_activity_id`,`amp_activity`.`amp_id` AS `amp_id` from `amp_activity` 
order by `amp_activity`.`amp_activity_id`;