SET @colName = 'Secondary Sector';
SET @colCellType = 'org.dgfoundation.amp.ar.cell.MetaTextCell';
SET @colExtractorView = 'v_secondary_sectors';
SET @filter = true;
SET @colId = (SELECT columnId FROM amp_columns WHERE columnName = 'Secondary Sector');
SET @stmt = IF(ISNULL(@colId) = 1,'INSERT INTO amp_columns(columnName, cellType, extractorView, filterRetrievable) VALUES(@colName, @colCellType, @colExtractorView, @filter)','UPDATE amp_columns SET columnId = 1 WHERE columnId = 1');

PREPARE stmt FROM @stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;