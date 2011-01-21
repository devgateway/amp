delete from amp_fields_templates where field in (select id from amp_fields_visibility where name = 'Filter Button');
delete from amp_fields_visibility where name = 'Filter Button';

delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Report and Tab Options');
delete from amp_features_visibility where name = 'Report and Tab Options';
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Enable Scrolling Reports');
delete from amp_features_visibility where name = 'Enable Scrolling Reports';
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Save Filters from Desktop');
delete from amp_features_visibility where name = 'Save Filters from Desktop';

delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'New Report Wizard');
delete from amp_modules_visibility where name = 'New Report Wizard';
delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'Reports');
delete from amp_modules_visibility where name = 'Reports';