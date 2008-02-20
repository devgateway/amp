/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
DROP TABLE IF EXISTS `amp_columns`;
CREATE TABLE `amp_columns` (
  `columnId` bigint(20) NOT NULL auto_increment,
  `columnName` varchar(255) character set utf8 collate utf8_bin default NULL,
  `aliasName` varchar(255) character set utf8 collate utf8_bin default NULL,
  `cellType` varchar(255) character set utf8 collate utf8_bin default NULL,
  `extractorView` varchar(255) character set utf8 collate utf8_bin default NULL,
  PRIMARY KEY  (`columnId`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40000 ALTER TABLE `amp_columns` DISABLE KEYS */;
INSERT INTO `amp_columns` (`columnId`,`columnName`,`aliasName`,`cellType`,`extractorView`) VALUES 
 (1,'Status','statusName','org.dgfoundation.amp.ar.cell.TextCell','v_status'),
 (2,'Donor Agency','donorAgency','org.dgfoundation.amp.ar.cell.TextCell','v_donors'),
 (3,'Actual Start Date','actualStartDate','org.dgfoundation.amp.ar.cell.DateCell','v_actual_start_date'),
 (4,'Project Title','activityName','org.dgfoundation.amp.ar.cell.TextCell','v_titles'),
 (5,'Type Of Assistance','termAssistName','org.dgfoundation.amp.ar.cell.TextCell','v_terms_assist'),
 (6,'Implementation Level','levelName','org.dgfoundation.amp.ar.cell.TextCell','v_implementation_level'),
 (7,'Actual Completion Date','actualCompletionDate','org.dgfoundation.amp.ar.cell.DateCell','v_actual_completion_date'),
 (8,'Sector',NULL,'org.dgfoundation.amp.ar.cell.MetaTextCell','v_sectors'),
 (9,'Region',NULL,'org.dgfoundation.amp.ar.cell.MetaTextCell','v_regions'),
 (10,'Financing Instrument',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_financing_instrument'),
 (11,'Objective',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_objectives');
INSERT INTO `amp_columns` (`columnId`,`columnName`,`aliasName`,`cellType`,`extractorView`) VALUES 
 (12,'AMP ID','ampId','org.dgfoundation.amp.ar.cell.TextCell','v_amp_id'),
 (13,'Contact Name',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_contact_name'),
 (14,'Description',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_description'),
 (15,'Cumulative Commitment',NULL,'org.dgfoundation.amp.ar.cell.CummulativeAmountCell',NULL),
 (16,'Cumulative Disbursement',NULL,'org.dgfoundation.amp.ar.cell.CummulativeAmountCell',NULL),
 (17,'Component Name','componentName','org.dgfoundation.amp.ar.cell.TextCell','v_components'),
 (18,'Team','team','org.dgfoundation.amp.ar.cell.TextCell','v_teams'),
 (19,'Issues','issues','org.dgfoundation.amp.ar.cell.TextCell','v_issues'),
 (20,'Measures Taken','measures_taken','org.dgfoundation.amp.ar.cell.TextCell','v_measures_taken'),
 (21,'Actors','actors','org.dgfoundation.amp.ar.cell.TextCell','v_actors'),
 (22,'Actual Approval Date','actual_approval_date','org.dgfoundation.amp.ar.cell.DateCell','v_actual_approval_date'),
 (23,'Donor Commitment Date','donor_commitment_date','org.dgfoundation.amp.ar.cell.DateCell','v_donor_commitment_date');
INSERT INTO `amp_columns` (`columnId`,`columnName`,`aliasName`,`cellType`,`extractorView`) VALUES 
 (24,'Physical Progress','physical_progress','org.dgfoundation.amp.ar.cell.TextCell','v_physical_progress'),
 (25,'Total Costs','costs','org.dgfoundation.amp.ar.cell.CummulativeAmountCell','v_costs'),
 (26,'A.C. Chapter','acchapter','org.dgfoundation.amp.ar.cell.TextCell','v_ac_chapters'),
 (27,'Accession Instrument','accessioninstr','org.dgfoundation.amp.ar.cell.TextCell','v_accession_instruments'),
 (28,'Costing Donor','costingDonor','org.dgfoundation.amp.ar.cell.TextCell','v_costing_donors'),
 (29,'Donor Group','donorGroup','org.dgfoundation.amp.ar.cell.TextCell','v_donor_groups'),
 (30,'Component description','description','org.dgfoundation.amp.ar.cell.TextCell','v_component_description'),
 (31,'Physical progress title','title','org.dgfoundation.amp.ar.cell.TextCell','v_physical_title'),
 (32,'Physical progress description','description','org.dgfoundation.amp.ar.cell.TextCell','v_physical_description'),
 (33,'Indicator Name','indicatorName','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_name');
INSERT INTO `amp_columns` (`columnId`,`columnName`,`aliasName`,`cellType`,`extractorView`) VALUES 
 (34,'Indicator Description','indicatorDescription','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_description'),
 (35,'Indicator ID','indicatorID','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_id'),
 (36,'Indicator Current Value','indicatorCurrentValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_actualvalue'),
 (37,'Indicator Base Value','indicatorBaseValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_basevalue'),
 (38,'Indicator Target Value','indicatorTargetValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_targetvalue'),
 (40,'Sub-Sector',NULL,'org.dgfoundation.amp.ar.cell.MetaTextCell','v_sub_sectors'),
 (41,'National Planning Objectives','nationalojectives','org.dgfoundation.amp.ar.cell.MetaTextCell','v_nationalojectives'),
 (42,'Primary Program','primaryprogram','org.dgfoundation.amp.ar.cell.TextCell','v_primaryprogram'),
 (43,'Secondary Program','secondaryprogram','org.dgfoundation.amp.ar.cell.TextCell','v_secondaryprogram'),
 (44,'Executing Agency','executing_agency','org.dgfoundation.amp.ar.cell.TextCell','v_executing_agency');
INSERT INTO `amp_columns` (`columnId`,`columnName`,`aliasName`,`cellType`,`extractorView`) VALUES 
 (46,'Implementing Agency','implementing_agency','org.dgfoundation.amp.ar.cell.TextCell','v_implementing_agency'),
 (47,'Contracting Agency','contracting_agency','org.dgfoundation.amp.ar.cell.TextCell','v_contracting_agency'),
 (48,'Beneficiary Agency','beneficiary_agency','org.dgfoundation.amp.ar.cell.TextCell','v_beneficiary_agency'),
 (49,'Sector Group','sector_group','org.dgfoundation.amp.ar.cell.TextCell','v_sector_group'),
 (50,'Regional Group','regional_group','org.dgfoundation.amp.ar.cell.TextCell','v_sector_group'),
 (51,'Componente','componente','org.dgfoundation.amp.ar.cell.MetaTextCell','v_componente'),
 (52,'Project Id','projectId','org.dgfoundation.amp.ar.cell.TextCell','v_project_id');
/*!40000 ALTER TABLE `amp_columns` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
