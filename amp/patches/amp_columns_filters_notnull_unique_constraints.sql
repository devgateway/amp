DELETE FROM amp_columns_filters WHERE column_id IS NULL;
CREATE TABLE amp_columns_filters_temp SELECT * FROM `amp_columns_filters` GROUP BY column_id,bean_field_name,view_field_name HAVING Count( * ) >=1;
TRUNCATE TABLE amp_columns_filters;
INSERT INTO amp_columns_filters (SELECT * from amp_columns_filters_temp);
DROP TABLE amp_columns_filters_temp;
ALTER TABLE amp_columns_filters MODIFY column_id bigint(20) NOT NULL;
ALTER TABLE amp_columns_filters ADD UNIQUE (column_id,bean_field_name,view_field_name);