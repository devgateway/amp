START TRANSACTION;

SET @colId = 0;
SET @colName = 'Secondary Sector';
SET @colCellType = 'org.dgfoundation.amp.ar.cell.MetaTextCell';
SET @colExtractorView = 'v_secondary_sectors';
SET @filter = true;

SELECT columnId INTO @colId FROM amp_columns WHERE columnName = @colName;

SET @stmt = IF(@colId = 0,'INSERT INTO amp_columns (columnName, cellType, extractorView, filterRetrievable) VALUES(@colName, @colCellType, @colExtractorView, @filter);','SELECT null;');

PREPARE stmt FROM @stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

COMMIT;