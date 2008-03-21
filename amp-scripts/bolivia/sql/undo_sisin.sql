use amp_bolivia;

/* SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; */

SET AUTOCOMMIT = 0;
START TRANSACTION;


DELETE FROM  ac
USING amp_activity_components as ac, amp_components as c
where c.CodigoSISIN is not null and ac.amp_component_id = c.amp_component_id;


DELETE FROM  acf
USING amp_component_funding as acf, amp_components as c
where c.CodigoSisin is not null and acf.amp_component_id = c.amp_component_id;

DELETE FROM amp_components where CodigoSISIN is not null;

COMMIT;

ALTER TABLE amp_components DROP INDEX `Index_CodigoSISIN`;
ALTER TABLE amp_components DROP COLUMN `CodigoSISIN`;
