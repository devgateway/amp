insert into amp_org_role(activity,organisation,role) 
select f2.amp_activity_id, f2.amp_donor_org_id, r.amp_role_id 
from amp_funding f2, amp_role r 
where r.role_code='DN' and f2.amp_funding_id not in ( 
 select f.amp_funding_id 
 from amp_funding f, amp_org_role orl 
 where orl.activity=f.amp_activity_id 
 and orl.organisation=f.amp_donor_org_id and orl.role=r.amp_role_id
);