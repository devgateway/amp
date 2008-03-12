drop table `amp_columns`;
CREATE TABLE `amp_columns`
(
   columnId bigint PRIMARY KEY NOT NULL,
   columnName varchar(255),
   aliasName varchar(255),
   cellType varchar(255),
   extractorView varchar(255)
);
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (1,'Status','statusName','org.dgfoundation.amp.ar.cell.TextCell','v_status');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (2,'Donor Agency','donorAgency','org.dgfoundation.amp.ar.cell.TextCell','v_donors');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (3,'Actual Start Date','actualStartDate','org.dgfoundation.amp.ar.cell.DateCell','v_actual_start_date');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (4,'Project Title','activityName','org.dgfoundation.amp.ar.cell.TextCell','v_titles');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (5,'Type Of Assistance','termAssistName','org.dgfoundation.amp.ar.cell.TextCell','v_terms_assist');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (6,'Implementation Level','levelName','org.dgfoundation.amp.ar.cell.TextCell','v_implementation_level');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (7,'Actual Completion Date','actualCompletionDate','org.dgfoundation.amp.ar.cell.DateCell','v_actual_completion_date');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (8,'Sector',null,'org.dgfoundation.amp.ar.cell.MetaTextCell','v_sectors');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (9,'Region',null,'org.dgfoundation.amp.ar.cell.MetaTextCell','v_regions');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (10,'Financing Instrument',null,'org.dgfoundation.amp.ar.cell.TextCell','v_financing_instrument');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (11,'Objective',null,'org.dgfoundation.amp.ar.cell.TextCell','v_objectives');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (12,'Project Id','ampId','org.dgfoundation.amp.ar.cell.TextCell','v_amp_id');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (13,'Contact Name',null,'org.dgfoundation.amp.ar.cell.TextCell','v_contact_name');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (14,'Description',null,'org.dgfoundation.amp.ar.cell.TextCell','v_description');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (15,'Cumulative Commitment',null,'org.dgfoundation.amp.ar.cell.CummulativeAmountCell',null);
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (16,'Cumulative Disbursement',null,'org.dgfoundation.amp.ar.cell.CummulativeAmountCell',null);
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (17,'Component Name','componentName','org.dgfoundation.amp.ar.cell.TextCell','v_components');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (18,'Team','team','org.dgfoundation.amp.ar.cell.TextCell','v_teams');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (19,'Issues','issues','org.dgfoundation.amp.ar.cell.TextCell','v_issues');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (20,'Measures Taken','measures_taken','org.dgfoundation.amp.ar.cell.TextCell','v_measures_taken');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (21,'Actors','actors','org.dgfoundation.amp.ar.cell.TextCell','v_actors');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (22,'Actual Approval Date','actual_approval_date','org.dgfoundation.amp.ar.cell.DateCell','v_actual_approval_date');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (23,'Donor Commitment Date','donor_commitment_date','org.dgfoundation.amp.ar.cell.DateCell','v_donor_commitment_date');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (24,'Physical Progress','physical_progress','org.dgfoundation.amp.ar.cell.TextCell','v_physical_progress');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (25,'Total Costs','costs','org.dgfoundation.amp.ar.cell.CummulativeAmountCell','v_costs');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (26,'A.C. Chapter','acchapter','org.dgfoundation.amp.ar.cell.TextCell','v_ac_chapters');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (27,'Accession Instrument','accessioninstr','org.dgfoundation.amp.ar.cell.TextCell','v_accession_instruments');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (28,'Costing Donor','costingDonor','org.dgfoundation.amp.ar.cell.TextCell','v_costing_donors');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (29,'Donor Group','donorGroup','org.dgfoundation.amp.ar.cell.TextCell','v_donor_groups');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (30,'Component description','description','org.dgfoundation.amp.ar.cell.TextCell','v_component_description');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (31,'Physical progress title','title','org.dgfoundation.amp.ar.cell.TextCell','v_physical_title');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (32,'Physical progress description','description','org.dgfoundation.amp.ar.cell.TextCell','v_physical_description');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (33,'Indicator Name','indicatorName','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_name');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (34,'Indicator Description','indicatorDescription','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_description');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (35,'Indicator ID','indicatorID','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_id');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (36,'Indicator Current Value','indicatorCurrentValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_actualvalue');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (37,'Indicator Base Value','indicatorBaseValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_basevalue');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (38,'Indicator Target Value','indicatorTargetValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_targetvalue');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (40,'Sub-Sector',null,'org.dgfoundation.amp.ar.cell.TextCell','v_sub_sectors');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (41,'National Planning Objectives','nationalojectives','org.dgfoundation.amp.ar.cell.MetaTextCell','v_nationalojectives');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (42,'Primary Program','primaryprogram','org.dgfoundation.amp.ar.cell.TextCell','v_primaryprogram');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (43,'Secondary Program','secondaryprogram','org.dgfoundation.amp.ar.cell.TextCell','v_secondaryprogram');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (44,'Executing Agency','executing_agency','org.dgfoundation.amp.ar.cell.MetaTextCell','v_executing_agency');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (46,'Implementing Agency','implementing_agency','org.dgfoundation.amp.ar.cell.TextCell','v_implementing_agency');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (47,'Contracting Agency','contracting_agency','org.dgfoundation.amp.ar.cell.TextCell','v_contracting_agency');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (48,'Beneficiary Agency','beneficiary_agency','org.dgfoundation.amp.ar.cell.TextCell','v_beneficiary_agency');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (49,'Sector Group','sector_group','org.dgfoundation.amp.ar.cell.TextCell','v_sector_group');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (50,'Regional Group','regional_group','org.dgfoundation.amp.ar.cell.TextCell','v_regional_Group');
INSERT INTO `amp_columns` (columnId,columnName,aliasName,cellType,extractorView) VALUES (51,'Componente','componente','org.dgfoundation.amp.ar.cell.MetaTextCell','v_componente');