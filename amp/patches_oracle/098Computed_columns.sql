INSERT INTO amp_columns (columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, tokenExpression, filterRetrievable) VALUES
 ('Overage Project', NULL, 'org.dgfoundation.amp.ar.cell.ComputedDateCell', 'v_computed_dates', NULL, 'overageProjects', NULL);
 
 INSERT INTO amp_columns (columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, tokenExpression, filterRetrievable) VALUES
  ('Age of Project', NULL, 'org.dgfoundation.amp.ar.cell.ComputedDateCell', 'v_computed_dates', NULL, 'ageOfProject', NULL);
  
  INSERT INTO amp_columns (columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, tokenExpression, filterRetrievable) VALUES
  ('Predictability of Funding', NULL, 'org.dgfoundation.amp.ar.cell.ComputedAmountCell', NULL, NULL, 'predictabilityOfFunding', NULL);
  
  INSERT INTO amp_columns (columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, tokenExpression, filterRetrievable) VALUES
  ('Average Size of Projects', NULL, 'org.dgfoundation.amp.ar.cell.ComputedCountingAmountCell', NULL, NULL, 'averageSizeofProjects', NULL);
  
  INSERT INTO amp_columns (columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, tokenExpression, filterRetrievable) VALUES
  ('Variance Of Commitments', NULL, 'org.dgfoundation.amp.ar.cell.ComputedCountingAmountCell', NULL, NULL, 'actualCommitmentsVariance', NULL);
  INSERT INTO amp_columns (columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, tokenExpression, filterRetrievable) VALUES
  ('Variance Of Disbursements', NULL, 'org.dgfoundation.amp.ar.cell.ComputedCountingAmountCell', NULL, NULL, 'actualDisbursmentVariance', NULL);
  
INSERT INTO amp_columns_order (columnName, indexOrder) VALUES ('Computed Fields', 24);

CREATE OR REPLACE VIEW v_computed_dates AS
  select
    amp_activity.amp_activity_id AS amp_activity_id,
    amp_activity.activity_close_date AS activity_close_date,
    amp_activity.actual_start_date AS actual_start_date,
    amp_activity.actual_approval_date AS actual_approval_date,
    amp_activity.activity_approval_date AS activity_approval_date,
    amp_activity.proposed_start_date AS proposed_start_date,
    amp_activity.actual_completion_date AS actual_completion_date,
    amp_activity.proposed_completion_date AS proposed_completion_date
  from
    amp_activity
  order by
    amp_activity.amp_activity_id;
    