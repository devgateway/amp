delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Filter Button');
delete from amp_features_visibility where name = 'Filter Button';
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Enable Scrolling Reports');
delete from amp_features_visibility where name = 'Enable Scrolling Reports';
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Save Filters from Desktop');
delete from amp_features_visibility where name = 'Save Filters from Desktop';