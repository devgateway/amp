/*donor contact columns*/
insert into amp_columns (columnName, cellType, extractorView) 
(select 'Donor Contact Name','org.dgfoundation.amp.ar.cell.TextCell','v_donor_cont_name' 
from dual where (select count(columnId) from amp_columns where extractorView="v_donor_cont_name")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Donor Contact Email','org.dgfoundation.amp.ar.cell.TextCell','v_donor_cont_email' 
from dual where (select count(columnId) from amp_columns where extractorView="v_donor_cont_email")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Donor Contact Organization','org.dgfoundation.amp.ar.cell.TextCell','v_donor_cont_org' 
from dual where (select count(columnId) from amp_columns where extractorView="v_donor_cont_org")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Donor Contact Phone','org.dgfoundation.amp.ar.cell.TextCell','v_donor_cont_phone' 
from dual where (select count(columnId) from amp_columns where extractorView="v_donor_cont_phone")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Donor Contact Fax','org.dgfoundation.amp.ar.cell.TextCell','v_donor_cont_fax' 
from dual where (select count(columnId) from amp_columns where extractorView="v_donor_cont_fax")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Donor Contact Title','org.dgfoundation.amp.ar.cell.TextCell','v_donor_cont_title' 
from dual where (select count(columnId) from amp_columns where extractorView="v_donor_cont_title")=0);

/*mofed contact columns*/
insert into amp_columns (columnName, cellType, extractorView) 
(select 'Ministry Of Finance Contact Name','org.dgfoundation.amp.ar.cell.TextCell','v_mofed_cont_name' 
from dual where (select count(columnId) from amp_columns where extractorView="v_mofed_cont_name")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Ministry Of Finance Contact Email','org.dgfoundation.amp.ar.cell.TextCell','v_mofed_cont_email' 
from dual where (select count(columnId) from amp_columns where extractorView="v_mofed_cont_email")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Ministry Of Finance Contact Organization','org.dgfoundation.amp.ar.cell.TextCell','v_mofed_cont_org' 
from dual where (select count(columnId) from amp_columns where extractorView="v_mofed_cont_org")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Ministry Of Finance Contact Phone','org.dgfoundation.amp.ar.cell.TextCell','v_mofed_cont_phone' 
from dual where (select count(columnId) from amp_columns where extractorView="v_mofed_cont_phone")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Ministry Of Finance Contact Fax','org.dgfoundation.amp.ar.cell.TextCell','v_mofed_cont_fax' 
from dual where (select count(columnId) from amp_columns where extractorView="v_mofed_cont_fax")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Ministry Of Finance Contact Title','org.dgfoundation.amp.ar.cell.TextCell','v_mofed_cont_title' 
from dual where (select count(columnId) from amp_columns where extractorView="v_mofed_cont_title")=0);


/*sector ministry contact columns*/
insert into amp_columns (columnName, cellType, extractorView) 
(select 'Sector Ministry Contact Name','org.dgfoundation.amp.ar.cell.TextCell','v_sect_min_cont_name' 
from dual where (select count(columnId) from amp_columns where extractorView="v_sect_min_cont_name")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Sector Ministry Contact Email','org.dgfoundation.amp.ar.cell.TextCell','v_sect_min_cont_email' 
from dual where (select count(columnId) from amp_columns where extractorView="v_sect_min_cont_email")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Sector Ministry Contact Organization','org.dgfoundation.amp.ar.cell.TextCell','v_sect_min_cont_org' 
from dual where (select count(columnId) from amp_columns where extractorView="v_sect_min_cont_org")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Sector Ministry Contact Phone','org.dgfoundation.amp.ar.cell.TextCell','v_sect_min_cont_phone' 
from dual where (select count(columnId) from amp_columns where extractorView="v_sect_min_cont_phone")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Sector Ministry Contact Fax','org.dgfoundation.amp.ar.cell.TextCell','v_sect_min_cont_fax' 
from dual where (select count(columnId) from amp_columns where extractorView="v_sect_min_cont_fax")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Sector Ministry Contact Title','org.dgfoundation.amp.ar.cell.TextCell','v_sect_min_cont_title' 
from dual where (select count(columnId) from amp_columns where extractorView="v_sect_min_cont_title")=0);


