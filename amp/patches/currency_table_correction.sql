SET AUTOCOMMIT = 0;
START TRANSACTION;
UPDATE amp_currency AS c, dg_countries AS cn SET c.country_id=cn.ISO WHERE c.country_name=cn.COUNTRY_NAME;
DELETE FROM amp_currency WHERE currency_code='' OR currency_code=null;
COMMIT;