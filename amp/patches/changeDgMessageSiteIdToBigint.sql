delete from dg_message where site_id is null or site_id='amp';
ALTER TABLE `dg_message` MODIFY COLUMN `SITE_ID` BIGINT UNSIGNED NOT NULL;
