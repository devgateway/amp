START TRANSACTION;

delete from amp_modules_templates where module in (select id FROM amp_modules_visibility WHERE LOWER(name) = 'activity imp&exp');
delete FROM amp_modules_visibility WHERE LOWER(name) = 'activity imp&exp';

delete from amp_modules_templates where module in (select id FROM amp_modules_visibility WHERE LOWER(name) = 'physical progress');
delete FROM amp_modules_visibility WHERE LOWER(name) = 'physical progress';

commit;