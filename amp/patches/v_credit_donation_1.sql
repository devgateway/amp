 CREATE OR REPLACE VIEW `v_credit_donation` AS
	SELECT amp_activity_id AS `amp_activity_id`,
	(case when name = convert('Donaci�n' using utf8) then convert('Donaci�n' using utf8) else convert('Credito' using utf8) end) AS `name`,
	(case when name = convert('Donaci�n' using utf8) then 0 else 1 end) AS `id`
	FROM v_financing_instrument v;


