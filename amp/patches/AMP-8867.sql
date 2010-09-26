delete ft.* from amp_features_visibility fv, amp_features_templates ft where fv.name = 'Pledge Location' and ft.feature = fv.id;
delete fv.* from amp_features_visibility fv where fv.name = 'Pledge Location';

delete ft.* from amp_features_visibility fv, amp_features_templates ft where fv.name = 'Pledge Sector' and ft.feature = fv.id;
delete fv.* from amp_features_visibility fv where fv.name = 'Pledge Sector';