DELIMITER $

DROP FUNCTION IF EXISTS getSectorDepth $ 
CREATE FUNCTION getSectorDepth(sectorId BIGINT(20)) RETURNS int  
BEGIN        
	declare depth int; 
        declare sid bigint(20); 
        declare pid bigint(20); 
	set depth = 0; 
        select parent_sector_id into pid from amp_sector where amp_sector_id=sectorId; 
        if pid is null then 
                 return depth; 
        end if; 
        while pid is not null do 
		set depth = depth + 1; 
                set sid = pid; 
                select parent_sector_id into pid from amp_sector where amp_sector_id=sid; 
        end while; 
        return depth; 
END$ 

DROP FUNCTION IF EXISTS getParentSubSectorId $ 
CREATE FUNCTION getParentSubSectorId(sectorId BIGINT(20)) RETURNS bigint(20) 
BEGIN 
    declare sid bigint(20); 
	declare depth int; 
	select getSectorDepth(sectorId) into depth from dual; 
	if (depth = 1) then 
		return sectorId; 
	end if; 
	if (depth = 2) then 
	        select parent_sector_id into sid from amp_sector where amp_sector_id=sectorId; 
		return sid; 
	end if; 
	
	return 0; 
END$ 
