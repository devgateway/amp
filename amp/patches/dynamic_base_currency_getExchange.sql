DELIMITER $$
drop function if exists getExchange$$


CREATE FUNCTION getExchange(currency char(3), cdate datetime) RETURNS double 

BEGIN
	declare r double;
	declare base_curr char(3);
	DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN END;
	select settingsValue into base_curr from amp_global_settings where settingsName="Base Currency";
	if base_curr is null
		then set base_curr='USD';
	end if;
	if currency=base_curr then return 1;
	end if;
	select exchange_rate into r from amp_currency_rate where to_currency_code=currency and from_currency_code=base_curr and exchange_rate_date=cdate;
	if r is not null then return r;
	end if;
	select exchange_rate into r from amp_currency_rate where to_currency_code=currency and from_currency_code=base_curr and exchange_rate_date<=cdate order by exchange_rate_date desc limit 1;
	if r is not null then return r;
	end if;
	select exchange_rate into r from amp_currency_rate where to_currency_code=currency and from_currency_code=base_curr and exchange_rate_date>=cdate order by exchange_rate_date limit 1;
	if r is not null then return r;
	end if;
	return 1;


END $$
