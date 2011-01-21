DELETE FROM `amp_modules_templates` WHERE module IN (SELECT id FROM `amp_modules_visibility` WHERE name ='Off Line Reports');
DELETE FROM `amp_modules_visibility` WHERE name='Off Line Reports';
DELETE FROM `amp_modules_templates` WHERE module IN (SELECT id FROM `amp_modules_visibility` WHERE name ='Off Line Reports Public View');
DELETE FROM `amp_modules_visibility` WHERE name='Off Line Reports Public View';


