CREATE TABLE IF NOT EXISTS `amp_category_values_used` (
  `value_id` bigint(20) NOT NULL,
  `used_value_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`value_id`,`used_value_id`),
  KEY `FKE572BAB46F22D5E1` (`used_value_id`),
  KEY `FKE572BAB4C823AB9F` (`value_id`),
  CONSTRAINT `FKE572BAB46F22D5E1` FOREIGN KEY (`used_value_id`) REFERENCES `amp_category_value` (`id`),
  CONSTRAINT `FKE572BAB4C823AB9F` FOREIGN KEY (`value_id`) REFERENCES `amp_category_value` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;