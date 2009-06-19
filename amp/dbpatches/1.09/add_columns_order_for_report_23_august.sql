CREATE TABLE  `amp_columns_order` (
  `id` bigint(20) NOT NULL auto_increment,
  `columnName` varchar(255) default NULL,
  `indexOrder` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `amp_columns_order` VALUES  (1,'Identification',1),
 (2,'Planning',2),
 (3,'Location',3),
 (4,'Funding Organizations',4),
 (5,'Issues',5),
 (6,'Contact Information',6),
 (7,'M & E',7),
 (8,'Costing',8);
