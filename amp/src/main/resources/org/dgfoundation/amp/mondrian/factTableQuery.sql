DELETE FROM mondrian_fact_table WHERE entity_id @@activityIdCondition@@;

INSERT INTO mondrian_fact_table (entity_id, funding_id, entity_internal_id, transaction_type, adjustment_type, transaction_date, date_code, 
  transaction_amount, expenditure_class, 
  currency_id, donor_id, 
  financing_instrument_id, terms_of_assistance_id, funding_status_id, mode_of_payment_id, status_id, modality_id, type_of_cooperation_id, type_of_implementation_id, procurement_system_id,
  primary_sector_id, secondary_sector_id, tertiary_sector_id, location_id,
  primary_program_id, secondary_program_id, tertiary_program_id, national_objectives_program_id,
  ea_org_id, ba_org_id, ia_org_id, ro_org_id, ca_org_id, rg_org_id, sg_org_id,
  component_id, agreement_id,
  capital_spend_percent, disaster_response,
  src_role, dest_role, dest_org_id, flow_name,
  related_entity_id)
  SELECT 
	rawdonation.amp_activity_id AS entity_id,
	rawdonation.funding_id AS funding_id,
	rawdonation.amp_fund_detail_id AS entity_internal_id,
    rawdonation.transaction_type AS transaction_type,
    rawdonation.adjustment_type AS adjustment_type,
    rawdonation.transaction_date AS transaction_date,
    rawdonation.date_code AS date_code,

	rawdonation.transaction_amount * (
         COALESCE(location.percentage, 1) *
         COALESCE(prim_prog.percentage, 1) *
         COALESCE(sec_prog.percentage, 1) *
		 COALESCE(tert_prog.percentage, 1) *
         COALESCE(npo_prog.percentage, 1) *
         COALESCE(prim_sect.percentage, 1) *
		 COALESCE(sec_sect.percentage, 1) *
         COALESCE(tert_sect.percentage, 1) *
         COALESCE(ra.percentage, 1) *
         COALESCE(ba.percentage, 1) *
         COALESCE(ia.percentage, 1) *
         COALESCE(ea.percentage, 1) *
         COALESCE(ca.percentage, 1) *
         COALESCE(rg.percentage, 1) *
         COALESCE(sg.percentage, 1)
         ) AS transaction_amount,
	 rawdonation.expenditure_class AS expenditure_class,
     rawdonation.currency_id AS currency_id,
	 CASE WHEN rawdonation.src_role='DN' THEN rawdonation.originating_org_id ELSE @@BUGCHOOSER@@ END AS donor_id,
     COALESCE(rawdonation.financing_instrument_id, 999999999) AS financing_instrument_id,
     COALESCE(rawdonation.terms_of_assistance_id, 999999999) AS terms_of_assistance_id,
     COALESCE(rawdonation.funding_status_id, 999999999) AS funding_status_id,
     COALESCE(rawdonation.mode_of_payment_id, 999999999) AS mode_of_payment_id,
     COALESCE(rawdonation.status_id, 999999999) AS status_id,
     COALESCE(rawdonation.modality_id, 999999999) AS modality_id,
     COALESCE(rawdonation.type_of_cooperation_id, 999999999) AS type_of_cooperation_id,
     COALESCE(rawdonation.type_of_implementation_id, 999999999) AS type_of_implementation_id,
     COALESCE(rawdonation.procurement_system_id, 999999999) AS procurement_system_id,
     

     COALESCE(prim_sect.ent_id, 999999999) AS primary_sector_id,
     COALESCE(sec_sect.ent_id, 999999999) AS secondary_sector_id,
     COALESCE(tert_sect.ent_id, 999999999) AS tertiary_sector_id,

     COALESCE(location.ent_id, 999999999) AS location_id,

     COALESCE(prim_prog.ent_id, 999999999) AS primary_program_id,
     COALESCE(sec_prog.ent_id, 999999999) AS secondary_program_id,
     COALESCE(tert_prog.ent_id, 999999999) AS tertiary_program_id,
     COALESCE(npo_prog.ent_id, 999999999) AS national_objectives_program_id,

     COALESCE(ea.ent_id, 999999999) AS ea_org_id,
     COALESCE(ba.ent_id, 999999999) AS ba_org_id,
     COALESCE(ia.ent_id, 999999999) AS ia_org_id,
     COALESCE(ra.ent_id, 999999999) AS ro_org_id,
     COALESCE(ca.ent_id, 999999999) AS ca_org_id,
     COALESCE(rg.ent_id, 999999999) AS rg_org_id,
     COALESCE(sg.ent_id, 999999999) AS sg_org_id,
     
     COALESCE(rawdonation.component_id, 999999999) AS component_id,
     COALESCE(rawdonation.agreement_id, 999999999) AS agreement_id,
     
     capital_spend_percent AS capital_spend_percent,
     CASE
     	WHEN rawdonation.disaster_response THEN 1
     	WHEN NOT rawdonation.disaster_response THEN 2
     	ELSE 999999999 
     END AS disaster_response_id,
     
    rawdonation.src_role AS src_role,
    rawdonation.dest_role AS dest_role,
    rawdonation.dest_org_id AS dest_org_id,
    'Undefined' AS flow_name, 
    rawdonation.related_entity_id AS related_entity_id
          
	FROM mondrian_raw_donor_transactions rawdonation
    LEFT JOIN etl_activity_sector_primary prim_sect ON prim_sect.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_sector_secondary sec_sect ON sec_sect.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_sector_tertiary tert_sect ON tert_sect.act_id = rawdonation.amp_activity_id
    
    LEFT JOIN etl_activity_program_national_plan_objective npo_prog ON npo_prog.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_program_primary_program prim_prog ON prim_prog.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_program_secondary_program sec_prog ON sec_prog.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_program_tertiary_program tert_prog ON tert_prog.act_id = rawdonation.amp_activity_id

    LEFT JOIN etl_locations location ON location.act_id = rawdonation.amp_activity_id

    LEFT JOIN etl_executing_agencies ea ON ea.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_beneficiary_agencies ba ON ba.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_implementing_agencies ia ON ia.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_responsible_agencies ra ON ra.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_contracting_agencies ca ON ca.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_regional_groups rg ON rg.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_sector_groups sg ON sg.act_id = rawdonation.amp_activity_id

    WHERE (rawdonation.transaction_amount IS NOT NULL) AND (rawdonation.amp_activity_id @@activityIdCondition@@);


