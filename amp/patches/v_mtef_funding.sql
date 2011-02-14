CREATE OR REPLACE VIEW v_mtef_funding AS
select f.amp_activity_id, m.amp_fund_mtef_projection_id, m.amount, c.currency_code, m.projection_date as currency_date,
getExchange(c.currency_code,m.projection_date) AS exchange_rate
from amp_funding f, amp_funding_mtef_projection m, amp_currency c, amp_category_class acc, amp_category_value acv 
where m.amp_funding_id=f.amp_funding_id and m.amp_currency_id=c.amp_currency_id and acv.amp_category_class_id=acc.id 
 and acc.keyName="mtef_projection" and acv.category_value="projection" and m.amp_projected_categoryvalue_id=acv.id;