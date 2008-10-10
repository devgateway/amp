delete from amp_report_column where columnId IN (select columnId from amp_columns where columnName like ('%Indicator%'));
delete from amp_report_hierarchy where columnId IN (select columnId from amp_columns where columnName like ('%Indicator%'));
delete from amp_columns where columnName like ('%Indicator%');