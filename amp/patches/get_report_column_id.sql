delimiter $
DROP FUNCTION IF EXISTS getReportColumnId$
CREATE FUNCTION getReportColumnId(cn varchar(64)) RETURNS double 
begin 
declare id double;
select columnId into id from amp_columns where columnName=cn limit 1;
return id;
END;$