-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.45-Debian_1ubuntu3-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;



--
-- Definition of table `amp_columns`
--

DROP TABLE IF EXISTS `amp_columns`;
CREATE TABLE  `amp_columns` (
  `columnId` bigint(20) NOT NULL auto_increment,
  `columnName` varchar(255) character set utf8 collate utf8_bin default NULL,
  `aliasName` varchar(255) character set utf8 collate utf8_bin default NULL,
  `cellType` varchar(255) character set utf8 collate utf8_bin default NULL,
  `extractorView` varchar(255) character set utf8 collate utf8_bin default NULL,
  PRIMARY KEY  (`columnId`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `amp_columns`
--

/*!40000 ALTER TABLE `amp_columns` DISABLE KEYS */;
LOCK TABLES `amp_columns` WRITE;
INSERT INTO `amp_columns` VALUES  (1,'Status','statusName','org.dgfoundation.amp.ar.cell.TextCell','v_status'),
 (2,'Donor Agency','donorAgency','org.dgfoundation.amp.ar.cell.TextCell','v_donors'),
 (3,'Actual Start Date','actualStartDate','org.dgfoundation.amp.ar.cell.DateCell','v_actual_start_date'),
 (4,'Project Title','activityName','org.dgfoundation.amp.ar.cell.TextCell','v_titles'),
 (5,'Type Of Assistance','termAssistName','org.dgfoundation.amp.ar.cell.TextCell','v_terms_assist'),
 (6,'Implementation Level','levelName','org.dgfoundation.amp.ar.cell.TextCell','v_implementation_level'),
 (7,'Actual Completion Date','actualCompletionDate','org.dgfoundation.amp.ar.cell.DateCell','v_actual_completion_date'),
 (8,'Sector',NULL,'org.dgfoundation.amp.ar.cell.MetaTextCell','v_sectors'),
 (9,'Region',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_regions'),
 (10,'Financing Instrument',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_financing_instrument'),
 (11,'Objective',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_objectives'),
 (12,'Project Id','ampId','org.dgfoundation.amp.ar.cell.TextCell','v_amp_id');
INSERT INTO `amp_columns` VALUES  (13,'Contact Name',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_contact_name'),
 (14,'Description',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_description'),
 (15,'Cumulative Commitment',NULL,'org.dgfoundation.amp.ar.cell.CummulativeAmountCell',NULL),
 (16,'Cumulative Disbursement',NULL,'org.dgfoundation.amp.ar.cell.CummulativeAmountCell',NULL),
 (17,'Component Name','componentName','org.dgfoundation.amp.ar.cell.TextCell','v_components'),
 (18,'Team','team','org.dgfoundation.amp.ar.cell.TextCell','v_teams'),
 (19,'Issues','issues','org.dgfoundation.amp.ar.cell.TextCell','v_issues'),
 (20,'Measures Taken','measures_taken','org.dgfoundation.amp.ar.cell.TextCell','v_measures_taken'),
 (21,'Actors','actors','org.dgfoundation.amp.ar.cell.TextCell','v_actors'),
 (22,'Actual Approval Date','actual_approval_date','org.dgfoundation.amp.ar.cell.DateCell','v_actual_approval_date'),
 (23,'Donor Commitment Date','donor_commitment_date','org.dgfoundation.amp.ar.cell.DateCell','v_donor_commitment_date'),
 (24,'Physical Progress','physical_progress','org.dgfoundation.amp.ar.cell.TextCell','v_physical_progress');
INSERT INTO `amp_columns` VALUES  (25,'Total Costs','costs','org.dgfoundation.amp.ar.cell.CummulativeAmountCell','v_costs'),
 (26,'A.C. Chapter','acchapter','org.dgfoundation.amp.ar.cell.TextCell','v_ac_chapters'),
 (27,'Accession Instrument','accessioninstr','org.dgfoundation.amp.ar.cell.TextCell','v_accession_instruments'),
 (28,'Costing Donor','costingDonor','org.dgfoundation.amp.ar.cell.TextCell','v_costing_donors'),
 (29,'Donor Group','donorGroup','org.dgfoundation.amp.ar.cell.TextCell','v_donor_groups'),
 (30,'Component description','description','org.dgfoundation.amp.ar.cell.TextCell','v_component_description'),
 (31,'Physical progress title','title','org.dgfoundation.amp.ar.cell.TextCell','v_physical_title'),
 (32,'Physical progress description','description','org.dgfoundation.amp.ar.cell.TextCell','v_physical_description'),
 (33,'Indicator Name','indicatorName','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_name'),
 (34,'Indicator Description','indicatorDescription','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_description'),
 (35,'Indicator ID','indicatorID','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_id');
INSERT INTO `amp_columns` VALUES  (36,'Indicator Current Value','indicatorCurrentValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_actualvalue'),
 (37,'Indicator Base Value','indicatorBaseValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_basevalue'),
 (38,'Indicator Target Value','indicatorTargetValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_targetvalue'),
 (40,'Sub-Sector',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_sub_sectors'),
 (41,'National Planning Objectives','nationalojectives','org.dgfoundation.amp.ar.cell.TextCell','v_nationalojectives'),
 (42,'Primary Program','primaryprogram','org.dgfoundation.amp.ar.cell.TextCell','v_primaryprogram'),
 (43,'Secondary Program','secondaryprogram','org.dgfoundation.amp.ar.cell.TextCell','v_secondaryprogram');
UNLOCK TABLES;
/*!40000 ALTER TABLE `amp_columns` ENABLE KEYS */;


--
-- Definition of table `amp_columns_order`
--

DROP TABLE IF EXISTS `amp_columns_order`;
CREATE TABLE  `amp_columns_order` (
  `id` bigint(20) NOT NULL auto_increment,
  `columnName` varchar(255) default NULL,
  `indexOrder` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `amp_columns_order`
--

/*!40000 ALTER TABLE `amp_columns_order` DISABLE KEYS */;
LOCK TABLES `amp_columns_order` WRITE;
INSERT INTO `amp_columns_order` VALUES  (1,'Identification',1),
 (2,'Planning',2),
 (3,'Location',3),
 (4,'Funding Organizations',4),
 (5,'Issues',5),
 (6,'Reports Contact Information',6),
 (7,'Activity',7),
 (8,'Costing',8),
 (9,'Program',9),
 (10,'Sectors',10),
 (11,'Components',11),
 (12,'Physical Progress',12);
UNLOCK TABLES;
/*!40000 ALTER TABLE `amp_columns_order` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
