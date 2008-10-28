CREATE OR REPLACE VIEW v_donor_groups AS
select f.amp_activity_id AS amp_activity_id,
o.org_group AS grp,f.amp_donor_org_id
AS amp_donor_org_id from amp_funding f, amp_organisation o 
where f.amp_donor_org_id = o.amp_org_id and (o.org_group is not null) 
order by f.amp_activity_id,o.org_group;