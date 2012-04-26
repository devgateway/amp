CREATE OR REPLACE FUNCTION getExchange(currency char, cdate timestamp) RETURN number 
IS
BEGIN
	declare 
  r number;
	base_curr char(3);
BEGIN
	select settingsValue into base_curr from amp_global_settings where settingsName='Base Currency';
	if base_curr is null then base_curr := 'USD';
	end if;
	if currency=base_curr then return 1;
	end if;
	select exchange_rate into r from amp_currency_rate where to_currency_code=currency and from_currency_code=base_curr and exchange_rate_date=cdate;
	if r is not null then return r;
	end if;
	select exchange_rate into r from amp_currency_rate where to_currency_code=currency and from_currency_code=base_curr and exchange_rate_date<=cdate and rownum < 2 order by exchange_rate_date desc;
	if r is not null then return r;
	end if;
	select exchange_rate into r from amp_currency_rate where to_currency_code=currency and from_currency_code=base_curr and exchange_rate_date>=cdate and rownum < 2 order by exchange_rate_date;
	if r is not null then return r;
	end if;
	return 1;
END;
END;