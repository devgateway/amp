UPDATE amp_columns SET columnName="Executing Agency Department/Division",  aliasName="execAgencyDept" 
	WHERE  extractorView="v_executing_agency_info";
UPDATE amp_columns SET columnName="Beneficiary Agency  Department/Division", aliasName="benAgencyDept"  
	WHERE  extractorView="v_beneficiary_agency_info";
UPDATE amp_columns SET columnName="Contracting Agency Department/Division", aliasName="contrAgencyDept" 
	WHERE  extractorView="v_contracting_agency_info";
UPDATE amp_columns SET columnName="Implementing Agency Department/Division", aliasName="implAgencyDept" 
	WHERE  extractorView="v_implementing_agency_info";
UPDATE amp_columns SET columnName="Regional Group Department/Division", aliasName="regGrpDept"
	WHERE  extractorView="v_regional_group_info";
UPDATE amp_columns SET columnName="Responsible Organization Department/Division", aliasName="respOrgDept" 
	WHERE  extractorView="v_responsible_org_info";
UPDATE amp_columns SET columnName="Sector Group Department/Division", aliasName="sectGrpDept" 
	WHERE  extractorView="v_sector_group_info";
	

	
UPDATE amp_fields_visibility SET name="Executing Agency Department/Division",  description="Executing Agency Department/Division" 
	WHERE  name="Executing Agency Additional Info";
UPDATE amp_fields_visibility SET name="Beneficiary Agency  Department/Division", description="Beneficiary Agency  Department/Division"  
	WHERE  name="Beneficiary Agency  Additional Info";
UPDATE amp_fields_visibility SET name="Contracting Agency Department/Division", description="Contracting Agency Department/Division" 
	WHERE  name="Contracting Agency Additional Info";
UPDATE amp_fields_visibility SET name="Implementing Agency Department/Division", description="Implementing Agency Department/Division" 
	WHERE  name="Implementing Agency Additional Info";
UPDATE amp_fields_visibility SET name="Regional Group Department/Division", description="Regional Group Department/Division"
	WHERE  name="Regional Group Additional Info";
UPDATE amp_fields_visibility SET name="Responsible Organization Department/Division", description="Responsible Organization Department/Division" 
	WHERE  name="Responsible Organization Additional Info";
UPDATE amp_fields_visibility SET name="Sector Group Department/Division", description="Sector Group Department/Division" 
	WHERE  name="Sector Group Additional Info";