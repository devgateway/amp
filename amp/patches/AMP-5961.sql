update dg_user set assigned_org_id=null where assigned_org_id not in (select amp_org_id from amp_organisation);
insert ignore into dg_user_orgs (user_id,org_id) (select id,assigned_org_id from dg_user where assigned_org_id is not null);
ALTER TABLE perm_map MODIFY object_identifier varchar(255);
