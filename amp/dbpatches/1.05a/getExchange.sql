drop function getExchange;
delimiter $
CREATE FUNCTION getExchange(currency char(3), cdate datetime) RETURNS double
begin
declare r double;
DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN END;
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
END;$
delimiter ;