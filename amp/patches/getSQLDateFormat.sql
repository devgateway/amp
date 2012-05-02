delimiter $
DROP FUNCTION IF EXISTS getSQLDateFormat$
CREATE FUNCTION getSQLDateFormat() RETURNS varchar(12) 
begin 
declare gsFormat varchar(12);
select settingsValue into gsFormat from amp_global_settings where settingsName='Default Date Format';
if gsFormat='dd/MM/yyyy' then return '%d/%m/%Y';
end if;
if gsFormat='dd/MMM/yyyy' then return '%d/%b/%Y';
end if;
if gsFormat='MMM/dd/yyyy' then return '%b/%d/%Y';
end if;
if gsFormat='MM/dd/yyyy' then return '%m/%d/%Y';
end if;
return 'none';
END;$