CREATE OR REPLACE VIEW v_component_funding AS 
select f.activity_id AS amp_activity_id,f.amp_component_funding_id AS amp_component_funding_id,
f.amp_component_funding_id AS amp_fund_detail_id, ct.name as component_type,f.transaction_type AS transaction_type,
f.adjustment_type AS adjustment_type,f.transaction_date AS transaction_date,f.transaction_amount AS transaction_amount,
f.currency_id AS currency_id,cu.currency_code AS currency_code 
from (((amp_component_funding f join amp_components c on (f.amp_component_id = c.amp_component_id)) 
join amp_currency cu on (cu.amp_currency_id = f.currency_id) ) join amp_component_type ct on (ct.type_id=c.type) )  order by f.activity_id;