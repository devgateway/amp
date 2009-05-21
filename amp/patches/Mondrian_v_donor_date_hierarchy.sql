CREATE OR REPLACE VIEW v_donor_date_hierarchy AS

 SELECT a.amp_activity_id,
        fd.amp_fund_detail_id as amp_fund_detail_id,
        fd.transaction_date as full_date,
        year (fd.transaction_date) as year,
        month (fd.transaction_date) as month,
        CAST(monthname(fd.transaction_date) AS CHAR) as month_name,
        quarter (fd.transaction_date) as quarter,
        concat('Q', CAST(quarter (fd.transaction_date) AS CHAR)) as quarter_name
 FROM amp_activity a,
      amp_funding f,
      amp_funding_detail fd
 WHERE a.amp_activity_id = f.amp_activity_id AND
       f.amp_funding_id = fd.amp_funding_id;