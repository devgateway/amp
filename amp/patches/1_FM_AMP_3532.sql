DELETE fit.* FROM amp_fields_templates fit, amp_fields_visibility fiv, amp_features_visibility fv where fv.name = 'SISIN' and fiv.parent = fv.id and fit.`field` = fiv.id;
DELETE fiv.* FROM amp_fields_visibility fiv, amp_features_visibility fv where fv.name = 'SISIN' and fiv.parent = fv.id;
DELETE ft.* from amp_features_visibility fv, amp_features_templates ft where fv.name = 'SISIN' and ft.feature = fv.id;
DELETE fv.* from amp_features_visibility fv where fv.name = 'SISIN';


DELETE fit.* FROM amp_fields_templates fit, amp_fields_visibility fiv, amp_features_visibility fv where fv.name = 'Components' and fiv.parent = fv.id and fit.`field` = fiv.id;
DELETE fiv.* FROM amp_fields_visibility fiv, amp_features_visibility fv where fv.name = 'Components' and fiv.parent = fv.id;
DELETE ft.* from amp_features_visibility fv, amp_features_templates ft where fv.name = 'Components' and ft.feature = fv.id;
DELETE fv.* from amp_features_visibility fv where fv.name = 'Components';