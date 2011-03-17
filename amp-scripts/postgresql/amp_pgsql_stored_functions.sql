CREATE OR REPLACE FUNCTION comma_concat (text, text) RETURNS text AS $$
  DECLARE
    t text;
  BEGIN
    IF  character_length($1) > 0 THEN
      t = $1 ||', '|| $2;
    ELSE
      t = $2;
    END IF;
    RETURN t;
  END;
$$ LANGUAGE plpgsql;

CREATE AGGREGATE textagg (
    sfunc = comma_concat,
    basetype = text,
    stype = text,
    initcond = ''
);

CREATE OR REPLACE FUNCTION pc_chartobool(chartoconvert character varying)
  RETURNS boolean AS
$BODY$
SELECT CASE WHEN trim($1)='1'
        THEN true
    ELSE false END;
$BODY$
  LANGUAGE 'sql' IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION pc_chartoint(chartoconvert character varying)
  RETURNS integer AS
$BODY$
SELECT CASE WHEN trim($1) SIMILAR TO '[0-9]+'
        THEN CAST(trim($1) AS integer)
    ELSE NULL END;

$BODY$
  LANGUAGE 'sql' IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION getReportColumnId ( cn varchar(64)) RETURNS double precision AS
$$
declare id double precision;
begin
select columnId into id from amp_columns where columnName=cn limit 1;
return id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION getParentSectorId ( sectorId bigint) RETURNS bigint AS
$$
DECLARE
sid bigint;
pid bigint;
BEGIN
  select parent_sector_id into pid from amp_sector where amp_sector_id=sectorId;
  if pid is null then return sectorId;
  end if;
  while pid is not null loop
        sid := pid;
  select parent_sector_id into pid from amp_sector where amp_sector_id=sid;
  end loop;
  return sid;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION getSectorName ( sectorId bigint) RETURNS varchar(256) AS
$$
DECLARE
ret varchar(256);
BEGIN
 select name into ret from amp_sector where amp_sector_id=sectorId;
 return ret;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION applyThousandsForVisibility (d double precision) RETURNS double precision AS
$$
declare thousands varchar(255);
begin
select settingsvalue into thousands from amp_global_settings where settingsName='Amounts in Thousands';
if thousands='true' then return d*0.001;
end if;
return d;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION  getTransactionAmmountUSD(fdId bigint) RETURNS double precision AS
$$
declare
r double precision;
code char(3);
dateFD timestamp;
amount double precision;
begin
select transaction_amount/fixed_exchange_rate into r from amp_funding_detail where amp_fund_detail_id =fdId and fixed_exchange_rate is not NULL;
if r is not null then return r;
end if;
select cur.currency_code, transaction_date, transaction_amount into code,dateFD,amount from amp_currency cur inner join amp_funding_detail det on cur.amp_currency_id=det.amp_currency_id where det.amp_fund_detail_id =fdId;
return amount/getExchange(code,dateFD);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION getExchange(currency char(3), cdate timestamp) RETURNS double precision AS
$$
declare r double precision;
BEGIN
if currency='USD' then return 1;
end if;
select exchange_rate into r from amp_currency_rate where to_currency_code=currency and exchange_rate_date=cdate;
if r is not null then return r;
end if;
select exchange_rate into r from amp_currency_rate where to_currency_code=currency and exchange_rate_date<=cdate order by exchange_rate_date desc limit 1;
if r is not null then return r;
end if;
select exchange_rate into r from amp_currency_rate where to_currency_code=currency and exchange_rate_date>=cdate order by exchange_rate_date limit 1;
if r is not null then return r;
end if;
return 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION getExchangeWithFixed(currency char(3), cdate timestamp,fixedExchangeRate double precision) RETURNS double precision AS
$$
begin
  if fixedExchangeRate is not null then return fixedExchangeRate;
  end if;
  return getExchange(currency,cdate);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION getSectorId(sector varchar(255), subsect varchar(255)) RETURNS bigint AS
$$
declare
        s varchar(255);
        r bigint;
	sp varchar(255);
