CREATE OR REPLACE VIEW v_costs AS
select eu.amp_activity_id as amp_activity_id,
eu.id as eu_id,
eu.total_cost as cost,
c.currency_code as currency_code,
eu.transaction_date as currency_date,
`getExchange`(`c`.`currency_code`,`eu`.`transaction_date`) AS `exchange_rate`
from amp_eu_activity eu, amp_currency c
where eu.total_cost_currency_id=c.amp_currency_id;