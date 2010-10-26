CREATE OR REPLACE VIEW v_proposed_start_date AS select amp_activity.amp_activity_id AS amp_activity_id,date_format(amp_activity.proposed_start_date,'%Y-%m-%d') AS proposed_start_date from amp_activity 
order by amp_activity.amp_activity_id;

INSERT INTO amp_columns (columnName,aliasName,cellType,extractorView) VALUES 
 ('Proposed Start Date','proposedStartDate','org.dgfoundation.amp.ar.cell.DateCell','v_proposed_start_date');

CREATE OR REPLACE VIEW v_disbursements_date AS select amp_activity.amp_activity_id AS amp_activity_id,date_format(amp_activity.disbursments_date,'%Y-%m-%d') AS disbursments_date from amp_activity 
order by amp_activity.amp_activity_id;

INSERT INTO amp_columns (columnName,aliasName,cellType,extractorView) VALUES 
 ('Final Date for Disbursements','disbursementsDate','org.dgfoundation.amp.ar.cell.DateCell','v_disbursements_date');

CREATE OR REPLACE VIEW v_contracting_date AS select amp_activity.amp_activity_id AS amp_activity_id,date_format(amp_activity.contracting_date,'%Y-%m-%d') AS contracting_date from amp_activity 
order by amp_activity.amp_activity_id;

INSERT INTO amp_columns (columnName,aliasName,cellType,extractorView) VALUES 
 ('Final Date for Contracting','contractingDate','org.dgfoundation.amp.ar.cell.DateCell','v_contracting_date');
 
CREATE OR REPLACE VIEW v_disbursements_date_comments as select amp_activity_id as amp_activity_id, comment_ from amp_comments WHERE amp_field_id=12;
CREATE OR REPLACE VIEW v_actual_completion_date_comments as select amp_activity_id as amp_activity_id, comment_ from amp_comments WHERE amp_field_id=1;

INSERT INTO amp_columns (columnName,aliasName,cellType,extractorView) VALUES 
 ('Final Date for Disbursements Comments','disbursementsDateComments','org.dgfoundation.amp.ar.cell.TextCell','v_disbursements_date_comments');

INSERT INTO amp_columns (columnName,aliasName,cellType,extractorView) VALUES 
 ('Current Completion Date Comments','currentCompletionDateComments','org.dgfoundation.amp.ar.cell.TextCell','v_actual_completion_date_comments');
 