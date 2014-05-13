 CREATE OR REPLACE FUNCTION comma_concat(text, text)
 				  RETURNS text AS
 				$BODY$
 				  DECLARE
				  t text;
 				  BEGIN
 				  IF character_length($1) > 0 THEN
 				  t = $1 ||', '|| $2;
 				  ELSE
				  t = $2;
 				  END IF;
 				  RETURN t;
 				  END;
 				 $BODY$
 				  LANGUAGE plpgsql VOLATILE
 				  COST 100;
