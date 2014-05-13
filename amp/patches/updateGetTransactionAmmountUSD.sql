delimiter $
DROP FUNCTION IF EXISTS getTransactionAmmountUSD$
CREATE FUNCTION getTransactionAmmountUSD(fdId long) RETURNS double 
BEGIN 
declare r double;
declare code char(3);
declare dateFD datetime;  
declare amount double;  
select cur.currency_code, transaction_date, transaction_amount,fixed_exchange_rate into code,dateFD,amount,r from amp_currency cur inner join amp_funding_detail det on cur.amp_currency_id=det.amp_currency_id where det.amp_fund_detail_id =fdId;
if r is not null then return amount/r*getExchange("USD",dateFD); end if;
return amount/getExchange(code,dateFD)*getExchange("USD",dateFD); 
END$