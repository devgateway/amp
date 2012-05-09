delete from amp_report_column  where columnId in (select x.`columnId` from amp_columns x where x.columnName='Uncommitted Cumulative Balance' or x.columnName='Undisbursed Cumulative Balance');
delete from  amp_columns where  columnName='Uncommitted Cumulative Balance' or columnName='Undisbursed Cumulative Balance';
update amp_measures set expression='uncommitedCumulativeBalance',  description='Proposed project cost - Cumulative commitments' where measureName='Uncommitted Balance';
update amp_measures set expression='undisbursedCumulativeBalance', description='Cumulative Commitment - Cumulative Disbursement' where measureName='Undisbursed Balance';

