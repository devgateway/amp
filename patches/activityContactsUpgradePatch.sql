/**add temporary columns to table*/
alter table amp_contact 
add (activity_id bigint(20),email varchar(50), phone varchar(50), fax varchar(50),contact_type varchar(50));


/*insert data in amp_contact*/
insert into amp_contact (name,lastName,email,function,organisation_name,phone,fax,is_shared,creator_id,activity_id,contact_type) 
(
select 
cont_first_name,cont_last_name,email,dnr_cont_title, dnr_cont_organization,
dnr_cont_phone_number,dnr_cont_fax_number ,true,activity_creator,amp_activity_id,
'DONOR_CONT' from amp_activity where (cont_first_name is not null and cont_first_name!='') 
or (cont_last_name is not null and cont_last_name!='') 
or (email is not null and email!='') 
or (dnr_cont_title is not null and dnr_cont_title!='') 
or (dnr_cont_organization is not null and dnr_cont_organization!='') 
or (dnr_cont_phone_number is not null and dnr_cont_phone_number!='') 
or (dnr_cont_fax_number is not null and dnr_cont_fax_number!='') 
)

UNION 

(
select mofed_cnt_first_name,mofed_cnt_last_name,mofed_cnt_email,mfd_cont_title,
mfd_cont_organization,mfd_cont_phone_number,mfd_cont_fax_number,true,
activity_creator,amp_activity_id, 'MOFED_CONT' from amp_activity 
where (mofed_cnt_first_name is not null and mofed_cnt_first_name!='') 
or (mofed_cnt_last_name is not null and mofed_cnt_last_name!='') 
or (mofed_cnt_email is not null and mofed_cnt_email!='') 
or (mfd_cont_title is not null and mfd_cont_title!='') 
or (mfd_cont_organization is not null and mfd_cont_organization!='') 
or (mfd_cont_phone_number is not null and mfd_cont_phone_number!='') 
or (mfd_cont_fax_number is not null and mfd_cont_fax_number!='') 
)

UNION

(
select prj_co_first_name,prj_co_last_name,prj_co_email,prj_co_title,prj_co_organization,
prj_co_phone_number,prj_co_fax_number,true,activity_creator,amp_activity_id,
'PROJ_COORDINATOR_CONT' from amp_activity 
where (prj_co_first_name is not null and prj_co_first_name!='') 
or (prj_co_last_name is not null and prj_co_last_name!='') 
or (prj_co_email is not null and prj_co_email!='') 
or (prj_co_title is not null and prj_co_title!='') 
or (prj_co_organization is not null and prj_co_organization!='') 
or (prj_co_phone_number is not null and prj_co_phone_number!='') 
or (prj_co_fax_number is not null and prj_co_fax_number!='') 
)

UNION

(
select sec_min_first_name,sec_min_last_name,sec_min_email,sec_min_title,
sec_min_organization,sec_min_phone_number,sec_min_fax_number,
true,activity_creator,amp_activity_id,'SECTOR_MINISTRY_CONT' from amp_activity 
where (sec_min_first_name is not null and sec_min_first_name!='') 
or (sec_min_last_name is not null and sec_min_last_name!='') 
or (sec_min_email is not null and sec_min_email!='') 
or (sec_min_title is not null and sec_min_title!='') 
or (sec_min_organization is not null and sec_min_organization!='') 
or (sec_min_phone_number is not null and sec_min_phone_number!='') 
or (sec_min_fax_number is not null and sec_min_fax_number!='')
)
;

/*fill amp_contact_property*/
insert into amp_contact_properties(name,value,contact_id) 
(select 'contact email',email,contact_id from amp_contact ac where 
(select count(contact_id) from amp_contact_properties where contact_id=ac.contact_id and value=ac.email and name = 'contact email')=0 );

insert into amp_contact_properties(name,value,contact_id) 
(select 'contact phone',concat('0 ',phone),contact_id from amp_contact ac where 
(select count(contact_id) from amp_contact_properties where contact_id=ac.contact_id and value=ac.phone and name = 'contact phone')=0 );

insert into amp_contact_properties(name,value,contact_id) 
(select 'contact fax',fax,contact_id from amp_contact ac where 
(select count(contact_id) from amp_contact_properties where contact_id=ac.contact_id and value=ac.fax and name = 'contact fax')=0 );

