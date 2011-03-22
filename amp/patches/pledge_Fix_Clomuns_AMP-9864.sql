DELETE FROM `amp_columns_order` where columnName ='Pledges Columns';
DELETE FROM `amp_columns_order` where columnName ='Pledge Contact 1';
DELETE FROM `amp_columns_order` where columnName ='Pledge Contact 2';


INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Pledges Columns', max(`indexOrder`) + 1 from `amp_columns_order`;

INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Pledge Contact 1', max(`indexOrder`) + 1 from `amp_columns_order`;

INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Pledge Contact 2', max(`indexOrder`) + 1 from `amp_columns_order`;
