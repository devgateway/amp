delimiter $
DROP FUNCTION IF EXISTS getExchangeWithFixed$
CREATE FUNCTION getExchangeWithFixed(currency char(3), cdate datetime,fixedExchangeRate double) RETURNS double begin if fixedExchangeRate is not null then return fixedExchangeRate;end if;return getExchange(currency,cdate);end;$
