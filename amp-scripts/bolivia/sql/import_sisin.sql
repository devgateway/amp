use amp_bolivia;

SET @commitment=0;
SET @disbursment=1;
SET @expenditure=2;

SET @funding_adjusment_planned=0;
SET @funding_adjusment_actual=1;

SET @usd_currency_id=21;
SET @mofed_perspective_id=2;

SET @base_url_sisin='http://www.google.com?sisinCode=';


ALTER TABLE amp_components ADD COLUMN CodigoSISIN VARCHAR(13) AFTER `type`;
ALTER TABLE amp_components ADD INDEX `Index_CodigoSISIN` USING BTREE(`CodigoSISIN`);


INSERT INTO amp_components (CodigoSISIN)
SELECT distinct CodigoSISIN FROM sisin_db.seguimiento_financiero s
where CodConvExt != '00000' and CodConvExt != '99999';

UPDATE amp_components a,
sisin_db.proyecto p
SET a.title = p.NombreProyecto,
a.description = p.DescripcionProyecto,
a.code = p.CodigoSISIN,
a.url = CONCAT(@base_url_sisin,p.CodigoSISIN)
where a.CodigoSISIN = p.CodigoSISIN;

INSERT INTO amp_activity_components(amp_activity_id, amp_component_id)
SELECT
a.amp_activity_id,
c.amp_component_id
FROM amp_components c
join sisin_db.seguimiento_financiero sf on c.CodigoSISIN = sf.CodigoSisin
join amp_activity a on a.amp_id = sf.CodConvExt
group by a.amp_activity_id, c.amp_component_id;


INSERT INTO amp_component_funding (transaction_type,adjustment_type,currency_id,perspective_id,amp_component_id,activity_id,transaction_amount,transaction_date,reporting_date)
SELECT
@commitment as transaction_type,
@funding_adjusment_planned as adjustment_type,
@usd_currency_id as currency_id,
@mofed_perspective_id as perspective_id,
c.amp_component_id,
a.amp_activity_id,
sf.MontoProgramado as transaction_amount,
STR_TO_DATE(CONCAT(sf.Mes, '/01/', sf.Ano), '%m/%d/%Y') as transaction_date,
FechaRegistro as reporting_date
FROM amp_components c
join sisin_db.seguimiento_financiero sf on c.CodigoSISIN = sf.CodigoSisin
join amp_activity a on a.amp_id = sf.CodConvExt and sf.MontoProgramado != 0;

INSERT INTO amp_component_funding (transaction_type,adjustment_type,currency_id,perspective_id,amp_component_id,activity_id,transaction_amount,transaction_date,reporting_date)
SELECT
@commitment as transaction_type,
@funding_adjusment_actual as adjustment_type,
@usd_currency_id as currency_id,
@mofed_perspective_id as perspective_id,
c.amp_component_id,
a.amp_activity_id,
sf.MontoReprogramado as transaction_amount,
STR_TO_DATE(CONCAT(sf.Mes, '/01/', sf.Ano), '%m/%d/%Y') as transaction_date,
FechaRegistro as reporting_date
FROM amp_components c
join sisin_db.seguimiento_financiero sf on c.CodigoSISIN = sf.CodigoSisin
join amp_activity a on a.amp_id = sf.CodConvExt and sf.MontoReprogramado != 0;


INSERT INTO amp_component_funding (transaction_type,adjustment_type,currency_id,perspective_id,amp_component_id,activity_id,transaction_amount,transaction_date,reporting_date)
SELECT
@expenditure,
@funding_adjusment_actual,
@usd_currency_id,
@mofed_perspective_id,
c.amp_component_id,
a.amp_activity_id,
sf.MontoEjecutado as transaction_amount,
STR_TO_DATE(CONCAT(sf.Mes, '/01/', sf.Ano), '%m/%d/%Y') as transaction_date,
FechaRegistro as reporting_date
FROM amp_components c
join sisin_db.seguimiento_financiero sf on c.CodigoSISIN = sf.CodigoSisin
join amp_activity a on a.amp_id = sf.CodConvExt and sf.MontoEjecutado != 0;