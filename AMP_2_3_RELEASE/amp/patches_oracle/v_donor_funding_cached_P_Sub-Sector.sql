begin 
  execute immediate 'drop table cached_v_secunday_program'; 
  exception when others then null; 
end;
/
begin 
  execute immediate 'drop table cached_v_secondary_program'; 
  exception when others then null; 
end;
/
CREATE TABLE cached_v_secondary_program AS SELECT * FROM v_secondaryprogram;

begin 
  execute immediate 'drop table cached_v_national_program'; 
  exception when others then null; 
end;
/
CREATE TABLE cached_v_national_program AS SELECT * FROM v_nationalobjectives_level_0;

begin 
  execute immediate 'drop table cached_v_sub_sector'; 
  exception when others then null; 
end;
/
CREATE TABLE cached_v_sub_sector AS SELECT * FROM v_sub_sectors;

CREATE OR REPLACE VIEW v_donor_funding_cached AS 
select f.amp_activity_id AS amp_activity_id,f.amp_funding_id AS amp_funding_id,fd.amp_fund_detail_id AS amp_fund_detail_id,d.name AS donor_name,
fd.transaction_type AS transaction_type,fd.adjustment_type AS adjustment_type,fd.transaction_date AS transaction_date,
((((((((((fd.transaction_amount * coalesce(rc.location_percentage, 100)) / 100) * coalesce(pp.program_percentage, 100)) / 100) * coalesce(sp.program_percentage, 100)) / 100) * coalesce(np.program_percentage, 100)) / 100) * sa.sector_percentage) /100) AS transaction_amount,
c.currency_code AS currency_code,ccvv.id AS terms_assist_id,ccvv.category_value AS terms_assist_name,
fd.fixed_exchange_rate AS fixed_exchange_rate,b.org_grp_name AS org_grp_name,ot.org_type AS donor_type_name,
ccvv2.category_value AS financing_instfrument_name,ccvv2.id AS financing_instrument_id,d.amp_org_id AS org_grp_id,
ot.amp_org_type_id AS org_type_id,fd.disbursement_order_rejected AS disb_ord_rej,
getSectorName(getParentSectorId(s.amp_sector_id)) AS p_sectorname,getParentSectorId(s.amp_sector_id) AS amp_sector_id,
sa.sector_percentage AS sector_percentage,ss.sec_scheme_name AS sec_scheme_name,cc.name AS classification_name,
cc.classification_id AS classification_id,ss.amp_sec_scheme_id AS amp_sec_scheme_id,sa.classification_config_id AS sec_act_id,
cc.id AS cc_id,s.sector_code AS sector_code,rc.Region AS region,pp.name AS primary_program_name,sp.name AS secondary_program_name,
np.name AS national_program_name,psub.name AS p_sub_sector_name 
from ((((((((((((((((amp_funding f join amp_funding_detail fd on ((f.amp_funding_id = fd.AMP_FUNDING_ID))) join amp_category_value ccvv on (ccvv.id = f.type_of_assistance_category_va) ) 
join amp_currency c on (c.amp_currency_id = fd.amp_currency_id) )left join amp_organisation d on ((f.amp_donor_org_id = d.amp_org_id)))
join amp_org_group b on (d.org_grp_id = b.amp_org_grp_id) )join amp_org_type ot on (ot.amp_org_type_id = d.org_type_id) )
join amp_category_value ccvv2 on (ccvv2.id = f.financing_instr_category_value) ) join amp_activity_sector sa on ((f.amp_activity_id = sa.amp_activity_id))) 
join amp_sector s on ((sa.amp_sector_id = s.amp_sector_id)))join amp_classification_config cc on (sa.classification_config_id = cc.id) ) 
join amp_sector_scheme ss on (cc.classification_id = ss.amp_sec_scheme_id) )join cached_v_regions rc on ((f.amp_activity_id = rc.amp_activity_id)))left join cached_v_primary_program pp 
on ((f.amp_activity_id =pp.amp_activity_id)))left join cached_v_secondary_program sp on ((f.amp_activity_id =sp.amp_activity_id)))
left join cached_v_national_program np on ((f.amp_activity_id =np.amp_activity_id)))
left join cached_v_sub_sector psub on ((s.amp_sector_id = psub.amp_sector_id)))where ( 
(f.amp_funding_id = fd.AMP_FUNDING_ID) 
and (d.amp_org_id = f.amp_donor_org_id)  and (cc.name = 'Primary') 
 ) 
group by fd.transaction_type,fd.amp_fund_detail_id,f.amp_funding_id,fd.adjustment_type,getParentSectorId(s.amp_sector_id),
f.amp_activity_id,cc.id,sa.classification_config_id,ss.amp_sec_scheme_id,cc.classification_id,cc.name,ss.sec_scheme_name,
fd.disbursement_order_rejected,ot.amp_org_type_id,d.amp_org_id,ccvv2.id,ccvv2.category_value,ot.org_type,b.org_grp_name,
fd.fixed_exchange_rate,ccvv.category_value,ccvv.id,c.currency_code,sa.sector_percentage,
fd.transaction_amount,fd.transaction_date,d.name,s.sector_code,rc.region_id,
pp.name,pp.amp_program_id,sp.amp_program_id,np.amp_program_id,psub.amp_sector_id,psub.name ,
rc.location_percentage,pp.program_percentage, sp.program_percentage, np.program_percentage, rc.Region, sp.name, np.name, sa.amp_sector_id
order by f.amp_activity_id,getSectorName(getParentSectorId(sa.amp_sector_id)),fd.transaction_type,f.amp_funding_id;

