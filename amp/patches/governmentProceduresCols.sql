CREATE OR REPLACE SQL SECURITY DEFINER VIEW   `v_yes_no_government_approval_Proc` AS select `a`.`amp_activity_id` AS `amp_activity_id`,if((`a`.`governmentApprovalProcedures` = 1),'yes',if((`a`.`governmentApprovalProcedures` = 0),'no','unallocated')) AS `governmentApprovalProcedures` from `amp_activity` `a` order by `a`.`amp_activity_id`;
CREATE OR REPLACE SQL SECURITY DEFINER VIEW   `v_yes_no_joint_criteria` AS select `a`.`amp_activity_id` AS `amp_activity_id`,if((`a`.`jointCriteria` = 1),'yes',if((`a`.`jointCriteria` = 0),'no','unallocated')) AS `jointCriteria` from `amp_activity` `a` order by `a`.`amp_activity_id`;


insert into amp_columns(columnName, cellType, extractorview) values("Government Approval Procedures", "org.dgfoundation.amp.ar.cell.TrnTextCell","v_yes_no_government_approval_Proc");
insert into amp_columns(columnName, cellType, extractorview) values("Joint Criteria", "org.dgfoundation.amp.ar.cell.TrnTextCell","v_yes_no_joint_criteria");