delete from amp_contact_properties where value is null or trim(value)='' or (name='contact phone' and value='0 ');

/*fill amp_activity_contact*/
insert into amp_activity_contact(contact_type,activity_id,contact_id,is_primary_contact) 
select contact_type,activity_id,contact_id,true from amp_contact ac where 
(select count(activity_contact_id) from amp_activity_contact where activity_id=ac.activity_id and contact_id=ac.contact_id and contact_type=ac.contact_type and is_primary_contact is true)=0;

/*drop unused columns*/
alter table amp_contact drop column activity_id;
alter table amp_contact drop column contact_type;
alter table amp_contact drop column email;
alter table amp_contact drop column phone;
alter table amp_contact drop column fax;

/*REPORTS*/

/*donor contact*/
create or replace view `v_donor_cont_name` 
AS (
select ac.activity_id as amp_activity_id, concat_ws(' ',c.name,c.lastname) as contact from amp_activity_contact ac join 
amp_contact c where ac.contact_id=c.contact_id and ac.contact_type='DONOR_CONT' and  trim(concat_ws(' ',c.name,c.lastname))!="" 
);

create or replace view `v_donor_cont_email` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as email  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='DONOR_CONT' and  cp.name='contact email'  
and trim(cp.value)!="");

 
create or replace view `v_donor_cont_org` AS 
(select ac.activity_id  as amp_activity_id,c.organisation_name as org from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='DONOR_CONT' and  trim(c.organisation_name)!=' ') 
	UNION 
(select ac.activity_id as amp_activity_id ,org.name as org from amp_activity_contact ac join amp_org_contact oc join amp_organisation org 
where ac.contact_id=oc.contact_id and oc.amp_org_id=org.amp_org_id and ac.contact_type='DONOR_CONT');


create or replace view `v_donor_cont_phone` AS (
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='DONOR_CONT' 
);

create or replace view `v_donor_cont_fax` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as fax  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='DONOR_CONT' and  cp.name='contact fax'  
and trim(cp.value)!="");

create or replace view `v_donor_cont_title` AS 
(select ac.activity_id  as amp_activity_id,c.function as function from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='DONOR_CONT' and  trim(c.function)!=' ');

/*mofed contact*/

create or replace view `v_mofed_cont_name` 
AS (
select ac.activity_id as amp_activity_id, concat_ws(' ',c.name,c.lastname) as contact from amp_activity_contact ac join 
amp_contact c where ac.contact_id=c.contact_id and ac.contact_type='MOFED_CONT' and  trim(concat_ws(' ',c.name,c.lastname))!="" 
);

create or replace view `v_mofed_cont_email` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as email  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='MOFED_CONT' and  cp.name='contact email'  
and trim(cp.value)!="");

 
create or replace view `v_mofed_cont_org` AS 
(select ac.activity_id  as amp_activity_id,c.organisation_name as org from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='MOFED_CONT' and  trim(c.organisation_name)!=' ') 
	UNION 
(select ac.activity_id  as amp_activity_id,org.name as org from amp_activity_contact ac join amp_org_contact oc join amp_organisation org 
where ac.contact_id=oc.contact_id and oc.amp_org_id=org.amp_org_id and ac.contact_type='MOFED_CONT');


create or replace view `v_mofed_cont_phone` AS (
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='MOFED_CONT' 
);

create or replace view `v_mofed_cont_fax` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as fax  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='MOFED_CONT' and  cp.name='contact fax'  
and trim(cp.value)!="");

create or replace view `v_mofed_cont_title` AS 
(select ac.activity_id  as amp_activity_id,c.function as function from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='MOFED_CONT' and  trim(c.function)!=' ');

/*sector ministry*/

create or replace view `v_sect_min_cont_name` 
AS (
select ac.activity_id as amp_activity_id, concat_ws(' ',c.name,c.lastname) as contact from amp_activity_contact ac join 
amp_contact c where ac.contact_id=c.contact_id and ac.contact_type='SECTOR_MINISTRY_CONT' and  trim(concat_ws(' ',c.name,c.lastname))!="" 
);

