CREATE OR REPLACE VIEW v_senegal_cris_budget AS 
  select 
    amp_activity.amp_activity_id AS amp_activity_id,
    amp_activity.cris_number AS cris_number,
    amp_activity.budget_code_project_id AS budget_number
  from 
    amp_activity
  order by 
    amp_activity.amp_activity_id;