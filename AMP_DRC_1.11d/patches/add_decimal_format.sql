CREATE OR REPLACE VIEW `v_default_number_format` AS
  select
    `util_global_settings_possible_values`.`value_id` AS `id`,
    `util_global_settings_possible_values`.`value_shown` AS `value`
  from
    `util_global_settings_possible_values`
  where
    (`util_global_settings_possible_values`.`setting_name` = _latin1'Default Number Format');



INSERT INTO `amp_global_settings`
  (`settingsName`, `settingsValue`, `possibleValues`, `description`) VALUES
  ('Default Number Format', '###,###,###,###.##', 'v_default_number_format', 'Default Number Format'),
  ( 'Default Decimal Separator', ',', NULL, 'Default Number Decimal Separator'),
  ( 'Default Grouping Separator', '.', NULL, 'Default Number Grouping Separator');


INSERT INTO `util_global_settings_possible_values` (`setting_name`, `value_id`, `value_shown`) VALUES

  ('Default Number Format', '#.#', '#.#'),
  ('Default Number Format', '#.######', '#.######'),
  ('Default Number Format', '###,###,###,###.##', '###,###,###,###.##'),
  ('Default Number Format', '000000.000', '000000.000');