create or replace view `v_sect_min_cont_email` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as email  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='SECTOR_MINISTRY_CONT' and  cp.name='contact email'  
and trim(cp.value)!="");

 
create or replace view `v_sect_min_cont_org` AS 
(select ac.activity_id  as amp_activity_id,c.organisation_name as org from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='SECTOR_MINISTRY_CONT' and  trim(c.organisation_name)!=' ') 
	UNION 
(select ac.activity_id  as amp_activity_id,org.name as org from amp_activity_contact ac join amp_org_contact oc join amp_organisation org 
where ac.contact_id=oc.contact_id and oc.amp_org_id=org.amp_org_id and ac.contact_type='SECTOR_MINISTRY_CONT');


create or replace view `v_sect_min_cont_phone` AS (
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='SECTOR_MINISTRY_CONT' 
);

create or replace view `v_sect_min_cont_fax` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as fax  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='SECTOR_MINISTRY_CONT' and  cp.name='contact fax'  
and trim(cp.value)!="");

create or replace view `v_sect_min_cont_title` AS 
(select ac.activity_id  as amp_activity_id,c.function as function from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='SECTOR_MINISTRY_CONT' and  trim(c.function)!=' ');

/*project coordinator*/
create or replace view `v_proj_coordr_cont_name` 
AS (
select ac.activity_id as amp_activity_id, concat_ws(' ',c.name,c.lastname) as contact from amp_activity_contact ac join 
amp_contact c where ac.contact_id=c.contact_id and ac.contact_type='PROJ_COORDINATOR_CONT' and  trim(concat_ws(' ',c.name,c.lastname))!="" 
);

create or replace view `v_proj_coordr_cont_email` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as email  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='PROJ_COORDINATOR_CONT' and  cp.name='contact email'  
and trim(cp.value)!="");

 
create or replace view `v_proj_coordr_cont_org` AS 
(select ac.activity_id  as amp_activity_id,c.organisation_name as org from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='PROJ_COORDINATOR_CONT' and  trim(c.organisation_name)!=' ') 
	UNION 
(select ac.activity_id  as amp_activity_id,org.name as org from amp_activity_contact ac join amp_org_contact oc join amp_organisation org 
where ac.contact_id=oc.contact_id and oc.amp_org_id=org.amp_org_id and ac.contact_type='PROJ_COORDINATOR_CONT');


create or replace view `v_proj_coordr_cont_phone` AS (
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='PROJ_COORDINATOR_CONT' 
);

create or replace view `v_proj_coordr_cont_fax` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as fax  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='PROJ_COORDINATOR_CONT' and  cp.name='contact fax'  
and trim(cp.value)!="");

create or replace view `v_proj_coordr_cont_title` AS 
(select ac.activity_id  as amp_activity_id,c.function as function from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='PROJ_COORDINATOR_CONT' and  trim(c.function)!=' ');

/*impl-exec agency*/

/*impl/executing aency contact columns*/
insert into amp_columns (columnName, cellType, extractorView) 
(select 'Implementing/Executing Agency Contact Name','org.dgfoundation.amp.ar.cell.TextCell','v_impl_ex_cont_name' 
from dual where (select count(columnId) from amp_columns where extractorView="v_impl_ex_cont_name")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Implementing/Executing Agency Contact Email','org.dgfoundation.amp.ar.cell.TextCell','v_impl_ex_cont_email' 
from dual where (select count(columnId) from amp_columns where extractorView="v_impl_ex_cont_email")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Implementing/Executing Agency Contact Organization','org.dgfoundation.amp.ar.cell.TextCell','v_impl_ex_cont_org' 
from dual where (select count(columnId) from amp_columns where extractorView="v_impl_ex_cont_org")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Implementing/Executing Agency Contact Phone','org.dgfoundation.amp.ar.cell.TextCell','v_impl_ex_cont_phone' 
from dual where (select count(columnId) from amp_columns where extractorView="v_impl_ex_cont_phone")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Implementing/Executing Agency Contact Fax','org.dgfoundation.amp.ar.cell.TextCell','v_impl_ex_cont_fax' 
from dual where (select count(columnId) from amp_columns where extractorView="v_impl_ex_cont_fax")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Implementing/Executing Agency Contact Title','org.dgfoundation.amp.ar.cell.TextCell','v_impl_ex_cont_title'  
from dual where (select count(columnId) from amp_columns where extractorView="v_impl_ex_cont_title")=0);


