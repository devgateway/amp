delete ft.* from amp_fields_visibility fv, amp_fields_templates ft where fv.name = 'Responsible Organization' and ft.`field` = fv.id;

delete fv.* from amp_fields_visibility fv where fv.name = 'Responsible Organization';
