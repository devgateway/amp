DROP TABLE  IF EXISTS tmp_location_table;

CREATE TEMPORARY TABLE tmp_location_table select * from amp_activity_location;

DROP TABLE amp_activity_location;

CREATE TABLE `amp_activity_location` (
  `amp_activity_id` bigint(20) NOT NULL default '0',
  `amp_location_id` bigint(20) NOT NULL default '0',
  `amp_activity_location_id` bigint(20) NOT NULL auto_increment,
  `location_percentage` float default NULL,
  PRIMARY KEY  (`amp_activity_location_id`),
  KEY `FKF702D92A8CBD0BEA` (`amp_location_id`),
  KEY `FKF702D92A3524C250` (`amp_activity_id`),
  CONSTRAINT `FK3E0B250A3524C250` FOREIGN KEY (`amp_activity_id`) REFERENCES `amp_activity` (`amp_activity_id`),
  CONSTRAINT `FK3E0B250A8CBD0BEA` FOREIGN KEY (`amp_location_id`) REFERENCES `amp_location` (`amp_location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;


INSERT INTO amp_activity_location (`amp_activity_id`,`amp_location_id`,`amp_activity_location_id`)
select 	`amp_activity_id`,`amp_location_id`,`amp_activity_location_id` from tmp_location_table;