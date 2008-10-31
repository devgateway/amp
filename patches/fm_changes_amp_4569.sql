delete from amp_fields_templates where field in (select id from amp_fields_visibility where parent in (select b.id from amp_features_visibility b where b.name = 'Messages'));

delete from amp_fields_visibility where parent in (select b.id from amp_features_visibility b where b.name = 'Messages');

delete from amp_features_templates where feature in (select b.id from amp_features_visibility b where b.name = 'Messages');

delete from amp_features_visibility  where name = 'Messages';

delete from amp_modules_visibility where name = 'My Messages';

insert into amp_features_visibility (name, parent, hasLevel) VALUES ('My Messages', (select b.id from amp_tanzania.amp_modules_visibility b where b.`name` = 'Messages'), true);
