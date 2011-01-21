CREATE OR REPLACE VIEW `v_zones` AS 
select `ra`.`amp_activity_id` AS `amp_activity_id`, 
	getLocationName(getLocationIdByImplLoc(l.location_id, "Zone")) as location_name, 
	getLocationIdByImplLoc(l.location_id, "Zone") AS location_id,
	sum(ra.location_percentage) as location_percentage 
from amp_activity_location ra, amp_location l  
where ra.amp_location_id=l.amp_location_id AND getLocationIdByImplLoc(l.location_id, "Zone") is not null  

group by ra.amp_activity_id,getLocationIdByImplLoc(l.location_id, "Zone") ;
