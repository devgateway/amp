DROP TABLE IF EXISTS `amp_funding_pledges_details_temp`;

CREATE TABLE `amp_funding_pledges_details_temp` SELECT * FROM `amp_funding_pledges_details`;

UPDATE `amp_funding_pledges_details` SET  `amp_funding_pledges_details`.`year` = (select YEAR(funding_date) from `amp_funding_pledges_details_temp` WHERE `amp_funding_pledges_details`.`id` = `amp_funding_pledges_details_temp`.id);

DROP TABLE IF EXISTS `amp_funding_pledges_details_temp`;

ALTER TABLE `amp_funding_pledges_details` DROP COLUMN `funding_date`;

