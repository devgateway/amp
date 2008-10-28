delete ft.* from amp_fields_visibility fv, amp_fields_templates ft where fv.name = 'Components Perspective Commitments' and ft.`field` = fv.id;

delete fv.* from amp_fields_visibility fv where fv.name = 'Components Perspective Commitments';


delete ft.* from amp_fields_visibility fv, amp_fields_templates ft where fv.name = 'Components Perspective Disbursements' and ft.`field` = fv.id;

delete fv.* from amp_fields_visibility fv where fv.name = 'Components Perspective Disbursements';


delete ft.* from amp_fields_visibility fv, amp_fields_templates ft where fv.name = 'Components Perspective Expenditures' and ft.`field` = fv.id;

delete fv.* from amp_fields_visibility fv where fv.name = 'Components Perspective Expenditures' ;