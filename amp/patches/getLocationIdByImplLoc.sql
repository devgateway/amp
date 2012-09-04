DELIMITER $$
DROP FUNCTION IF EXISTS getLocationIdByImplLoc$$


CREATE FUNCTION getLocationIdByImplLoc(cvLocationId BIGINT(20), implLocation varchar(255) ) 
	RETURNS bigint(20)
BEGIN
	declare ret bigint(20);
	declare cValueId bigint(20);
	declare currentValueId bigint(20);
	declare parentCvLocationId bigint(20);

	set currentValueId	= 0;
	set parentCvLocationId	= cvLocationId;

	select v.id into cValueId
		from amp_category_value v, amp_category_class c
		where c.keyName="implementation_location" AND v.category_value=implLocation AND
			c.id=v.amp_category_class_id;	

	WHILE currentValueId<>cValueId AND parentCvLocationId is not null
	DO 
		set cvLocationId	= parentCvLocationId;
		select loc.parent_category_value, loc.parent_location into currentValueId,parentCvLocationId  
			from amp_category_value_location loc where loc.id=cvLocationId;
	end WHILE;

	if currentValueId=cValueId THEN
		return cvLocationId;
	end if;

	return null;
      
	end $$


