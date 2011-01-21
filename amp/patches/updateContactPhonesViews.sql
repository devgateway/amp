create or replace view `v_donor_cont_phone` AS (
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='DONOR_CONT' 
);

create or replace view `v_mofed_cont_phone` AS (
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='MOFED_CONT' 
);

create or replace view `v_sect_min_cont_phone` AS (
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='SECTOR_MINISTRY_CONT' 
);

create or replace view `v_proj_coordr_cont_phone` AS (
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='PROJ_COORDINATOR_CONT' 
);

create or replace view `v_impl_ex_cont_phone` AS ( 
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='IMPL_EXEC_AGENCY_CONT' 
);