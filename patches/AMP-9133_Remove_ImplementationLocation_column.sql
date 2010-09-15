DELETE FROM `amp_columns_filters` WHERE column_id=(SELECT columnId FROM amp_columns WHERE columnName = 'Implementation Location');

DELETE FROM `amp_report_hierarchy` WHERE columnId=(SELECT columnId FROM amp_columns WHERE columnName = 'Implementation Location');

DELETE FROM `amp_report_column` WHERE columnId=(SELECT columnId FROM amp_columns WHERE columnName = 'Implementation Location');

DELETE FROM amp_columns WHERE columnName = 'Implementation Location';

