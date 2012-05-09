DROP TABLE IF EXISTS temp_table;

CREATE TABLE temp_table SELECT * FROM `amp_columns` GROUP BY `columnName` HAVING Count( * ) >=1;

DROP TABLE IF EXISTS temp_table2;
CREATE TABLE temp_table2 LIKE `amp_columns_filters`;
INSERT INTO `temp_table2` (SELECT colTable.id, temp.columnId AS column_id, colTable.bean_field_name, colTable.view_field_name FROM `temp_table` temp JOIN
(SELECT id, column_id, bean_field_name, view_field_name, columnName FROM `amp_columns` col JOIN `amp_columns_filters` colFil 
WHERE colFil.`column_id` = col.`columnId`) AS colTable 
WHERE `temp`.`columnName` = colTable.columnName);

DELETE FROM `amp_columns_filters`;

INSERT INTO `amp_columns_filters` (SELECT * FROM `temp_table2`);

DROP TABLE IF EXISTS temp_table2;
CREATE TABLE temp_table2 LIKE `amp_report_hierarchy`;
INSERT INTO `temp_table2`(amp_report_id,columnId,cv_level_id,levelId) (SELECT colTable.amp_report_id, temp.columnId AS columnId, colTable.cv_level_id, colTable.levelId FROM `temp_table` temp JOIN
(SELECT columnName, colRep.*  FROM `amp_columns` col JOIN `amp_report_hierarchy` colRep 
WHERE colRep.`columnId` = col.`columnId`) AS colTable 
WHERE `temp`.`columnName` = colTable.columnName);

DELETE FROM `amp_report_hierarchy`;

INSERT INTO `amp_report_hierarchy` (SELECT * FROM `temp_table2`);

DROP TABLE IF EXISTS temp_table2;
CREATE TABLE temp_table2 LIKE `amp_report_column`;
INSERT INTO `temp_table2`(amp_report_id,columnId,cv_level_id,order_id) (SELECT colTable.amp_report_id, temp.columnId AS columnId, colTable.cv_level_id, colTable.order_id FROM `temp_table` temp JOIN
(SELECT columnName, colRep.*  FROM `amp_columns` col JOIN `amp_report_column` colRep 
WHERE colRep.`columnId` = col.`columnId`) AS colTable 
WHERE `temp`.`columnName` = colTable.columnName);

DELETE FROM `amp_report_column`;

INSERT INTO `amp_report_column` (SELECT * FROM `temp_table2`);

DELETE FROM `amp_columns` WHERE columnId NOT IN (SELECT columnId FROM `temp_table`);

ALTER TABLE `amp_columns` ADD UNIQUE (`columnName`);

DROP TABLE IF EXISTS temp_table;
DROP TABLE IF EXISTS temp_table2;
