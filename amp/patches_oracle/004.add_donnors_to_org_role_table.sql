insert into amp_org_role (amp_org_role_id,activity,organisation, role) 
select  amp_orgrole_seq.NEXTVAL,f.amp_activity_id,f.amp_donor_org_id,1 
from amp_funding f  where f.amp_donor_org_id not in (select z.organisation from amp_org_role z WHERE z.role=1 and z.activity=f.amp_activity_id)

