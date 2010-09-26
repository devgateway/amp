update amp_columns set columnName='National Planning Objectives Level 6' where extractorView='v_nationalobjectives_level_6';
update amp_columns set columnName='Primary Program Level 6' where extractorView='v_primaryprogram_level_6';
update amp_columns set columnName='Secondary Program Level 6' where extractorView='v_secondaryprogram_level_6'; 
CREATE OR REPLACE  VIEW v_donor_funding AS
  select
    f.amp_activity_id AS amp_activity_id,
    f.amp_funding_id AS amp_funding_id,
    fd.amp_fund_detail_id AS amp_fund_detail_id,
    d.name AS donor_name,
    fd.transaction_type AS transaction_type,
    fd.adjustment_type AS adjustment_type,
    fd.transaction_date AS transaction_date,
    fd.transaction_amount AS transaction_amount,
    c.currency_code AS currency_code,
    cval.id AS terms_assist_id,
    cval.category_value AS terms_assist_name,
    fd.fixed_exchange_rate AS fixed_exchange_rate,
    b.org_grp_name AS org_grp_name,
    ot.org_type AS donor_type_name,
    cval2.category_value AS financing_instrument_name,
    cval2.id AS financing_instrument_id,
    d.amp_org_id AS org_id,
    d.org_grp_id AS org_grp_id,
    ot.amp_org_type_id AS org_type_id
  from
  amp_funding f, amp_funding_detail fd, amp_category_value cval,amp_currency c,amp_organisation d,amp_org_group b,amp_org_type ot,amp_category_value cval2
  where
    ((cval2.id = f.financing_instr_category_value) and (c.amp_currency_id = fd.amp_currency_id) and (f.amp_funding_id = fd.amp_funding_id) and (cval.id = f.type_of_assistance_category_va) and (d.amp_org_id = f.amp_donor_org_id) and (d.org_grp_id = b.amp_org_grp_id) and (ot.amp_org_type_id = d.org_type_id))
  order by
    f.amp_activity_id;
TRUNCATE TABLE amp_columns_filters;
INSERT INTO amp_columns_filters VALUES 
(1,getReportColumnId('Primary Sector'),'selectedSecondarySectors','amp_sector_id');
INSERT INTO amp_columns_filters VALUES 
(2,getReportColumnId('National Planning Objectives'),'selectedNatPlanObj','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(3,getReportColumnId('Primary Program'),'selectedPrimaryPrograms','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(4,getReportColumnId('Secondary Program'),'selectedSecondaryPrograms','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(5,getReportColumnId('Region'),'regionSelected','region_id');
INSERT INTO amp_columns_filters VALUES 
(6,getReportColumnId('Executing Agency'),'executingAgency','amp_org_id');
INSERT INTO amp_columns_filters VALUES 
(7,getReportColumnId('Implementing Agency'),'implementingAgency','amp_org_id');
INSERT INTO amp_columns_filters VALUES 
(8,getReportColumnId('Beneficiary Agency'),'beneficiaryAgency','amp_org_id');
INSERT INTO amp_columns_filters VALUES 
(9,getReportColumnId('Financing Instrument'),'financingInstruments','amp_modality_id');
INSERT INTO amp_columns_filters VALUES 
(10,getReportColumnId('Type Of Assistance'),'typeOfAssistance','terms_assist_code');
INSERT INTO amp_columns_filters VALUES 
(11,getReportColumnId('Donor Group'),'donorGroups','amp_org_grp_id');
INSERT INTO amp_columns_filters VALUES 
(12,getReportColumnId('Donor Type'),'donorTypes','org_type_id');
INSERT INTO amp_columns_filters VALUES 
(13,getReportColumnId('Donor Agency'),'donorGroups','org_grp_id');
INSERT INTO amp_columns_filters VALUES 
(14,getReportColumnId('Donor Agency'),'donorTypes','org_type_id');
INSERT INTO amp_columns_filters VALUES 
(15,getReportColumnId('Primary Sector'),'selectedSectors','amp_sector_id');
INSERT INTO amp_columns_filters VALUES 
(16,getReportColumnId('Donor Agency'),'donnorgAgency','amp_donor_org_id');
INSERT INTO amp_columns_filters VALUES 
(17,getReportColumnId('National Planning Objectives'),'relatedNatPlanObjs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(18,getReportColumnId('National Planning Objectives Level 1'),'relatedNatPlanObjs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(19,getReportColumnId('National Planning Objectives Level 2'),'relatedNatPlanObjs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(20,getReportColumnId('National Planning Objectives Level 3'),'relatedNatPlanObjs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(21,getReportColumnId('National Planning Objectives Level 4'),'relatedNatPlanObjs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(22,getReportColumnId('National Planning Objectives Level 5'),'relatedNatPlanObjs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(23,getReportColumnId('National Planning Objectives Level 6'),'relatedNatPlanObjs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(24,getReportColumnId('National Planning Objectives Level 7'),'relatedNatPlanObjs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(25,getReportColumnId('National Planning Objectives Level 8'),'relatedNatPlanObjs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(26,getReportColumnId('Primary Program'),'relatedPrimaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(27,getReportColumnId('Primary Program Level 1'),'relatedPrimaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(28,getReportColumnId('Primary Program Level 2'),'relatedPrimaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(29,getReportColumnId('Primary Program Level 3'),'relatedPrimaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(30,getReportColumnId('Primary Program Level 4'),'relatedPrimaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(31,getReportColumnId('Primary Program Level 5'),'relatedPrimaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(32,getReportColumnId('Primary Program Level 6'),'relatedPrimaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(33,getReportColumnId('Primary Program Level 7'),'relatedPrimaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(34,getReportColumnId('Primary Program Level 8'),'relatedPrimaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(35,getReportColumnId('Secondary Program'),'relatedSecondaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(36,getReportColumnId('Secondary Program Level 1'),'relatedSecondaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(37,getReportColumnId('Secondary Program Level 2'),'relatedSecondaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(38,getReportColumnId('Secondary Program Level 3'),'relatedSecondaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(39,getReportColumnId('Secondary Program Level 4'),'relatedSecondaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(40,getReportColumnId('Secondary Program Level 5'),'relatedSecondaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(41,getReportColumnId('Secondary Program Level 6'),'relatedSecondaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(42,getReportColumnId('Secondary Program Level 7'),'relatedSecondaryProgs','amp_program_id');
INSERT INTO amp_columns_filters VALUES 
(43,getReportColumnId('Secondary Program Level 8'),'relatedSecondaryProgs','amp_program_id');