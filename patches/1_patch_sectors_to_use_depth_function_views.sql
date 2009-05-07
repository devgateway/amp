DELIMITER $$

DROP FUNCTION IF EXISTS getSectorDepth $$
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
END$$

DELIMITER ;

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_sub_sub_sectors AS 
select a.amp_activity_id, s.name, s.amp_sector_id, a.sector_percentage  
from amp_activity_sector a, amp_sector s
where a.amp_sector_id=s.amp_sector_id and getSectorDepth(s.amp_sector_id)=2
and s.amp_sec_scheme_id in (select c.classification_id from amp_classification_config c where (c.name = _latin1'Primary'))
order by a.amp_activity_id, s.name;

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_secondary_sub_sub_sectors AS 
select a.amp_activity_id, s.name, s.amp_sector_id, a.sector_percentage  
from amp_activity_sector a, amp_sector s
where a.amp_sector_id=s.amp_sector_id and getSectorDepth(s.amp_sector_id)=2
and s.amp_sec_scheme_id in (select c.classification_id from amp_classification_config c where (c.name = _latin1'Secondary'))
order by a.amp_activity_id, s.name;

update amp_columns set relatedContentPersisterClass="org.digijava.module.aim.dbentity.AmpSector" where columnName="Primary Sector Sub-Sub-Sector";
update amp_columns set relatedContentPersisterClass="org.digijava.module.aim.dbentity.AmpSector" where columnName="Secondary Sub-Sub Sector";





DELIMITER $$

DROP FUNCTION IF EXISTS getParentSubSectorId$$
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
END$$

DELIMITER ;

CREATE OR REPLACE SQL SECURITY DEFINER VIEW `v_sub_sectors` AS 
select 
sa.amp_activity_id,
getSectorName(getParentSubSectorId(s.amp_sector_id)) AS sectorname,
getParentSubSectorId(s.amp_sector_id) AS amp_sector_id,
sum(sa.sector_percentage) AS sector_percentage,
s.amp_sec_scheme_id,
ss.sec_scheme_name
from
(((amp_sector_scheme ss join amp_classification_config cc on(((cc.name = _latin1'Primary') and (cc.classification_id = ss.amp_sec_scheme_id))))
join amp_sector s on((s.amp_sec_scheme_id = ss.amp_sec_scheme_id) and (getSectorDepth(s.amp_sector_id) >= 1))) 
join amp_activity_sector sa on(((sa.amp_sector_id = s.amp_sector_id) and (sa.classification_config_id = cc.id)))) 
group by sa.amp_activity_id, getParentSubSectorId(s.amp_sector_id) order by sa.amp_activity_id, getSectorName(getParentSubSectorId(s.amp_sector_id));

CREATE OR REPLACE SQL SECURITY DEFINER VIEW `v_secondary_sub_sectors` AS 
select 
sa.amp_activity_id,
getSectorName(getParentSubSectorId(s.amp_sector_id)) AS sectorname,
getParentSubSectorId(s.amp_sector_id) AS amp_sector_id,
sum(sa.sector_percentage) AS sector_percentage,
s.amp_sec_scheme_id,
ss.sec_scheme_name
from
(((amp_sector_scheme ss join amp_classification_config cc on(((cc.name = _latin1'Secondary') and (cc.classification_id = ss.amp_sec_scheme_id))))
join amp_sector s on((s.amp_sec_scheme_id = ss.amp_sec_scheme_id) and (getSectorDepth(s.amp_sector_id) >= 1))) 
join amp_activity_sector sa on(((sa.amp_sector_id = s.amp_sector_id) and (sa.classification_config_id = cc.id)))) 
group by sa.amp_activity_id, getParentSubSectorId(s.amp_sector_id) order by sa.amp_activity_id, getSectorName(getParentSubSectorId(s.amp_sector_id));


