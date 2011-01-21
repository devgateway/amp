update amp_columns set columnName='Cumulative Execution Rate', tokenExpression='cumulativeExecutionRate' where columnName='Execution Rate';
update amp_measures set measureName='Percentage Of Total Disbursements',expression='percentageDisbursements' where measureName='Disbursment Ratio';
update amp_columns  set tokenExpression =null where columnName='Average Disbursement Rate';
insert into amp_measures (measureName,expression,description) values ('Execution Rate','executionRate','Sum Of Actual Disb (Dependent on Filter) / Sum Of Planned Disb (Dependent on Filter) * 100');
delete from amp_fields_templates where field in (select id from amp_fields_visibility  where name='Execution Rate');
delete from amp_fields_visibility where name='Execution Rate';