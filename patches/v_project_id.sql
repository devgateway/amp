create or replace view v_project_id as select i.amp_activity_id, concat(i.internal_id,' (',o.name,')') from amp_organisation o, amp_activity_internal_id i where o.amp_org_id=i.amp_org_id;