BEGIN
        s := TRIM(subsect);
	if length(subsect)=0 then
        s := TRIM(sector);
		select amp_sector_id into r from amp_sector where amp_sec_scheme_id = 3 and TRIM(name) like s and parent_sector_id is null;
	else
		select amp_sector_id into sp from amp_sector where amp_sec_scheme_id = 3 and TRIM(name) like TRIM(sector) and parent_sector_id is null;
     		select amp_sector_id into r from amp_sector where amp_sec_scheme_id = 3 and TRIM(name) like s and parent_sector_id like TRIM(sp);
	end if;
	return r;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION getLocationName(locationId BIGINT) RETURNS varchar(255) AS
$$
declare 
	locationName varchar(255);
BEGIN
	select location_name into locationName from amp_category_value_location l where l.id=locationId;
	return locationName;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION getLocationIdByImplLoc(paramCvLocationId BIGINT, implLocation varchar(255) ) RETURNS bigint AS
$$
declare ret bigint;	
	cValueId bigint;
	currentValueId bigint;
	parentCvLocationId bigint;
	cvLocationId bigint;
BEGIN
	cvLocationId := paramCvLocationId;
	currentValueId	:= 0;
	parentCvLocationId := cvLocationId;
	select v.id into cValueId from amp_category_value v, amp_category_class c where 
	c.keyName="implementation_location" AND v.category_value=implLocation AND c.id=v.amp_category_class_id;
	WHILE currentValueId<>cValueId AND parentCvLocationId is not null LOOP	
	cvLocationId	:= parentCvLocationId;
	select loc.parent_category_value, loc.parent_location into currentValueId,parentCvLocationId 
	from amp_category_value_location loc where loc.id=cvLocationId;	
	end LOOP;
	if currentValueId=cValueId THEN	return cvLocationId;
	end if;	
	return null; 
end;
$$ LANGUAGE plpgsql;	


CREATE OR REPLACE FUNCTION getSQLDateFormat() RETURNS varchar(12) AS
$$
declare
gsFormat varchar(12);
begin
select settingsValue into gsFormat from amp_global_settings where settingsName='Default Date Format';
if gsFormat='dd/MM/yyyy' then return '%d/%m/%Y';end if;
if gsFormat='dd/MMM/yyyy' then return '%d/%b/%Y';end if;
if gsFormat='MMM/dd/yyyy' then return '%b/%d/%Y';end if;
if gsFormat='MM/dd/yyyy' then return '%m/%d/%Y';end if;
return 'none';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pc_chartoint(chartoconvert character varying)
  RETURNS integer AS
$BODY$
SELECT CASE WHEN trim($1) SIMILAR TO '[0-9]+'
        THEN CAST(trim($1) AS integer)
    ELSE NULL END;

$BODY$
LANGUAGE 'sql' IMMUTABLE STRICT;


CREATE OR REPLACE FUNCTION getSectorDepth(sectorId BIGINT) RETURNS integer AS
$$
declare
      	 depth int;
      	 sid bigint;
      	 pid bigint;
begin      	
      	depth := 0;
      	select parent_sector_id into pid from amp_sector where amp_sector_id=sectorId;
      	if pid is null then return depth; 
      	end if;
      	while pid is not null LOOP
		depth := depth + 1;
		sid := pid;
		select parent_sector_id into pid from amp_sector where amp_sector_id=sid;
	end loop;
return depth;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION getParentSubSectorId(sectorId BIGINT) RETURNS bigint AS
$$
declare
	sid bigint;
	depth int;
BEGIN      	 	
	select getSectorDepth(sectorId) into depth from dual; 
	if (depth = 1) then return sectorId;
	end if; 	
	if (depth = 2) then 
		select parent_sector_id into sid from amp_sector where amp_sector_id=sectorId;
	return sid; 	
	end if;
return 0;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION getProgramSettingId(paramProgramId BIGINT) RETURNS bigint AS
$$
declare 
	ret bigint;
	parentId bigint;
	currentProgramId bigint;
	programId bigint;
begin
	 programId:=paramProgramId;
         parentId := 1;
         WHILE parentId is not null LOOP
		currentProgramId := programId;
		select parent_theme_id into parentId from amp_theme where amp_theme_id=programId;
		programId := parentId;
         end LOOP;
         select amp_program_settings_id into ret from amp_program_settings where default_hierarchy=currentProgramId;
         return ret;
end;
$$ LANGUAGE plpgsql;