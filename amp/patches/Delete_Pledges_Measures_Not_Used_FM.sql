Delete from `amp_features_templates` where feature IN(
select id from `amp_features_visibility` f where f.`name` like 'pledges commitments');

Delete from `amp_features_templates` where feature IN(
select id from `amp_features_visibility` f where f.`name` like 'Pledges Disbursements');