CREATE OR REPLACE VIEW v_activity_creator AS  
SELECT a.amp_activity_id as amp_activity_id, u.email as name, atm.user as user_id 
FROM amp_activity a, amp_team_member atm, dg_user u 
WHERE atm.amp_team_mem_id=a.activity_creator AND atm.user=u.id ORDER BY amp_activity_id; 


INSERT INTO amp_columns(columnName, cellType, extractorView)  
VALUES('Activity Creator', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_activity_creator'); 

CREATE OR REPLACE VIEW v_activity_changed_by AS  
SELECT a.amp_activity_id as amp_activity_id, u.email as name, atm.user as user_id 
FROM amp_activity a, amp_team_member atm, dg_user u 
WHERE atm.amp_team_mem_id=a.updated_by AND atm.user=u.id ORDER BY amp_activity_id; 

INSERT INTO amp_columns(columnName, cellType, extractorView) 
VALUES('Last changed by', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_activity_changed_by'); 

CREATE OR REPLACE VIEW v_creation_date AS  
SELECT a.amp_activity_id as amp_activity_id, a.date_created as creation_date 
FROM amp_activity a ORDER BY amp_activity_id; 

CREATE OR REPLACE VIEW v_changed_date AS  
SELECT a.amp_activity_id as amp_activity_id, a.date_updated as changed_date 
FROM amp_activity a ORDER BY amp_activity_id; 

INSERT INTO amp_columns(columnName, cellType, extractorView) 
VALUES('Creation Date', 'org.dgfoundation.amp.ar.cell.DateCell', 'v_creation_date'); 

INSERT INTO amp_columns(columnName, cellType, extractorView) 
VALUES('Changed Date', 'org.dgfoundation.amp.ar.cell.DateCell', 'v_changed_date'); 

