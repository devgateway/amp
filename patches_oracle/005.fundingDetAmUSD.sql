delimiter $
create or replace FUNCTION getTransactionAmmountUSD(fdId Long) RETURN NUMBER IS
  BEGIN
    DECLARE
      r    NUMBER;
      code VARCHAR(3);
      dateFD TIMESTAMP;
      amount NUMBER;
    BEGIN
      SELECT transaction_amount/fixed_exchange_rate INTO r FROM amp_funding_detail WHERE amp_fund_detail_id=fdId;
      if r is not null then return r;end if;
      SELECT cur.currency_code,transaction_date,transaction_amount INTO code, dateFD,amount FROM amp_currency cur INNER JOIN amp_funding_detail det ON cur.amp_currency_id=det.amp_currency_id WHERE det.amp_fund_detail_id=fdId;
      RETURN amount/getExchange(code,dateFD);
    END;
END;$