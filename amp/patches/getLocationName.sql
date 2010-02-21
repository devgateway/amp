DELIMITER $$
DROP FUNCTION IF EXISTS getLocationName$$

CREATE FUNCTION getLocationName(locationId BIGINT(20)) 
	RETURNS varchar(255)

BEGIN
	declare locationName varchar(255);
	select location_name into locationName from amp_category_value_location l 
		where l.id=locationId;
	return locationName;
end $$
