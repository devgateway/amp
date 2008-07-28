delimiter $
DROP FUNCTION IF EXISTS getTransactionAmmountUSD$
CREATE FUNCTION getTransactionAmmountUSD(fdId long) RETURNS double 
begin 
declare r double; 
declare code char(3); 
declare dateFD datetime;  
declare amount double;  

select fixed_exchange_rate*transaction_amount into r from amp_funding_detail where amp_fund_detail_id =fdId;
if r is not null then return r;
end if; 

select cur.currency_code, transaction_date, transaction_amount into code,dateFD,amount from amp_currency cur inner join amp_funding_detail det on cur.amp_currency_id=det.amp_currency_id where det.amp_fund_detail_id =fdId;
 

return getExchange(code,dateFD)*amount; 
END$