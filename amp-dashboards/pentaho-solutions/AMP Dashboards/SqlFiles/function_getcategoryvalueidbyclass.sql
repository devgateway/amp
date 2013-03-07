
CREATE OR REPLACE FUNCTION getcategoryvalueidbyclass(cat_val character varying, cat_class character varying)
  RETURNS bigint AS
$BODY$
declare 
	categoryId bigint;
BEGIN
	select amp_category_value.id into categoryId from amp_category_value
	left join amp_category_class  on (amp_category_class.id = amp_category_value.amp_category_class_id)
	where amp_category_class.keyname = cat_class and amp_category_value.category_value = cat_val;
	
	return categoryId;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getcategoryvalueidbyclass(character varying, character varying)
  OWNER TO postgres;
