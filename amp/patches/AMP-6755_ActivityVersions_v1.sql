CREATE TABLE `amp_activity_group` (
  `amp_activity_group_id` bigint(20) NOT NULL auto_increment,
  `amp_activity_last_version_id` bigint(20) default NULL,
  PRIMARY KEY  (`amp_activity_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE amp_activity ADD COLUMN (amp_activity_group_id BIGINT(20));
ALTER TABLE amp_activity ADD COLUMN (amp_activity_previous_version_id BIGINT(20));
ALTER TABLE amp_activity ADD COLUMN (modified_date DATETIME);
ALTER TABLE amp_activity ADD COLUMN (modified_by BIGINT(20));
ALTER TABLE amp_activity ADD INDEX `amp_activity_group_id` (`amp_activity_group_id`);
ALTER TABLE amp_activity ADD CONSTRAINT FOREIGN KEY (`amp_activity_group_id`) REFERENCES `amp_activity_group` (`amp_activity_group_id`);
ALTER TABLE amp_activity ADD INDEX `amp_activity_previous_version_id` (`amp_activity_previous_version_id`);
ALTER TABLE amp_activity ADD CONSTRAINT FOREIGN KEY (`amp_activity_previous_version_id`) REFERENCES `amp_activity` (`amp_activity_id`);
ALTER TABLE amp_activity ADD INDEX `modified_by` (`modified_by`);
ALTER TABLE amp_activity ADD CONSTRAINT FOREIGN KEY (`modified_by`) REFERENCES `amp_team_member` (`amp_team_mem_id`);

ALTER TABLE amp_activity_group ADD INDEX `amp_activity_last_version_id` (`amp_activity_last_version_id`);
ALTER TABLE amp_activity_group ADD CONSTRAINT FOREIGN KEY (`amp_activity_last_version_id`) REFERENCES `amp_activity` (`amp_activity_id`);