use amp_burundi;
SET AUTOCOMMIT = 0;
START TRANSACTION;
update dg_user as us set  us.COUNTRY_ISO='bi' where us.COUNTRY_ISO='_a';
COMMIT;