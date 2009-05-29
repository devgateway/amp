delete from amp_modules_templates where module in (select id from amp_modules_visibility where name='Components_Resume');
delete from amp_modules_visibility where name='Components_Resume';
