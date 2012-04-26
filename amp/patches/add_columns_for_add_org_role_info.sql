CREATE OR REPLACE VIEW v_executing_agency_info AS 
	select aor.activity as amp_activity_id, aor.additional_info, aor.organisation as amp_org_id from amp_org_role aor, amp_role r where aor.role=r.amp_role_id AND r.role_code="EA" ;
	
CREATE OR REPLACE VIEW v_beneficiary_agency_info AS 
	select aor.activity as amp_activity_id, aor.additional_info, aor.organisation as amp_org_id from amp_org_role aor, amp_role r where aor.role=r.amp_role_id AND r.role_code="BA" ;	
	
CREATE OR REPLACE VIEW v_contracting_agency_info AS 
	select aor.activity as amp_activity_id, aor.additional_info, aor.organisation as amp_org_id from amp_org_role aor, amp_role r where aor.role=r.amp_role_id AND r.role_code="CA" ;	
	
CREATE OR REPLACE VIEW v_implementing_agency_info AS 
	select aor.activity as amp_activity_id, aor.additional_info, aor.organisation as amp_org_id from amp_org_role aor, amp_role r where aor.role=r.amp_role_id AND r.role_code="IA" ;	
	
CREATE OR REPLACE VIEW v_regional_group_info AS 
	select aor.activity as amp_activity_id, aor.additional_info, aor.organisation as amp_org_id from amp_org_role aor, amp_role r where aor.role=r.amp_role_id AND r.role_code="RG" ;	
	
CREATE OR REPLACE VIEW v_responsible_org_info AS 
	select aor.activity as amp_activity_id, aor.additional_info, aor.organisation as amp_org_id from amp_org_role aor, amp_role r where aor.role=r.amp_role_id AND r.role_code="RO" ;
	
CREATE OR REPLACE VIEW v_sector_group_info AS 
	select aor.activity as amp_activity_id, aor.additional_info, aor.organisation as amp_org_id from amp_org_role aor, amp_role r where aor.role=r.amp_role_id AND r.role_code="SG" ;
	
	
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView) 
	VALUES("Executing Agency Additional Info", "execAgencyAddInfo", "org.dgfoundation.amp.ar.cell.TrnTextCell", "v_executing_agency_info");
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView) 
	VALUES("Beneficiary Agency  Additional Info", "benAgencyAddInfo", "org.dgfoundation.amp.ar.cell.TrnTextCell", "v_beneficiary_agency_info");
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView) 
	VALUES("Contracting Agency Additional Info", "contrAgencyAddInfo", "org.dgfoundation.amp.ar.cell.TrnTextCell", "v_contracting_agency_info");
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView) 
	VALUES("Implementing Agency Additional Info", "implAgencyAddInfo", "org.dgfoundation.amp.ar.cell.TrnTextCell", "v_implementing_agency_info");
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView) 
	VALUES("Regional Group Additional Info", "regGrpAddInfo", "org.dgfoundation.amp.ar.cell.TrnTextCell", "v_regional_group_info");
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView) 
	VALUES("Responsible Organization Additional Info", "respOrgAddInfo", "org.dgfoundation.amp.ar.cell.TrnTextCell", "v_responsible_org_info");
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView) 
	VALUES("Sector Group Additional Info", "sectGrpAddInfo", "org.dgfoundation.amp.ar.cell.TrnTextCell", "v_sector_group_info");
	
