update amp_measures set allowSplitbyCateg=0;
update amp_measures set allowSplitbyCateg=1 where measureName='Actual Commitments';
update amp_measures set allowSplitbyCateg=1 where measureName='Actual Disbursements';
update amp_measures set allowSplitbyCateg=1 where measureName='Actual Expenditures';
update amp_measures set allowSplitbyCateg=1 where measureName='Planned Commitments';
update amp_measures set allowSplitbyCateg=1 where measureName='Planned Disbursements';
update amp_measures set allowSplitbyCateg=1 where measureName='Planned Expenditures';
update amp_measures set allowSplitbyCateg=1 where measureName='Undisbursed Balance';
update amp_measures set allowSplitbyCateg=1, description='Total Commitments (Depends of global setting configuration)', expression='totalCommitments'  where measureName='Total Commitments';

CREATE OR REPLACE VIEW  v_proposed_cost AS
  select
    amp_activity.amp_activity_id AS amp_activity_id,
    amp_activity.amp_activity_id AS object_id,
    amp_activity.amp_activity_id AS object_id2,
    amp_activity.proj_cost_amount AS transaction_amount,
    amp_activity.proj_cost_currcode AS currency_code,
    amp_activity.proj_cost_date AS transaction_date
  from
    amp_activity
  where
    (amp_activity.proj_cost_amount is not null);