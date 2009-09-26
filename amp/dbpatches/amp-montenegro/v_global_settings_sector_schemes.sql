INSERT INTO amp_global_settings (id,settingsName,settingsValue,possibleValues) values 
(5,'Default Sector Scheme',1,'v_global_settings_sector_schemes');

CREATE OR REPLACE VIEW `v_global_settings_sector_schemes` AS 
select `amp_sector_scheme`.`amp_sec_scheme_id` AS `id`,
`amp_sector_scheme`.`sec_scheme_name` AS `value` 
from `amp_sector_scheme` order by `amp_sector_scheme`.`sec_scheme_name`;