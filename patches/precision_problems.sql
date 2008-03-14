ALTER TABLE `amp_activity_sector` MODIFY COLUMN `sector_percentage` DECIMAL(8,4) DEFAULT NULL;
ALTER TABLE `amp_activity` MODIFY COLUMN `actual_start_date` DATETIME;
ALTER TABLE `amp_activity` MODIFY COLUMN `actual_completion_date` DATETIME DEFAULT NULL;
ALTER TABLE `amp_activity` MODIFY COLUMN `proposed_start_date` DATETIME DEFAULT NULL;
ALTER TABLE `amp_funding_detail` MODIFY COLUMN `transaction_date` DATETIME DEFAULT NULL;

