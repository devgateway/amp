insert into `amp_columns` (columnName, cellType, extractorView)
values('Donor Contact','org.dgfoundation.amp.ar.cell.MetaTextCell','v_contact_donor');
insert into `amp_columns` (columnName, cellType, extractorView)
values('Ministry of Finance Contact','org.dgfoundation.amp.ar.cell.MetaTextCell','v_contact_mofed');
insert into `amp_columns` (columnName, cellType, extractorView)
values('Project Coordinator','org.dgfoundation.amp.ar.cell.MetaTextCell','v_contact_project_coordinator');
insert into `amp_columns` (columnName, cellType, extractorView)
values('Sector Ministry Contact','org.dgfoundation.amp.ar.cell.MetaTextCell','v_contact_sector_ministry');

create view `v_contact_sector_ministry` AS (
select
	a.amp_activity_id,
	concat_ws(_latin1' ',`c`.`lastname`,`c`.`name`) AS `contact`,
    a.amp_activity_id AS `contact_id` 
from 
	amp_contact c,
    amp_activity a,
    amp_activity_contact ac 
where
	c.contact_id = ac.contact_id  
and	ac.activity_id = a.amp_activity_id 
and ac.contact_type LIKE 'SECTOR_MINISTRY_CONT');


create view `v_contact_project_coordinator` AS (
select
	a.amp_activity_id,
	concat_ws(_latin1' ',`c`.`lastname`,`c`.`name`) AS `contact`,
    a.amp_activity_id AS `contact_id`
from 
	amp_contact c,
    amp_activity a,
    amp_activity_contact ac 
where
	c.contact_id = ac.contact_id 
and	ac.activity_id = a.amp_activity_id 
and ac.contact_type LIKE 'PROJ_COORDINATOR_CONT');


create view `v_contact_mofed` AS (
select
	a.amp_activity_id, 
	concat_ws(_latin1' ',`c`.`lastname`,`c`.`name`) AS `contact`,
    a.amp_activity_id AS `contact_id` 
from 
	amp_contact c,
    amp_activity a,
    amp_activity_contact ac 
where 
	c.contact_id = ac.contact_id 
and	ac.activity_id = a.amp_activity_id 
and ac.contact_type LIKE 'MOFED_CONT');


create view `v_contact_donor` AS (
select
	a.amp_activity_id,
	concat_ws(_latin1' ',`c`.`lastname`,`c`.`name`) AS `contact`,
    a.amp_activity_id AS `contact_id`
from 
	amp_contact c,
    amp_activity a,
    amp_activity_contact ac
where
	c.contact_id = ac.contact_id
and	ac.activity_id = a.amp_activity_id
and ac.contact_type LIKE 'DONOR_CONT');
