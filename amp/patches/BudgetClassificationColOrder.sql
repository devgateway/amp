INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Budget Sector', max(`indexOrder`) + 1 from `amp_columns_order`;

INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Budget Organization', max(`indexOrder`) + 1 from `amp_columns_order`;

INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Budget Department', max(`indexOrder`) + 1 from `amp_columns_order`;

INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Budget Program', max(`indexOrder`) + 1 from `amp_columns_order`;