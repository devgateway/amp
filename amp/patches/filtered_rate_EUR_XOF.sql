INSERT INTO amp_filtered_currency_rate (from_currency_id, to_currency_id)
	SELECT f.amp_currency_id, t.amp_currency_id 
		FROM amp_currency f, amp_currency t 
		WHERE f.currency_code="EUR" AND t.currency_code="XOF"; 
		
DELETE FROM amp_currency_rate WHERE from_currency_code="EUR" AND to_currency_code="XOF";
DELETE FROM amp_currency_rate WHERE from_currency_code="XOF" AND to_currency_code="EUR";

INSERT INTO amp_currency_rate(from_currency_code, to_currency_code, exchange_rate, exchange_rate_date, data_source) 
	VALUES("EUR", "XOF", 655.957, curdate(), 1);
INSERT INTO amp_currency_rate(from_currency_code, to_currency_code, exchange_rate, exchange_rate_date, data_source) 
	VALUES("XOF", "EUR", 0.0015244901723741038, curdate(), 1);