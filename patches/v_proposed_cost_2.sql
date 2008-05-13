CREATE OR REPLACE VIEW v_proposed_cost AS 
SELECT amp_activity_id as amp_activity_id, amp_activity_id as object_id, amp_activity_id as object_id2, proj_cost_amount as transaction_amount, proj_cost_currcode as currency_code,proj_cost_date as transaction_date FROM amp_activity where proj_cost_amount is not null;
UPDATE amp_columns set cellType='org.dgfoundation.amp.ar.cell.CategAmountCell' where columnName='Proposed Project Cost';
INSERT INTO amp_measures (measureId,measureName,aliasName) values (9,'Uncommitted Balance','Uncommitted Balance','A');