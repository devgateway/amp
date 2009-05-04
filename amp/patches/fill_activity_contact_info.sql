/**add temporary columns to table*/
alter table amp_contact 
add (activity_id bigint(20), contact_type varchar(50));
/**donor contact info*/
insert into amp_contact (name,lastName,email,title,organisation_name,phone,fax,is_shared,creator_id,activity_id,contact_type) 
select cont_first_name,cont_last_name,email,dnr_cont_title,dnr_cont_organization,dnr_cont_phone_number,dnr_cont_fax_number,true,activity_creator,amp_activity_id,'DONOR_CONT' 
from amp_activity where (cont_first_name is not null and cont_first_name!='') or 
(cont_last_name is not null and cont_last_name!='') or (email is not null and email!='') or 
(dnr_cont_title is not null and dnr_cont_title!='') or (dnr_cont_organization is not null and dnr_cont_organization!='') 
or (dnr_cont_phone_number is not null and dnr_cont_phone_number!='') or (dnr_cont_fax_number is not null and dnr_cont_fax_number!='');

/**mofed contact info*/
insert into  amp_contact(name,lastName,email,title,organisation_name,phone,fax,is_shared,creator_id,activity_id,contact_type) 
select mofed_cnt_first_name,mofed_cnt_last_name,mofed_cnt_email,mfd_cont_title,mfd_cont_organization,mfd_cont_phone_number,mfd_cont_fax_number,true,activity_creator,amp_activity_id,'MOFED_CONT' from amp_activity 
where (mofed_cnt_first_name is not null and mofed_cnt_first_name!='') or (mofed_cnt_last_name is not null and mofed_cnt_last_name!='') 
 or (mofed_cnt_email is not null and mofed_cnt_email!='') or (mfd_cont_title is not null and mfd_cont_title!='') or 
 (mfd_cont_organization is not null and mfd_cont_organization!='') or (mfd_cont_phone_number is not null and mfd_cont_phone_number!='') 
 or (mfd_cont_fax_number is not null and mfd_cont_fax_number!='');


/**Project Coordinator contact info*/
insert into  amp_contact(name,lastName,email,title,organisation_name,phone,fax,is_shared,creator_id,activity_id,contact_type) 
select prj_co_first_name,prj_co_last_name,prj_co_email,prj_co_title,prj_co_organization,prj_co_phone_number,prj_co_fax_number,true,activity_creator,amp_activity_id,'PROJ_COORDINATOR_CONT' from amp_activity 
where (prj_co_first_name is not null and prj_co_first_name!='') or (prj_co_last_name is not null and prj_co_last_name!='') 
or (prj_co_email is not null and prj_co_email!='') or (prj_co_title is not null and prj_co_title!='') or  
(prj_co_organization is not null and prj_co_organization!='') or (prj_co_phone_number is not null and prj_co_phone_number!='') 
or (prj_co_fax_number is not null and prj_co_fax_number!='');


/**sector ministry contact info*/
insert into  amp_contact(name,lastName,email,title,organisation_name,phone,fax,is_shared,creator_id,activity_id,contact_type) 
select sec_min_first_name,sec_min_last_name,sec_min_email,sec_min_title,sec_min_organization,sec_min_phone_number,sec_min_fax_number,true,activity_creator,amp_activity_id,'SECTOR_MINISTRY_CONT' from amp_activity 
where (sec_min_first_name is not null and sec_min_first_name!='') or (sec_min_last_name is not null and sec_min_last_name!='') 
or (sec_min_email is not null and sec_min_email!='') or (sec_min_title is not null and sec_min_title!='') or 
(sec_min_organization is not null and sec_min_organization!='') or (sec_min_phone_number is not null and sec_min_phone_number!='') 
or (sec_min_fax_number is not null and sec_min_fax_number!='');

/**fill activity_contact table*/
insert into amp_activity_contact (activity_id,contact_id,contact_type,is_primary_contact) 
select activity_id,contact_id,contact_type,true from amp_contact;

/**remove temporary columns*/
alter table amp_contact 
drop column activity_id;

alter table amp_contact 
drop column contact_type;