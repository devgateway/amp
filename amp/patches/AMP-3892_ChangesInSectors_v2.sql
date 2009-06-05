START TRANSACTION;

UPDATE amp_columns set columnName = 'Secondary Sector Sub-Sub-Sector' WHERE columnName = 'Secondary Sector Sub-Sub Sector';

COMMIT;