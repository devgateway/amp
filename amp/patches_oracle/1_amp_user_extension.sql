update amp_user_ext e set e.amp_org_type_id=
(select o.org_Type_id from amp_organisation o where o.amp_org_id=e.amp_org_id);
update amp_user_ext e set e.amp_org_group__id=
(select o.org_grp_id from amp_organisation o where o.amp_org_id=e.amp_org_id);