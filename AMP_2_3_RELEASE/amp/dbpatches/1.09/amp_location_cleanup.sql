select * FROM amp_location l WHERE l.amp_location_id < (select ll.amp_location_id from amp_location ll where
ll.amp_location_id!=amp_location_id and l.country=ll.country and l.region_id=ll.region_id and
l.zone_id=ll.zone_id and l.woreda_id=ll.woreda_id);