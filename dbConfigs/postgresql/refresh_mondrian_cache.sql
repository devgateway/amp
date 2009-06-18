DROP TABLE IF EXISTS cached_amp_activity;
CREATE TABLE cached_amp_activity AS SELECT * FROM amp_activity;
DROP TABLE IF EXISTS cached_v_donor_date_hierarchy;
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy limit 0;
insert into cached_v_donor_date_hierarchy SELECT * FROM v_donor_date_hierarchy;
DROP TABLE IF EXISTS cached_v_status;
CREATE TABLE cached_v_status AS SELECT * FROM v_status;
DROP TABLE IF EXISTS cached_v_regions CASCADE;
CREATE TABLE cached_v_regions AS SELECT * FROM v_regions_cached;
DROP TABLE IF EXISTS cached_v_primary_program CASCADE;
CREATE TABLE cached_v_primary_program AS SELECT * FROM v_primaryprogram;
DROP TABLE IF EXISTS cached_v_secunday_program CASCADE;
CREATE TABLE cached_v_secunday_program AS SELECT * FROM v_secondaryprogram;
DROP TABLE IF EXISTS cached_v_national_program CASCADE;
CREATE TABLE cached_v_national_program AS SELECT * FROM v_nationalobjectives;
CREATE VIEW "v_donor_funding_cached" AS select f.amp_activity_id AS amp_activity_id,f.amp_funding_id AS amp_funding_id,fd.amp_fund_detail_id AS amp_fund_detail_id,d.name AS donor_name,fd.transaction_type AS transaction_type,fd.adjustment_type AS adjustment_type,fd.transaction_date AS transaction_date,(((((fd.transaction_amount *(coalesce(rc.location_percentage, 100) / 100)) *(coalesce(pp.program_percentage, 100) / 100)) *(coalesce(sp.program_percentage, 100 ) / 100)) *(coalesce(np.program_percentage, 100) / 100)) *(coalesce(sa.sector_percentage,100) /100)) AS transaction_amount,c.currency_code AS currency_code,cval.id AS terms_assist_id, cval.category_value AS terms_assist_name,fd.fixed_exchange_rate AS fixed_exchange_rate,b.org_grp_name AS org_grp_name,ot.org_type AS donor_type_name,cval2.category_value AS financing_instrument_name,cval2.id AS financing_instrument_id,d.amp_org_id AS org_grp_id,ot.amp_org_type_id AS org_type_id,fd.disbursement_order_rejected AS disb_ord_rej,getSectorName(getParentSectorId(s.amp_sector_id)) AS sectorname,getParentSectorId(s.amp_sector_id) AS amp_sector_id,sa.sector_percentage AS sector_percentage,ss.sec_scheme_name AS sec_scheme_name,cc.name AS classification_name,cc.classification_id AS classification_id,ss.amp_sec_scheme_id AS amp_sec_scheme_id,sa.classification_config_id AS sec_act_id,cc.id AS cc_id,s.sector_code AS sector_code,rc.Region AS region,pp.name AS primary_program_name,sp.name AS secundary_program_name,np.name AS national_program_name from amp_funding f join amp_funding_detail fd on f.amp_funding_id = fd.AMP_FUNDING_ID join amp_activity_sector sa on f.amp_activity_id = sa.amp_activity_id join amp_sector s on sa.amp_sector_id = s.amp_sector_id join cached_v_regions rc on f.amp_activity_id = rc.amp_activity_id left join cached_v_primary_program pp on f.amp_activity_id = pp.amp_activity_id left join cached_v_secunday_program sp on f.amp_activity_id =sp.amp_activity_id left join cached_v_national_program np on f.amp_activity_id =np.amp_activity_id,amp_category_value cval,amp_currency c,amp_organisation d,amp_org_group b,amp_org_type ot,amp_category_value cval2,amp_sector_scheme ss,amp_classification_config cc where cval2.id = f.financing_instr_category_value and c.amp_currency_id = fd.amp_currency_id and f.amp_funding_id = fd.AMP_FUNDING_ID and cval.id = f.type_of_assistance_category_va and d.amp_org_id = f.amp_donor_org_id and d.org_grp_id = b.amp_org_grp_id and ot.amp_org_type_id = d.org_type_id and cc.name = 'Primary' and sa.classification_config_id = cc.id and cc.classification_id = ss.amp_sec_scheme_id group by fd.transaction_type, fd.amp_fund_detail_id,f.amp_funding_id,fd.adjustment_type,getParentSectorId(s.amp_sector_id),f.amp_activity_id,cc.id,sa.classification_config_id,ss.amp_sec_scheme_id,cc.classification_id,cc.name,ss.sec_scheme_name,fd.disbursement_order_rejected,ot.amp_org_type_id,d.amp_org_id,cval2.id,cval2.category_value,ot.org_type,b.org_grp_name,fd.fixed_exchange_rate,cval.category_value,cval.id,c.currency_code,sa.sector_percentage,fd.transaction_amount,fd.transaction_date,d.name,s.sector_code,rc.region_id,pp.name,pp.amp_program_id,sp.amp_program_id,np.amp_program_id,rc.location_percentage,pp.program_percentage,sp.program_percentage,np.program_percentage,rc.region,sp.name,np.name order by f.amp_activity_id,getSectorName(getParentSectorId(s.amp_sector_id)),fd.transaction_type,f.amp_funding_id;
DROP TABLE IF EXISTS cached_v_donor_funding;
CREATE TABLE cached_v_donor_funding AS SELECT * FROM v_donor_funding_cached;

CREATE INDEX idx_amp_activity_dh ON cached_v_donor_date_hierarchy(amp_activity_id);
CREATE INDEX idx_fund_det_id_dh ON cached_v_donor_date_hierarchy(amp_fund_detail_id);
CREATE INDEX idx_year_dh ON cached_v_donor_date_hierarchy(year);
CREATE INDEX idx_month_dh ON cached_v_donor_date_hierarchy(month);
CREATE INDEX idx_quarter_dh ON cached_v_donor_date_hierarchy(quarter);
CREATE INDEX idx_amp_activity ON cached_v_donor_funding(amp_activity_id);
CREATE INDEX idx_donor_name ON cached_v_donor_funding(donor_name);
CREATE INDEX idx_curr_code ON cached_v_donor_funding(currency_code);
CREATE INDEX idx_financ_intrum ON cached_v_donor_funding(financing_instrument_name);
CREATE INDEX idx_donor_type ON cached_v_donor_funding(donor_type_name);
CREATE INDEX idx_donor_group ON cached_v_donor_funding(org_grp_name);
CREATE INDEX idx_region_name ON cached_v_donor_funding(Region);
CREATE INDEX idx_pri_prog_name ON cached_v_donor_funding(primary_program_name);
CREATE INDEX idx_sec_prog_name ON cached_v_donor_funding(secundary_program_name);
CREATE INDEX idx_nac_prog_name ON cached_v_donor_funding(national_program_name);


CREATE INDEX idx_amp_activity_st ON cached_v_status(amp_activity_id);
CREATE INDEX idx_st_name_st ON cached_v_status(name);
CREATE INDEX idx_st_id_st ON cached_v_status(amp_status_id);