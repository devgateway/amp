CREATE TABLE amp_columns_order_temp SELECT * FROM `amp_columns_order` GROUP BY `columnName` HAVING Count( * ) >=1;
TRUNCATE TABLE amp_columns_order;
INSERT INTO `amp_columns_order` (SELECT * FROM `amp_columns_order_temp`);
DROP TABLE amp_columns_order_temp;
ALTER TABLE `amp_columns_order` ADD UNIQUE (`columnName`);