INSERT INTO mondrian_fact_table (entity_id, funding_id, entity_internal_id, transaction_type, adjustment_type, transaction_date, date_code,  
  transaction_amount, expenditure_class,
  currency_id, donor_id, 
  financing_instrument_id, terms_of_assistance_id, funding_status_id, mode_of_payment_id, status_id, modality_id, type_of_cooperation_id, type_of_implementation_id, procurement_system_id,
  primary_sector_id, secondary_sector_id, tertiary_sector_id, location_id,
  primary_program_id, secondary_program_id, tertiary_program_id, national_objectives_program_id,
  ea_org_id, ba_org_id, ia_org_id, ro_org_id, ca_org_id, rg_org_id, sg_org_id,
  component_id, agreement_id,
  capital_spend_percent, disaster_response,
  src_role, dest_role, dest_org_id, flow_name,
  related_entity_id)
  SELECT 
	rawdonation.amp_activity_id AS entity_id,
	rawdonation.funding_id AS funding_id,
	rawdonation.amp_fund_detail_id AS entity_internal_id,
    200 + rawdonation.transaction_type AS transaction_type,
    rawdonation.adjustment_type AS adjustment_type,
    rawdonation.transaction_date AS transaction_date,
    rawdonation.date_code AS date_code,

	rawdonation.transaction_amount * (
         COALESCE(location.percentage, 1) *
         COALESCE(prim_prog.percentage, 1) *
         COALESCE(sec_prog.percentage, 1) *
		 COALESCE(tert_prog.percentage, 1) *
         COALESCE(npo_prog.percentage, 1) *
         COALESCE(prim_sect.percentage, 1) *
		 COALESCE(sec_sect.percentage, 1) *
         COALESCE(tert_sect.percentage, 1)
         ) AS transaction_amount,
         
	 rawdonation.expenditure_class AS expenditure_class,
	 
     rawdonation.currency_id AS currency_id,
	 CASE WHEN src_role='DN' THEN rawdonation.originating_org_id ELSE @@BUGCHOOSER@@ END AS donor_id,
	 
     COALESCE(rawdonation.financing_instrument_id, 999999999) AS financing_instrument_id,
     COALESCE(rawdonation.terms_of_assistance_id, 999999999) AS terms_of_assistance_id,
     COALESCE(rawdonation.funding_status_id, 999999999) AS funding_status_id,
     COALESCE(rawdonation.mode_of_payment_id, 999999999) AS mode_of_payment_id,
     COALESCE(rawdonation.status_id, 999999999) AS status_id,
     COALESCE(rawdonation.modality_id, 999999999) AS modality_id,
     COALESCE(rawdonation.type_of_cooperation_id, 999999999) AS type_of_cooperation_id,
     COALESCE(rawdonation.type_of_implementation_id, 999999999) AS type_of_implementation_id,
     COALESCE(rawdonation.procurement_system_id, 999999999) AS procurement_system_id,
     

     COALESCE(prim_sect.ent_id, 999999999) AS primary_sector_id,
     COALESCE(sec_sect.ent_id, 999999999) AS secondary_sector_id,
     COALESCE(tert_sect.ent_id, 999999999) AS tertiary_sector_id,

     COALESCE(location.ent_id, 999999999) AS location_id,

     COALESCE(prim_prog.ent_id, 999999999) AS primary_program_id,
     COALESCE(sec_prog.ent_id, 999999999) AS secondary_program_id,
     COALESCE(tert_prog.ent_id, 999999999) AS tertiary_program_id,
     COALESCE(npo_prog.ent_id, 999999999) AS national_objectives_program_id,

     CASE WHEN src_role='EA' AND rawdonation.originating_org_id IS NOT NULL THEN rawdonation.originating_org_id WHEN dest_role='EA' AND rawdonation.dest_org_id IS NOT NULL THEN rawdonation.dest_org_id ELSE @@BUGCHOOSER@@ END AS ea_org_id,
     CASE WHEN src_role='BA' AND rawdonation.originating_org_id IS NOT NULL THEN rawdonation.originating_org_id WHEN dest_role='BA' AND rawdonation.dest_org_id IS NOT NULL THEN rawdonation.dest_org_id ELSE @@BUGCHOOSER@@ END AS ba_org_id,
     CASE WHEN src_role='IA' AND rawdonation.originating_org_id IS NOT NULL THEN rawdonation.originating_org_id WHEN dest_role='IA' AND rawdonation.dest_org_id IS NOT NULL THEN rawdonation.dest_org_id ELSE @@BUGCHOOSER@@ END AS ia_org_id,
     CASE WHEN src_role='RO' AND rawdonation.originating_org_id IS NOT NULL THEN rawdonation.originating_org_id WHEN dest_role='RO' AND rawdonation.dest_org_id IS NOT NULL THEN rawdonation.dest_org_id ELSE @@BUGCHOOSER@@ END AS ro_org_id,
     CASE WHEN src_role='CA' AND rawdonation.originating_org_id IS NOT NULL THEN rawdonation.originating_org_id WHEN dest_role='CA' AND rawdonation.dest_org_id IS NOT NULL THEN rawdonation.dest_org_id ELSE @@BUGCHOOSER@@ END AS ca_org_id,
     CASE WHEN src_role='RG' AND rawdonation.originating_org_id IS NOT NULL THEN rawdonation.originating_org_id WHEN dest_role='RG' AND rawdonation.dest_org_id IS NOT NULL THEN rawdonation.dest_org_id ELSE @@BUGCHOOSER@@ END AS rg_org_id,
     CASE WHEN src_role='SG' AND rawdonation.originating_org_id IS NOT NULL THEN rawdonation.originating_org_id WHEN dest_role='SG' AND rawdonation.dest_org_id IS NOT NULL THEN rawdonation.dest_org_id ELSE @@BUGCHOOSER@@ END AS sg_org_id,
     
     COALESCE(rawdonation.component_id, 999999999) AS component_id,
     COALESCE(rawdonation.agreement_id, 999999999) AS agreement_id,
     
     capital_spend_percent AS capital_spend_percent,
     CASE
     	WHEN rawdonation.disaster_response THEN 1
     	WHEN NOT rawdonation.disaster_response THEN 2
     	ELSE 999999999 
     END AS disaster_response_id,
    
    rawdonation.src_role AS src_role,
    rawdonation.dest_role AS dest_role,
    rawdonation.dest_org_id AS dest_org_id,
    'Undefined' AS flow_name, 
    rawdonation.related_entity_id AS related_entity_id
          
	FROM mondrian_raw_donor_transactions rawdonation
    LEFT JOIN etl_activity_sector_primary prim_sect ON prim_sect.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_sector_secondary sec_sect ON sec_sect.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_sector_tertiary tert_sect ON tert_sect.act_id = rawdonation.amp_activity_id
    
    LEFT JOIN etl_activity_program_national_plan_objective npo_prog ON npo_prog.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_program_primary_program prim_prog ON prim_prog.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_program_secondary_program sec_prog ON sec_prog.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_program_tertiary_program tert_prog ON tert_prog.act_id = rawdonation.amp_activity_id

    LEFT JOIN etl_locations location ON location.act_id = rawdonation.amp_activity_id

    WHERE (@@directed_disbursements_condition@@) AND (rawdonation.transaction_amount IS NOT NULL) AND (rawdonation.amp_activity_id @@activityIdCondition@@) AND (rawdonation.transaction_type IN (0, 1, 2, 3)) AND (src_role IS NOT NULL) AND (dest_role IS NOT NULL);
    


UPDATE mondrian_fact_table SET flow_name = ((@@src_role@@) || ' - ' || (@@dest_role@@)) 
	WHERE ((transaction_type >= 200) AND (transaction_type <= 215)) AND (src_role IS NOT NULL) AND (dest_role IS NOT NULL) AND (entity_id @@activityIdCondition@@);
