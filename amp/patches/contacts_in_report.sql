drop temporary table if exists tmp_rep_cont;

create temporary table if not exists tmp_rep_cont (
select c1.columnName, c1.columnId,count(c1.columnId)as duplicateAmount 
from amp_columns c1 join amp_columns c2 on  (c1.columnName like '%contact%' or c1.columnName like 'project coordinator')  and c1.columnName=c2.columnName group by c1.columnId having count(c1.columnId)>1 order by c1.columnName 
);

delete from amp_report_column where columnId in (select columnId from tmp_rep_cont where duplicateAmount>1);
delete from amp_report_hierarchy where columnId in (select columnId from tmp_rep_cont where duplicateAmount>1);
delete from amp_columns where columnId in (select columnId from tmp_rep_cont where duplicateAmount>1);
drop temporary table tmp_rep_cont;


insert into amp_columns (columnName, cellType, extractorView) 
SELECT 'Donor Contact','org.dgfoundation.amp.ar.cell.MetaTextCell','v_contact_donor'   
from dual where (select count(columnId) from amp_columns where extractorView="v_contact_donor")=0;

insert into amp_columns (columnName, cellType, extractorView) 
SELECT 'Ministry Of Finance Contact','org.dgfoundation.amp.ar.cell.MetaTextCell','v_contact_mofed'  
from dual where (select count(columnId) from amp_columns where extractorView="v_contact_mofed")=0;

insert into amp_columns (columnName, cellType, extractorView) 
SELECT 'Project Coordinator Contact','org.dgfoundation.amp.ar.cell.MetaTextCell','v_contact_project_coordinator'   
from dual where (select count(columnId) from amp_columns where extractorView="v_contact_project_coordinator")=0;

insert into amp_columns (columnName, cellType, extractorView) 
SELECT 'Sector Ministry Contact','org.dgfoundation.amp.ar.cell.MetaTextCell','v_contact_sector_ministry'  
from dual where (select count(columnId) from amp_columns where extractorView="v_contact_sector_ministry")=0;

insert into amp_columns (columnName, cellType, extractorView) 
SELECT 'Implementing/Executing Agency Contact','org.dgfoundation.amp.ar.cell.MetaTextCell','v_contact_impl_exec_agency'  
from dual where (select count(columnId) from amp_columns where extractorView="v_contact_impl_exec_agency")=0;


/*creating/replacing donor view*/
create or replace view `v_contact_donor` AS (
select 	a.amp_activity_id,concat_ws(_latin1' ',`c`.`lastname`,`c`.`name`) AS `contact`,
    	a.amp_activity_id AS `contact_id` from amp_contact c,amp_activity a,amp_activity_contact ac  
where c.contact_id = ac.contact_id and	ac.activity_id = a.amp_activity_id and ac.contact_type LIKE 'DONOR_CONT'  
 and ac.is_primary_contact is true );

/*creating/replacing mofed view*/
create or replace view `v_contact_mofed` AS (
select a.amp_activity_id, concat_ws(_latin1' ',`c`.`lastname`,`c`.`name`) AS `contact`,a.amp_activity_id AS `contact_id`  
from amp_contact c,amp_activity a,amp_activity_contact ac 
where c.contact_id = ac.contact_id and	ac.activity_id = a.amp_activity_id 
and ac.contact_type LIKE 'MOFED_CONT' and ac.is_primary_contact is true);

/*creating/replacing sector ministry view*/
create or replace view `v_contact_sector_ministry` AS (
select a.amp_activity_id, concat_ws(_latin1' ',`c`.`lastname`,`c`.`name`) AS `contact`,a.amp_activity_id AS `contact_id` 
from amp_contact c,amp_activity a,amp_activity_contact ac 
where c.contact_id = ac.contact_id and	ac.activity_id = a.amp_activity_id 
and ac.contact_type LIKE 'SECTOR_MINISTRY_CONT' and ac.is_primary_contact is true );

/*creating/replacing proj. coordinator view*/
create or replace view `v_contact_project_coordinator` AS (
select a.amp_activity_id, concat_ws(_latin1' ',`c`.`lastname`,`c`.`name`) AS `contact`,a.amp_activity_id AS `contact_id` 
from amp_contact c,amp_activity a,amp_activity_contact ac 
where c.contact_id = ac.contact_id and	ac.activity_id = a.amp_activity_id  
and ac.contact_type LIKE 'PROJ_COORDINATOR_CONT' and ac.is_primary_contact is true);


/*creating/replacing implementing/executing agency view*/
create or replace view `v_contact_impl_exec_agency` AS (
select a.amp_activity_id, concat_ws(_latin1' ',`c`.`lastname`,`c`.`name`) AS `contact`,a.amp_activity_id AS `contact_id` 
from amp_contact c,amp_activity a,amp_activity_contact ac 
where c.contact_id = ac.contact_id and	ac.activity_id = a.amp_activity_id 
and ac.contact_type LIKE 'IMPL_EXEC_AGENCY_CONT' and ac.is_primary_contact is true);