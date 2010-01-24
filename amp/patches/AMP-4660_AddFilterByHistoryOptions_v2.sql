CREATE TABLE IF NOT EXISTS `amp_activity_access` (
  `id` bigint(20) NOT NULL auto_increment,
  `viewed` bit(1) default NULL,
  `updated` bit(1) default NULL,
  `change_date` datetime NOT NULL,
  `dg_user_id` bigint(20) NOT NULL,
  `amp_activity_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `dg_user_id` (`dg_user_id`),
  KEY `amp_activity_id` (`amp_activity_id`),
  CONSTRAINT FOREIGN KEY (`amp_activity_id`) REFERENCES `amp_activity_version` (`amp_activity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FOREIGN KEY (`dg_user_id`) REFERENCES `dg_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;