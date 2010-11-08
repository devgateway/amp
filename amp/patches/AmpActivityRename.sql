SET FOREIGN_KEY_CHECKS=0;
drop table amp_activity_version;
SET FOREIGN_KEY_CHECKS=1;

RENAME TABLE amp_activity TO amp_activity_version;

CREATE VIEW `amp_activity` AS  select  amp_activity_version.*  from    (`amp_activity_version` join `amp_activity_group`)  where    (`amp_activity_version`.`amp_activity_id` = `amp_activity_group`.`amp_activity_last_version_id`);

insert into `amp_activity_group` (`amp_activity_last_version_id`) select `amp_activity_version`.`amp_activity_id` from `amp_activity_version`;
update amp_activity_version set `amp_activity_version`.`amp_activity_group_id` = ( select `amp_activity_group`.`amp_activity_group_id` from `amp_activity_group` where `amp_activity_group`.`amp_activity_last_version_id` = `amp_activity_version`.`amp_activity_id`);