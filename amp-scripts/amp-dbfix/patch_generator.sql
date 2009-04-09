SELECT '/*!40014 SET  FOREIGN_KEY_CHECKS=0, UNIQUE_CHECKS=0 */;' FROM dual;
CREATE TABLE columns_ampclean AS (SELECT column_name,table_name FROM information_schema.columns WHERE table_schema='AMP_CLEAN');
CREATE TABLE tables_ampclean AS (SELECT table_name from information_schema.tables WHERE table_schema='AMP_CLEAN');
SELECT CONCAT('DROP TABLE ',t.table_name,';') AS '' FROM information_schema.tables t WHERE t.table_name NOT IN (SELECT tt.table_name from tables_ampclean tt) AND t.table_schema=DATABASE() AND t.table_name NOT LIKE 'v_%';
SELECT CONCAT('ALTER TABLE ', c.table_name,' DROP FOREIGN KEY ',kcu.constraint_name,';') AS '' FROM information_schema.columns c, information_schema.key_column_usage kcu WHERE
c.table_schema=DATABASE() AND c.table_name IN (SELECT tt.table_name FROM tables_ampclean tt) AND c.column_name NOT IN (SELECT cc.column_name FROM columns_ampclean cc WHERE cc.table_name=c.table_name) AND c.table_name NOT LIKE 'v_%'
AND kcu.table_name=c.table_name AND kcu.column_name=c.column_name AND kcu.table_schema=DATABASE();
SELECT CONCAT('ALTER TABLE ', c.table_name,' DROP COLUMN ',c.column_name,';') AS '' FROM information_schema.columns c WHERE
c.table_schema=DATABASE() AND c.table_name IN (SELECT tt.table_name FROM tables_ampclean tt) AND c.column_name NOT IN (SELECT cc.column_name FROM columns_ampclean cc WHERE cc.table_name=c.table_name) AND c.table_name NOT LIKE 'v_%';
