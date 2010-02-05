/* Old Module */
delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'Old Report Wizard');
delete from amp_modules_visibility where name = 'Old Report Wizard';

/* Measures */
delete from amp_fields_templates where field in (select id from amp_fields_visibility where parent in (select id from amp_features_visibility where name = 'Measures'));
delete from amp_fields_visibility where parent in (select id from amp_features_visibility where name = 'Measures');

delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Measures');
delete from amp_features_visibility where name = 'Measures';

/* Report Generator and Tab Generator */
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Report Generator');
delete from amp_features_visibility where name = 'Report Generator';
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Tab Generator');
delete from amp_features_visibility where name = 'Tab Generator';

/* Report Types */
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Component Report');
delete from amp_features_visibility where name = 'Component Report';
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Contribution Report');
delete from amp_features_visibility where name = 'Contribution Report';
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Donor Report');
delete from amp_features_visibility where name = 'Donor Report';
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Regional Report');
delete from amp_features_visibility where name = 'Regional Report';