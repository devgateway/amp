-- intentionally left blank except creating a view and a cached version of it
-- do not port any changes from pre-AMP 2.6 to it, it has to remain this way!
-- the equivalent functionality is in PublicViewColumnsUtil::maintainPublicViewCaches

DROP VIEW IF EXISTS v_donor_funding_cached;
CREATE OR REPLACE VIEW v_donor_funding_cached AS
  select aa.amp_activity_id AS amp_activity_id,
         f.amp_funding_id AS amp_funding_id,
         fd.amp_fund_detail_id AS amp_fund_detail_id,
         fd.transaction_type AS transaction_type,
         fd.adjustment_type AS adjustment_type,
         fd.transaction_date AS transaction_date,
         fd.transaction_amount * (
         
         coalesce(rc.location_percentage, 100) / 100 *
         coalesce(pp.program_percentage, 100) / 100 *
         coalesce(sp.program_percentage, 100) / 100 *
         coalesce(np.program_percentage, 100) / 100 *
         coalesce(secs.sector_percentage,100) / 100 *
         coalesce(s.sector_percentage,100) / 100) AS transaction_amount,
         
         d.name AS donor_name,
         c.currency_code AS currency_code,
         cval.id AS terms_assist_id,
         cval.category_value AS terms_assist_name,
         fd.fixed_exchange_rate AS fixed_exchange_rate,
         b.org_grp_name AS org_grp_name,
         ot.org_type AS donor_type_name,
         cval2.category_value AS financing_instrument_name,
         cval2.id AS financing_instrument_id,
         d.amp_org_id AS org_grp_id,
         ot.amp_org_type_id AS org_type_id,
         fd.disbursement_order_rejected AS disb_ord_rej,
         s.sectorname AS p_sectorname,
         s.subsectorname AS p_sub_sectorname,
         s.subsubsectorname AS p_sub_sub_sectorname,
         s.sec_scheme_name AS p_amp_sec_scheme_name,
         

	 secs.sectorname AS s_sectorname,
         secs.subsectorname AS s_sub_sectorname,
         secs.subsubsectorname AS s_sub_sub_sectorname,
         secs.sec_scheme_name AS s_amp_sec_scheme_name,
         
         
         rc.Region AS region,
          pp.name AS primary_program_name,
          ppl1.name as ppl1_name,
          ppl2.name as ppl2_name,
          ppl3.name as ppl3_name,
          ppl4.name as ppl4_name,
          ppl5.name as ppl5_name,
          ppl6.name as ppl6_name,
          ppl7.name as ppl7_name,
          ppl8.name as ppl8_name,
          sp.name AS secondary_program_name,
          spl1.name as spl1_name,
          spl2.name as spl2_name,
          spl3.name as spl3_name,
          spl4.name as spl4_name,
          spl5.name as spl5_name,
          spl6.name as spl6_name,
          spl7.name as spl7_name,
          spl8.name as spl8_name,

          np.name AS national_program_name,
          npl1.name as npl1_name,
          npl2.name as npl2_name,
          npl3.name as npl3_name,
          npl4.name as npl4_name,
          npl5.name as npl5_name,
          npl6.name as npl6_name,
          npl7.name as npl7_name,
          npl8.name as npl8_name
          
  from cached_amp_activity aa join amp_funding f on aa.amp_activity_id = f.amp_activity_id
       join amp_funding_detail fd on f.amp_funding_id = fd.amp_funding_id
       join amp_category_value cval on f.type_of_assistance_category_va = cval.id
       join amp_currency c on fd.amp_currency_id = c.amp_currency_id
       left join amp_organisation d on f.amp_donor_org_id = d.amp_org_id
       join amp_org_group b on d.org_grp_id = b.amp_org_grp_id
       join amp_org_type ot on b.org_type = ot.amp_org_type_id
       join amp_category_value cval2 on f.financing_instr_category_value = cval2.id

       left join cached_v_m_sectors s on aa.amp_activity_id = s.amp_activity_id

       left join cached_v_m_secondary_sectors secs on aa.amp_activity_id = secs.amp_activity_id
       
       left join amp_sector_scheme ss on secs.amp_sector_scheme_id = ss.amp_sec_scheme_id
       join cached_v_m_regions rc on aa.amp_activity_id = rc.amp_activity_id


       left join cached_v_primaryprogram_level_0 pp on f.amp_activity_id = pp.amp_activity_id
       left join cached_v_primaryprogram_level_1 ppl1 on f.amp_activity_id = ppl1.amp_activity_id
       left join cached_v_primaryprogram_level_2 ppl2 on f.amp_activity_id = ppl2.amp_activity_id
       left join cached_v_primaryprogram_level_3 ppl3 on f.amp_activity_id = ppl3.amp_activity_id
       left join cached_v_primaryprogram_level_4 ppl4 on f.amp_activity_id = ppl4.amp_activity_id
       left join cached_v_primaryprogram_level_5 ppl5 on f.amp_activity_id = ppl5.amp_activity_id
       left join cached_v_primaryprogram_level_6 ppl6 on f.amp_activity_id = ppl6.amp_activity_id
       left join cached_v_primaryprogram_level_7 ppl7 on f.amp_activity_id = ppl7.amp_activity_id
       left join cached_v_primaryprogram_level_8 ppl8 on f.amp_activity_id = ppl8.amp_activity_id

       left join cached_v_secondaryprogram_level_0 sp on f.amp_activity_id = sp.amp_activity_id
       left join cached_v_secondaryprogram_level_1 spl1 on f.amp_activity_id = spl1.amp_activity_id
       left join cached_v_secondaryprogram_level_2 spl2 on f.amp_activity_id = spl2.amp_activity_id
       left join cached_v_secondaryprogram_level_3 spl3 on f.amp_activity_id = spl3.amp_activity_id
       left join cached_v_secondaryprogram_level_4 spl4 on f.amp_activity_id = spl4.amp_activity_id
       left join cached_v_secondaryprogram_level_5 spl5 on f.amp_activity_id = spl5.amp_activity_id
       left join cached_v_secondaryprogram_level_6 spl6 on f.amp_activity_id = spl6.amp_activity_id
       left join cached_v_secondaryprogram_level_7 spl7 on f.amp_activity_id = spl7.amp_activity_id
       left join cached_v_secondaryprogram_level_8 spl8 on f.amp_activity_id = spl8.amp_activity_id

       left join cached_v_nationalobjectives_level_0 np on f.amp_activity_id = np.amp_activity_id
       left join cached_v_nationalobjectives_level_1 npl1 on f.amp_activity_id = npl1.amp_activity_id
       left join cached_v_nationalobjectives_level_2 npl2 on f.amp_activity_id = npl2.amp_activity_id
       left join cached_v_nationalobjectives_level_3 npl3 on f.amp_activity_id = npl3.amp_activity_id
       left join cached_v_nationalobjectives_level_4 npl4 on f.amp_activity_id = npl4.amp_activity_id
       left join cached_v_nationalobjectives_level_5 npl5 on f.amp_activity_id = npl5.amp_activity_id
       left join cached_v_nationalobjectives_level_6 npl6 on f.amp_activity_id = npl6.amp_activity_id
       left join cached_v_nationalobjectives_level_7 npl7 on f.amp_activity_id = npl7.amp_activity_id
       left join cached_v_nationalobjectives_level_8 npl8 on f.amp_activity_id = npl8.amp_activity_id

 group by fd.transaction_type,
           fd.amp_fund_detail_id,
           fd.disbursement_order_rejected,
           fd.adjustment_type,
           fd.transaction_amount,
           fd.transaction_date,
           f.amp_funding_id,
           aa.amp_activity_id,
           s.amp_sector_id,
           s.sectorname,
           secs.amp_sector_id,
           secs.sectorname,
           secs.amp_activity_id,
           ss.amp_sec_scheme_id,
           ot.org_type,
           ot.amp_org_type_id,
           d.amp_org_id,
           cval2.id,
           cval2.category_value,
           cval.category_value,
           cval.id,
           b.org_grp_name,
           fd.fixed_exchange_rate,
           c.currency_code,
           d.name,
           rc.region_id,
           rc.Region,
           rc.amp_activity_id,
           pp.amp_program_id,
      	   ppl2.amp_program_id,
           ppl3.amp_program_id,
           ppl4.amp_program_id,
           ppl5.amp_program_id,
           ppl6.amp_program_id,
           ppl7.amp_program_id,
           ppl8.amp_program_id,
           sp.amp_program_id,
           spl2.amp_program_id,
           spl3.amp_program_id,
           spl4.amp_program_id,
           spl5.amp_program_id,
           spl6.amp_program_id,
           spl7.amp_program_id,
           spl8.amp_program_id,
           np.amp_program_id,
           npl1.amp_program_id,
           npl2.amp_program_id,
           npl3.amp_program_id,
           npl4.amp_program_id,
           npl5.amp_program_id,
           npl6.amp_program_id,
           npl7.amp_program_id,
           npl8.amp_program_id,
           rc.location_percentage,
           pp.program_percentage,
           sp.program_percentage,
           np.program_percentage,
           secs.sector_percentage,
           
           s.sector_percentage,
           s.sec_scheme_name,
	   pp.name ,
	   ppl1.name,
	   ppl2.name,
	   ppl3.name,
           ppl4.name,
           ppl5.name,
           ppl6.name,
           ppl7.name,
           ppl8.name,
           sp.name,
           spl1.name,
           spl2.name,
           spl3.name,
           spl4.name,
           spl5.name,
           spl6.name,
           spl7.name,
           spl8.name,
	   np.name,
           npl1.name,
           npl2.name,
           npl3.name,
           npl4.name,
           npl5.name,
           npl6.name,
           npl7.name,
           npl8.name,
           p_sub_sectorname,
           p_sub_sub_sectorname,
           s_sub_sectorname,
           s_sub_sub_sectorname,
           s_amp_sec_scheme_name
           
  order by aa.amp_activity_id,
           s.sectorname,
           fd.transaction_type,
           f.amp_funding_id;

DROP TABLE IF EXISTS cached_v_m_donor_funding;
CREATE TABLE cached_v_m_donor_funding AS SELECT * FROM v_donor_funding_cached;
DROP VIEW IF EXISTS v_donor_funding_cached;
