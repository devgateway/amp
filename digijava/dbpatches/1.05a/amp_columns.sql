-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.24-standard

--
-- Structure for table `amp_columns`
--

DROP TABLE IF EXISTS `amp_columns`;
CREATE TABLE `amp_columns` (
  `columnId` bigint(20) NOT NULL auto_increment,
  `columnName` varchar(255) default NULL,
  `aliasName` varchar(255) default NULL,
  `cellType` varchar(255) default NULL,
  `extractorView` varchar(255) default NULL,
  PRIMARY KEY  (`columnId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `amp_columns`
--
INSERT INTO `amp_columns` (`columnId`,`columnName`,`aliasName`,`cellType`,`extractorView`) VALUES 
 (1,'Status','statusName','org.dgfoundation.amp.ar.cell.TextCell','v_status'),
 (2,'Donor','donorName','org.dgfoundation.amp.ar.cell.TextCell','v_donors'),
 (3,'Actual Start Date','actualStartDate','org.dgfoundation.amp.ar.cell.DateCell','v_actual_start_date'),
 (4,'Project Title','activityName','org.dgfoundation.amp.ar.cell.TextCell','v_titles'),
 (5,'Type Of Assistance','termAssistName','org.dgfoundation.amp.ar.cell.TextCell','v_terms_assist'),
 (6,'Implementation Level','levelName','org.dgfoundation.amp.ar.cell.TextCell','v_implementation_level'),
 (7,'Actual Completion Date','actualCompletionDate','org.dgfoundation.amp.ar.cell.DateCell','v_actual_completion_date'),
 (8,'Sector',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_sectors'),
 (9,'Region',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_regions'),
 (10,'Financing Instrument',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_financing_instrument'),
 (11,'Objective',NULL,'org.dgfoundation.amp.ar.cell.TextCell','v_objectives');
INSERT INTO `amp_columns` (`columnId`,`columnName`,`aliasName`,`cellType`,`extractorView`) VALUES 
 (12,'Project Id','ampId','org.dgfoundation.amp.ar.cell.TextCell','v_amp_id'),
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
 (23,'Donor Commitment Date','donor_commitment_date','org.dgfoundation.amp.ar.cell.DateCell','v_donor_commitment_date'),
 (24,'Physical Progress','physical_progress','org.dgfoundation.amp.ar.cell.TextCell','v_physical_progress');  
