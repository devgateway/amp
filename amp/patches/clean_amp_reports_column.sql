DELETE FROM `amp_report_column` WHERE columnId NOT IN (SELECT columnid FROM `amp_columns`);


