DELIMITER $$
DROP FUNCTION IF EXISTS getSectorName$$
CREATE FUNCTION getSectorName(sectorId BIGINT(20)) RETURNS varchar(256)
BEGIN
         declare ret varchar(256);
         select name into ret from amp_sector where amp_sector_id=sectorId;
    return ret;
END$$
CREATE OR REPLACE VIEW `v_sectors` AS select `sa`.`amp_activity_id` AS `amp_activity_id`, getSectorName(getParentSectorId(s.amp_sector_id))  as sectorname, getParentSectorId(s.amp_sector_id) AS `amp_sector_id`,sum(`sa`.`sector_percentage`) AS `sector_percentage` from  amp_activity_sector sa, amp_sector s where s.amp_sector_id=sa.amp_sector_id group by `sa`.`amp_activity_id`,sectorname order by `sa`.`amp_activity_id`,sectorname;
