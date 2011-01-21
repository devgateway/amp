CREATE OR REPLACE FUNCTION getParentSubSectorId(sectorId NUMBER) RETURN NUMBER
IS
BEGIN 
    declare sid number; 
	depth number; 
	BEGIN
	select getSectorDepth(sectorId) into depth from dual; 
	if (depth = 1) then 
		return sectorId; 
	end if; 
	if (depth = 2) then 
	        select parent_sector_id into sid from amp_sector where amp_sector_id=sectorId; 
		return sid; 
	end if; 
	
	return 0; 
END;
END;