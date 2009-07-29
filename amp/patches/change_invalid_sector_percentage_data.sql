/*un-assign activities from team*/
update amp_activity 
set amp_team_id=null where amp_activity_id in  
(select amp_activity_id from amp_activity_sector where sector_percentage is null group by amp_activity_id); 


/*creating memory table to store invalid data*/
drop table if exists temporary_amp_sector_percentage_data; 
create TABLE temporary_amp_sector_percentage_data(
 amp_activity_sector_id bigint,
 sector_percentage float
) ENGINE = MEMORY;

insert into temporary_amp_sector_percentage_data (amp_activity_sector_id, sector_percentage) 

select as1.amp_activity_sector_id,count(as1.amp_activity_sector_id) from amp_activity_sector as1 join amp_activity_sector as2 on 
as1.sector_percentage is null and as2.sector_percentage is null and as1.amp_activity_id=as2.amp_activity_id and as1.classification_config_id=as2.classification_config_id group by as1.amp_activity_sector_id;

update temporary_amp_sector_percentage_data 
set sector_percentage=100/sector_percentage where mod(100,sector_percentage)=0;

update temporary_amp_sector_percentage_data 
set sector_percentage=null where mod(100,sector_percentage)!=0;

/*update amp_activity_sector table, change null sector percentages with the correct values */
update amp_activity_sector as as1 
set as1.sector_percentage=(select td.sector_percentage from temporary_amp_sector_percentage_data td where as1.amp_activity_sector_id=td.amp_activity_sector_id)  
where as1.sector_percentage is null;

/*drop temporary table if exists*/
drop table if exists temporary_amp_sector_percentage_data;