CREATE OR REPLACE FUNCTION GETSECTORDEPTH
(sectorId IN NUMBER) RETURN NUMBER 
IS
BEGIN        
	declare 
		depth number; 
        sid number;
        pid number; 
BEGIN
		depth := 0; 
        select parent_sector_id into pid from amp_sector where amp_sector_id=sectorId; 
        if pid is null then 
                 return depth; 
        end if; 
        loop
        depth := depth + 1; 
        sid := pid; 
                select parent_sector_id into pid from amp_sector where amp_sector_id=sid;
        if  pid is not null then exit; end if;
        end loop;
        return depth; 
END;
END;