UPDATE `amp_columns` SET columnName = 'Proposed Project Amount'
WHERE columnName ='Proposed Project Cost';

INSERT INTO `amp_columns_order`(`columnName`, `indexOrder`)
SELECT 'Proposed Project Cost', max(`indexOrder`) + 1 from `amp_columns_order`;



