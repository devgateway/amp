update amp_user_ext e, amp_organisation o set e.amp_org_type_id=o.org_type_id, e.amp_org_group__id=o.org_grp_id 
	where e.amp_org_id=o.amp_org_id; 