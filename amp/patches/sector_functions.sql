delimiter $
DROP FUNCTION IF EXISTS getParentSectorId$
CREATE FUNCTION `getParentSectorId`(sectorId BIGINT(20)) RETURNS bigint(20)
BEGIN
        declare sid bigint(20);
        declare pid bigint(20);
        select parent_sector_id into pid from amp_sector where amp_sector_id=sectorId;
        if pid is null then 
                return sectorId;
        end if;
        while pid is not null do
                set sid =pid;
                select parent_sector_id into pid from amp_sector where amp_sector_id=sid;
        end while;
        return sid;
END;$
DROP FUNCTION IF EXISTS getSectorName$
CREATE FUNCTION getSectorName(sectorId BIGINT(20)) RETURNS varchar(256)
BEGIN
         declare ret varchar(256);
         select name into ret from amp_sector where amp_sector_id=sectorId;
    return ret;
END;$
CREATE OR REPLACE VIEW `v_sectors` AS select `sa`.`amp_activity_id` AS `amp_activity_id`, getSectorName(getParentSectorId(s.amp_sector_id))  as sectorname, getParentSectorId(s.amp_sector_id) AS `amp_sector_id`,sum(`sa`.`sector_percentage`) AS `sector_percentage` from  amp_activity_sector sa, amp_sector s where s.amp_sector_id=sa.amp_sector_id group by `sa`.`amp_activity_id`,sectorname order by `sa`.`amp_activity_id`,sectorname$