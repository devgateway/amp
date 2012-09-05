create or replace view v_executing_agency as  
  select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id, sum(COALESCE(f.percentage,NULL,1)) AS percentage  
  from amp_org_role f,amp_organisation o,amp_role r 
  where f.organisation = o.amp_org_id and f.role = r.amp_role_id and r.role_code = 'EA' 
  group by f.activity, f.role, f.organisation, o.name 
  order by f.activity,o.name ; 
 
create or replace view v_implementing_agency as 
  select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id, sum(COALESCE(f.percentage,NULL,1)) AS percentage  
  from amp_org_role f,amp_organisation o,amp_role r 
  where f.organisation = o.amp_org_id and f.role = r.amp_role_id and r.role_code = 'IA' 
  group by f.activity, f.role, f.organisation, o.name 
  order by f.activity,o.name ; 
 
create or replace view v_beneficiary_agency as 
  select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id, sum(COALESCE(f.percentage,NULL,1)) AS percentage  
  from amp_org_role f,amp_organisation o,amp_role r 
  where f.organisation = o.amp_org_id and f.role = r.amp_role_id and r.role_code = 'BA' 
  group by f.activity, f.role, f.organisation, o.name 
  order by f.activity,o.name ; 
 
create or replace view v_contracting_agency as  
  select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id, sum(COALESCE(f.percentage,NULL,1)) AS percentage   
  from amp_org_role f,amp_organisation o,amp_role r 
  where f.organisation = o.amp_org_id and f.role = r.amp_role_id and r.role_code = 'CA' 
  group by f.activity, f.role, f.organisation, o.name 
  order by f.activity,o.name ; 
 
create or replace view v_responsible_organisation as 
  select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id, sum(COALESCE(f.percentage,NULL,1)) AS percentage  
  from amp_org_role f,amp_organisation o,amp_role r 
  where f.organisation = o.amp_org_id and f.role = r.amp_role_id and r.role_code = 'RO' 
  group by f.activity, f.role, f.organisation, o.name 
  order by f.activity,o.name ; 
 
create or replace view v_sector_group as 
  select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id, sum(COALESCE(f.percentage,NULL,1)) AS percentage  
  from amp_org_role f,amp_organisation o,amp_role r 
  where f.organisation = o.amp_org_id and f.role = r.amp_role_id and r.role_code = 'SG' 
  group by f.activity, f.role, f.organisation, o.name 
  order by f.activity,o.name ; 

create or replace view v_regional_group as  
  select f.activity AS amp_activity_id,o.name AS name,f.organisation AS amp_org_id, sum(COALESCE(f.percentage,NULL,1)) AS percentage 
  from amp_org_role f,amp_organisation o,amp_role r 
  where f.organisation = o.amp_org_id and f.role = r.amp_role_id and r.role_code = 'RG' 
  group by f.activity, f.role, f.organisation, o.name 
  order by f.activity,o.name ; 
 
