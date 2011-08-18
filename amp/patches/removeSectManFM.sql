delete FROM amp_modules_templates where module = (select id FROM amp_modules_visibility where name like 'Sector  Manager');
delete FROM amp_modules_visibility where name like 'Sector  Manager';