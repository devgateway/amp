  INSERT INTO mondrian_fact_table 
  select 
	'A' as entity_type,
	aa.amp_activity_id AS entity_id,
	fd.amp_fund_detail_id AS entity_internal_id,
    fd.transaction_type AS transaction_type,
    fd.adjustment_type AS adjustment_type,
    fd.transaction_date AS transaction_date,

	fd.transaction_amount * (
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
         COALESCE(ea.percentage, 1)
         ) AS transaction_amount,

     fd.amp_currency_id AS currency_id,
	 f.amp_donor_org_id AS donor_id,
     f.financing_instr_category_value AS financing_instrument_id,
     f.type_of_assistance_category_va AS terms_of_assistance_id,

     prim_sect.ent_id AS primary_sector_id,
     sec_sect.ent_id AS secondary_sector_id,
     tert_sect.ent_id AS tertiary_sector_id,

     location.ent_id AS location_id,

     prim_prog.ent_id AS primary_program_id,
     sec_prog.ent_id AS secondary_program_id,
     tert_prog.ent_id AS tertiary_program_id,
     npo_prog.ent_id AS national_objectives_program_id,

     ea.ent_id AS ea_org_id,
     ba.ent_id AS ba_org_id,
     ia.ent_id AS ia_org_id,
     ra.ent_id AS ro_org_id,
     
     NULL as src_role_id,
     NULL as dest_role_id,
     NULL as dest_org_id
          
  from amp_activity aa join amp_funding f on aa.amp_activity_id = f.amp_activity_id
    join amp_funding_detail fd on f.amp_funding_id = fd.amp_funding_id
    LEFT JOIN etl_activity_sector_primary prim_sect ON prim_sect.act_id = aa.amp_activity_id
    LEFT JOIN etl_activity_sector_secondary sec_sect ON sec_sect.act_id = aa.amp_activity_id
    LEFT JOIN etl_activity_sector_tertiary tert_sect ON tert_sect.act_id = aa.amp_activity_id
    
    LEFT JOIN etl_activity_program_national_plan_objective npo_prog ON npo_prog.act_id = aa.amp_activity_id
    LEFT JOIN etl_activity_program_primary_program prim_prog ON prim_prog.act_id = aa.amp_activity_id
    LEFT JOIN etl_activity_program_secondary_program sec_prog ON sec_prog.act_id = aa.amp_activity_id
    LEFT JOIN etl_activity_program_tertiary_program tert_prog ON tert_prog.act_id = aa.amp_activity_id

    LEFT JOIN etl_locations location ON location.act_id = aa.amp_activity_id

    LEFT JOIN etl_executing_agencies ea ON ea.act_id = aa.amp_activity_id
    LEFT JOIN etl_beneficiary_agencies ba ON ba.act_id = aa.amp_activity_id
    LEFT JOIN etl_implementing_agencies ia ON ia.act_id = aa.amp_activity_id
    LEFT JOIN etl_responsible_agencies ra ON ra.act_id = aa.amp_activity_id

order by aa.amp_activity_id;
