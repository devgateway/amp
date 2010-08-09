update amp_user_ext e, amp_organisation o, amp_org_group og set e.amp_org_type_id=og.org_type, e.amp_org_group__id=o.org_grp_id 
	where e.amp_org_id=o.amp_org_id and og.amp_org_grp_id = o.org_grp_id; 