/*create views*/
create or replace view `v_impl_ex_cont_name` 
AS (
select ac.activity_id as amp_activity_id, concat_ws(' ',c.name,c.lastname) as contact from amp_activity_contact ac join 
amp_contact c where ac.contact_id=c.contact_id and ac.contact_type='IMPL_EXEC_AGENCY_CONT' and  trim(concat_ws(' ',c.name,c.lastname))!="" 
);

create or replace view `v_impl_ex_cont_email` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as email  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='IMPL_EXEC_AGENCY_CONT' and  cp.name='contact email'  
and trim(cp.value)!="");

 
create or replace view `v_impl_ex_cont_org` AS 
(select ac.activity_id  as amp_activity_id,c.organisation_name as org from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='IMPL_EXEC_AGENCY_CONT' and  trim(c.organisation_name)!=' ') 
	UNION 
(select ac.activity_id  as amp_activity_id,org.name as org from amp_activity_contact ac join amp_org_contact oc join amp_organisation org 
where ac.contact_id=oc.contact_id and oc.amp_org_id=org.amp_org_id and ac.contact_type='IMPL_EXEC_AGENCY_CONT');


create or replace view `v_impl_ex_cont_phone` AS ( 
select ac.activity_id as amp_activity_id,COALESCE( concat((select category_value from amp_category_value where id=substr(cp.value,1,locate(' ', cp.value)) ),' ',substr(cp.value,locate(' ', cp.value)) )  , substr(cp.value,locate(' ', cp.value))) as phone 
from amp_activity_contact ac join amp_contact_properties cp where ac.contact_id=cp.contact_id AND cp.name='contact phone' and ac.contact_type='IMPL_EXEC_AGENCY_CONT' 
);

create or replace view `v_impl_ex_cont_fax` 
AS ( 
select ac.activity_id as amp_activity_id, cp.value as fax  from amp_activity_contact ac join 
amp_contact_properties cp where ac.contact_id=cp.contact_id and ac.contact_type='IMPL_EXEC_AGENCY_CONT' and  cp.name='contact fax'  
and trim(cp.value)!="");

create or replace view `v_impl_ex_cont_title` AS 
(select ac.activity_id  as amp_activity_id,c.function as function from amp_activity_contact ac join amp_contact c 
where ac.contact_id=c.contact_id and ac.contact_type='IMPL_EXEC_AGENCY_CONT' and  trim(c.function)!=' ');

/*remove activity columns*/
alter table amp_activity drop column cont_first_name;
alter table amp_activity drop column cont_last_name;
alter table amp_activity drop column email;
alter table amp_activity drop column dnr_cont_title;
alter table amp_activity drop column dnr_cont_organization;
alter table amp_activity drop column dnr_cont_phone_number;
alter table amp_activity drop column dnr_cont_fax_number;

alter table amp_activity drop column mofed_cnt_first_name;
alter table amp_activity drop column mofed_cnt_last_name;
alter table amp_activity drop column mofed_cnt_email;
alter table amp_activity drop column mfd_cont_title;
alter table amp_activity drop column mfd_cont_organization;
alter table amp_activity drop column mfd_cont_phone_number;
alter table amp_activity drop column mfd_cont_fax_number;

alter table amp_activity drop column prj_co_first_name;
alter table amp_activity drop column prj_co_last_name;
alter table amp_activity drop column prj_co_email;
alter table amp_activity drop column prj_co_title;
alter table amp_activity drop column prj_co_organization;
alter table amp_activity drop column prj_co_phone_number;
alter table amp_activity drop column prj_co_fax_number;

alter table amp_activity drop column sec_min_first_name;
alter table amp_activity drop column sec_min_last_name;
alter table amp_activity drop column sec_min_email;
alter table amp_activity drop column sec_min_title;
alter table amp_activity drop column sec_min_organization;
alter table amp_activity drop column sec_min_phone_number;
alter table amp_activity drop column sec_min_fax_number;