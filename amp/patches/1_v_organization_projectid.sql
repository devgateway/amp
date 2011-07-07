CREATE OR REPLACE VIEW v_organization_projectid AS 
SELECT aaii.amp_activity_id, CONCAT(org.name,' -- ', aaii.internal_id), aaii.amp_org_id 
FROM amp_activity_internal_id aaii, amp_organisation org 
WHERE aaii.amp_org_id = org.amp_org_id;

INSERT INTO amp_columns (columnName, aliasName, cellType, extractorView) 
VALUES ('Organizations and Project ID','org_proj_id', 
'org.dgfoundation.amp.ar.cell.TextCell',
'v_organization_projectid');