/*project coordinator contact columns*/
insert into amp_columns (columnName, cellType, extractorView) 
(select 'Project Coordinator Contact Name','org.dgfoundation.amp.ar.cell.TextCell','v_proj_coordr_cont_name' 
from dual where (select count(columnId) from amp_columns where extractorView="v_proj_coordr_cont_name")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Project Coordinator Contact Email','org.dgfoundation.amp.ar.cell.TextCell','v_proj_coordr_cont_email' 
from dual where (select count(columnId) from amp_columns where extractorView="v_proj_coordr_cont_email")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Project Coordinator Contact Organization','org.dgfoundation.amp.ar.cell.TextCell','v_proj_coordr_cont_org' 
from dual where (select count(columnId) from amp_columns where extractorView="v_proj_coordr_cont_org")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Project Coordinator Contact Phone','org.dgfoundation.amp.ar.cell.TextCell','v_proj_coordr_cont_phone' 
from dual where (select count(columnId) from amp_columns where extractorView="v_proj_coordr_cont_phone")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Project Coordinator Contact Fax','org.dgfoundation.amp.ar.cell.TextCell','v_proj_coordr_cont_fax' 
from dual where (select count(columnId) from amp_columns where extractorView="v_proj_coordr_cont_fax")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Project Coordinator Contact Title','org.dgfoundation.amp.ar.cell.TextCell','v_proj_coordr_cont_title'  
from dual where (select count(columnId) from amp_columns where extractorView="v_proj_coordr_cont_title")=0);


/*create or replace donor views*/
create or replace view `v_donor_cont_name` AS (
select `a`.`amp_activity_id`,concat_ws(_latin1' ',`a`.`cont_last_name`,`a`.`cont_first_name`) AS contact from amp_activity `a` 
where trim(concat_ws(' ',`a`.`cont_last_name`, `a`.`cont_first_name`))!="");
		
create or replace view `v_donor_cont_email` AS (select 	`a`.`amp_activity_id`, `a`.`email` from amp_activity `a`  where trim(`a`.`email`)!="");
		
create or replace view `v_donor_cont_org` AS ( select 	`a`.`amp_activity_id`,`a`.`dnr_cont_organization` as `organization` from amp_activity `a` where trim(`a`.`dnr_cont_organization`)!="");
		
create or replace view `v_donor_cont_phone` AS ( select	`a`.`amp_activity_id`,`a`.`dnr_cont_phone_number` as `phone` from amp_activity `a` where trim(`a`.`dnr_cont_phone_number`)!="");
		
create or replace view `v_donor_cont_fax` AS ( select `a`.`amp_activity_id`,`a`.`dnr_cont_fax_number` as `fax` from amp_activity `a`  where trim(`a`.`dnr_cont_fax_number`)!="");

create or replace view `v_donor_cont_title` AS ( select `a`.`amp_activity_id`,`a`.`dnr_cont_title` as `title` from amp_activity `a`  where trim(`a`.`dnr_cont_title`)!="");
		
		
/*create or replace mofed views*/
create or replace view `v_mofed_cont_name` AS ( 
select 	a.amp_activity_id,concat_ws(_latin1' ',a.mofed_cnt_last_name,a.mofed_cnt_first_name) AS contact from amp_activity a 
where trim(concat_ws(' ',a.mofed_cnt_last_name, a.mofed_cnt_first_name ))!="");		
		
create or replace view `v_mofed_cont_email` AS (select 	a.amp_activity_id, a.mofed_cnt_email as email from amp_activity a where trim(a.mofed_cnt_email)!="");
		
create or replace view `v_mofed_cont_org` AS (select a.amp_activity_id,	a.mfd_cont_organization as organization from amp_activity a  where trim(a.mfd_cont_organization)!="");
		
create or replace view `v_mofed_cont_phone` AS (select 	a.amp_activity_id,a.mfd_cont_phone_number as phone from amp_activity a  where trim(a.mfd_cont_phone_number)!="");
		
create or replace view `v_mofed_cont_fax` AS (select a.amp_activity_id, a.mfd_cont_fax_number as fax from amp_activity a  where trim(a.mfd_cont_fax_number)!="");

create or replace view `v_mofed_cont_title` AS (select a.amp_activity_id, a.mfd_cont_title as title from amp_activity a  where trim(a.mfd_cont_title)!="");


