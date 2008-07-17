delimiter $
DROP FUNCTION IF EXISTS applyThousandsForVisibility$
CREATE FUNCTION applyThousandsForVisibility (d double) RETURNS double 
begin 
declare thousands char(5);
select settingsValue into thousands from amp_global_settings where settingsName='Amounts in Thousands';
if thousands='true' then return d*0.001;
end if;
return d;
END;$