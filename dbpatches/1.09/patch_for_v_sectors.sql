DELIMITER $$

DROP FUNCTION IF EXISTS getParentSectorId$$
CREATE FUNCTION getParentSectorId(sectorId BIGINT(20)) RETURNS bigint(20)
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
END$$

DELIMITER ;

CREATE OR REPLACE VIEW `v_sectors` AS select `sa`.`amp_activity_id` AS `amp_activity_id`,`s`.`name` AS `name`,`s`.`amp_sector_id` AS `amp_sector_id`,`sa`.`sector_percentage` AS `sector_percentage` from (`amp_activity_sector` `sa` join `amp_sector` `s` on((getParentSectorId(`sa`.`amp_sector_id`) = `s`.`amp_sector_id`))) order by `sa`.`amp_activity_id`,`s`.`name`