/*create or replace sector ministry views */
create or replace view `v_sect_min_cont_name` AS ( 
select 	a.amp_activity_id,concat_ws(_latin1' ',a.sec_min_last_name,a.sec_min_first_name) AS contact from amp_activity a 
where trim(concat_ws(' ',a.sec_min_last_name, a.sec_min_first_name ))!="");
		
create or replace view `v_sect_min_cont_email` AS (select a.amp_activity_id, a.sec_min_email as email from amp_activity a where trim(a.sec_min_email)!="");
		
create or replace view `v_sect_min_cont_org` AS ( select a.amp_activity_id,a.sec_min_organization as organization from amp_activity a where trim(a.sec_min_organization)!="");
		
create or replace view `v_sect_min_cont_phone` AS ( select a.amp_activity_id, a.sec_min_phone_number as phone from amp_activity a where trim(a.sec_min_phone_number)!="");
		
create or replace view `v_sect_min_cont_fax` AS ( select 	a.amp_activity_id,a.sec_min_fax_number as fax from amp_activity a where trim(a.sec_min_fax_number)!="");

create or replace view `v_sect_min_cont_title` AS ( select 	a.amp_activity_id,a.sec_min_title as title from amp_activity a where trim(a.sec_min_title)!="");

		
/*create or replace project coordinator views */
create or replace view `v_proj_coordr_cont_name` AS ( 
select 	a.amp_activity_id,concat_ws(_latin1' ',a.prj_co_last_name,a.prj_co_first_name) AS contact from amp_activity a 
where trim(concat_ws(' ',a.prj_co_last_name, a.prj_co_first_name ))!="");
		
create or replace view `v_proj_coordr_cont_email` AS ( select a.amp_activity_id,a.prj_co_email as email from amp_activity a where trim(a.prj_co_email)!="");
		
create or replace view `v_proj_coordr_cont_org` AS ( select a.amp_activity_id,a.prj_co_organization as organization from amp_activity a where trim(a.prj_co_organization)!="");
		
create or replace view `v_proj_coordr_cont_phone` AS ( select a.amp_activity_id, a.prj_co_phone_number as phone from amp_activity a where trim(a.prj_co_phone_number)!="");
		
create or replace view `v_proj_coordr_cont_fax` AS ( select a.amp_activity_id, a.prj_co_fax_number as fax from amp_activity a where trim(a.prj_co_fax_number)!="");

create or replace view `v_proj_coordr_cont_title` AS ( select a.amp_activity_id, a.prj_co_title as title from amp_activity a where trim(a.prj_co_title)!="");

/*remove unnecessary data, replace with new ones */
drop temporary table if exists tmp_rep_cont;

create temporary table if not exists tmp_rep_cont (
select arc.amp_report_id,cv_level_id,order_id from amp_report_column arc where arc.columnId in
(select columnId from amp_columns where columnName like 'Contact Name')
);

delete from amp_report_column where columnId in (select columnId from amp_columns where columnName like 'Contact Name');
delete from amp_report_hierarchy where columnId in (select columnId from amp_columns where columnName like 'Contact Name');
delete from amp_columns_filters where column_id in (select columnId from amp_columns where columnName like 'Contact Name');
delete from amp_columns where columnName like 'Contact Name';

/*insert data in amp_report_columns*/
insert into amp_report_column (amp_report_id,columnId,cv_level_id,order_id) 
(select amp_report_id,(select columnId from amp_columns where columnName like 'Donor Contact Name' and extractorView='v_donor_cont_name'),cv_level_id,order_id from tmp_rep_cont);

insert into amp_report_column (amp_report_id,columnId,cv_level_id,order_id) 
(select trc.amp_report_id,(select columnId from amp_columns where columnName like 'Ministry Of Finance Contact Name' and extractorView='v_mofed_cont_name'),trc.cv_level_id,(select max(order_id)+1 from amp_report_column m where m.amp_report_id=trc.amp_report_id) from tmp_rep_cont trc);

drop temporary table tmp_rep_cont;

/*drop old view */
drop view if exists v_contact_name;

/* remove 'Contact Name' field from FM */
delete from amp_fields_templates where field=(select id from amp_fields_visibility a where name like 'Contact Name');
delete from amp_fields_visibility where name like 'Contact Name';
