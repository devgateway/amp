DROP TABLE IF EXISTS dg_message_clean;
alter table dg_message drop primary key;
CREATE TABLE dg_message_clean like dg_message;
alter table dg_message_clean add primary key (message_key,lang_iso,site_id);
INSERT IGNORE INTO dg_message_clean (select * from dg_message);
TRUNCATE table dg_message;
INSERT INTO dg_message (select * from dg_message_clean);
alter table dg_message add primary key (message_key,lang_iso,site_id);
DROP TABLE IF EXISTS dg_message_clean;