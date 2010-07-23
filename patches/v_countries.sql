CREATE OR REPLACE VIEW `v_countries` AS 
		SELECT `ra`.`amp_activity_id` AS `amp_activity_id`, 
			getLocationName(getLocationIdByImplLoc(l.location_id, "Country")) as location_name, 
			getLocationIdByImplLoc(l.location_id, "Country") AS location_id,
			sum(ra.location_percentage) as location_percentage 
		FROM amp_activity_location ra, amp_location l  
		WHERE ra.amp_location_id=l.amp_location_id AND getLocationIdByImplLoc(l.location_id, "Country") is not null 
		GROUP BY ra.amp_activity_id,getLocationIdByImplLoc(l.location_id, "Country") ;
		
