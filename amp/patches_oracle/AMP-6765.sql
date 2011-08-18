CREATE OR REPLACE VIEW v_regional_funding AS select f.activity_id AS 
amp_activity_id,f.amp_regional_funding_id AS amp_regional_funding_id,f.amp_regional_funding_id AS
amp_fund_detail_id,r.name AS region_name,f.transaction_type AS 
transaction_type,f.adjustment_type AS adjustment_type,f.transaction_date AS 
transaction_date,f.transaction_amount AS transaction_amount,c.currency_code AS 
currency_code,f.region_id AS region_id from amp_regional_funding f , amp_region r
, amp_currency c where ((c.amp_currency_id = f.currency_id) and (f.region_id = r.amp_region_id)) order by f.activity_id;