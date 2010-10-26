DELETE FROM `amp_fields_templates` WHERE field IN (SELECT id FROM `amp_fields_visibility` WHERE name ='Ministry Rank');
DELETE FROM `amp_fields_visibility` WHERE name = 'Ministry Rank';





