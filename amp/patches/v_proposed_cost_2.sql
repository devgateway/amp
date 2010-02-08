CREATE OR REPLACE VIEW v_proposed_cost AS SELECT amp_activity_id as amp_activity_id, amp_activity_id as object_id, amp_activity_id as object_id2, proj_cost_amount as transaction_amount, proj_cost_currcode as currency_code,proj_cost_date as transaction_date FROM amp_activity where proj_cost_amount is not null;
DELETE FROM amp_report_column WHERE columnId IN (SELECT columnId FROM amp_columns WHERE extractorView='v_proposed_cost');
DELETE FROM amp_columns WHERE extractorView='v_proposed_cost';
INSERT INTO amp_columns (columnName,aliasName,cellType,extractorView) VALUES ('Proposed Project Cost','proposed_cost','org.dgfoundation.amp.ar.cell.CategAmountCell','v_proposed_cost');
INSERT INTO amp_measures (measureName,aliasName,type) values ('Uncommitted Balance','Uncommitted Balance','A');