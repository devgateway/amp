insert INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Procurement System', b'0', b'0', 'procurement_system');

insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Use donor's own system",acc.id,0 FROM amp_category_class acc WHERE acc.keyName='procurement_system'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Use Nepalese system",acc.id,1 FROM amp_category_class acc WHERE acc.keyName='procurement_system'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Rely on other donor's system",acc.id,2 FROM amp_category_class acc WHERE acc.keyName='procurement_system'); 		


insert INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Reporting System', b'0', b'0', 'reporting_system');

insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Use donor's own system",acc.id,0 FROM amp_category_class acc WHERE acc.keyName='reporting_system'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Use Nepalese system",acc.id,1 FROM amp_category_class acc WHERE acc.keyName='reporting_system'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Rely on other donor's system",acc.id,2 FROM amp_category_class acc WHERE acc.keyName='reporting_system'); 		


insert INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Audit System', b'0', b'0', 'audit_system');

insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Use donor's own system",acc.id,0 FROM amp_category_class acc WHERE acc.keyName='audit_system'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Use Nepalese system",acc.id,1 FROM amp_category_class acc WHERE acc.keyName='audit_system'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Rely on other donor's system",acc.id,2 FROM amp_category_class acc WHERE acc.keyName='audit_system'); 		


insert INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName) 
	VALUES('Institutions', b'0', b'0', 'institutions');

insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Use donor's own structure",acc.id,0 FROM amp_category_class acc WHERE acc.keyName='institutions'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Create new parallel institution",acc.id,1 FROM amp_category_class acc WHERE acc.keyName='institutions'); 
	
insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Use existing Nepalese institutions",acc.id,2 FROM amp_category_class acc WHERE acc.keyName='institutions'); 		

insert INTO amp_category_value(category_value, amp_category_class_id, index_column ) 
	(SELECT "Rely on other donor's institutions",acc.id,3 FROM amp_category_class acc WHERE acc.keyName='institutions'); 		


INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView)  
VALUES('Procurement System', 'procurement_system', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_procurement_system'); 

INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView)  
VALUES('Reporting System', 'reporting_system', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_reporting_system'); 

INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView)  
VALUES('Audit System', 'audit_system', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_audit_system'); 

INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView)  
VALUES('Institutions', 'institutions', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_institutions'); 


CREATE OR REPLACE VIEW `v_procurement_System` AS 
select 
    `acc`.`amp_activity_id` AS `amp_activity_id`,
    `acv`.`category_value` AS `name` 
  from 
    ((`amp_activities_categoryvalues` `acc` join `amp_category_value` `acv`) join `amp_category_class` `ac`) 
  where 
    ((`acv`.`id` = `acc`.`amp_categoryvalue_id`) and (`ac`.`id` = `acv`.`amp_category_class_id`) and (`ac`.`keyName` = _utf8'procurement_system'));


CREATE OR REPLACE VIEW `v_reporting_System` AS 
select 
    `acc`.`amp_activity_id` AS `amp_activity_id`,
    `acv`.`category_value` AS `name` 
  from 
    ((`amp_activities_categoryvalues` `acc` join `amp_category_value` `acv`) join `amp_category_class` `ac`) 
  where 
    ((`acv`.`id` = `acc`.`amp_categoryvalue_id`) and (`ac`.`id` = `acv`.`amp_category_class_id`) and (`ac`.`keyName` = _utf8'reporting_system'));


CREATE OR REPLACE VIEW `v_audit_system` AS 
select 
    `acc`.`amp_activity_id` AS `amp_activity_id`,
    `acv`.`category_value` AS `name` 
  from 
    ((`amp_activities_categoryvalues` `acc` join `amp_category_value` `acv`) join `amp_category_class` `ac`) 
  where 
    ((`acv`.`id` = `acc`.`amp_categoryvalue_id`) and (`ac`.`id` = `acv`.`amp_category_class_id`) and (`ac`.`keyName` = _utf8'audit_system'));


CREATE OR REPLACE VIEW `v_institutions` AS 
select 
    `acc`.`amp_activity_id` AS `amp_activity_id`,
    `acv`.`category_value` AS `name` 
  from 
    ((`amp_activities_categoryvalues` `acc` join `amp_category_value` `acv`) join `amp_category_class` `ac`) 
  where 
    ((`acv`.`id` = `acc`.`amp_categoryvalue_id`) and (`ac`.`id` = `acv`.`amp_category_class_id`) and (`ac`.`keyName` = _utf8'institutions'));

