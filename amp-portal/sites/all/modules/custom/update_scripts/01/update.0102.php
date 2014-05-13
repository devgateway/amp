<?php

if (!function_exists('_us_system__add_date_format')) {
  /**
   * Create a new date format string.
   *
   * @see http://www.php.net/manual/en/function.date.php
   */
  function _us_system__add_date_format($date_format) {
    $formats = system_get_date_formats('custom');

    // Make sure the date_format dos not exit.
    if (!empty($formats) && in_array($date_format, array_keys($formats))) {
      return;
    }

    $format_info = array();
    $format_info['format'] = $date_format;
    $format_info['type'] = 'custom';
    $format_info['locked'] = 0;
    $format_info['is_new'] = 1;
    system_date_format_save($format_info);

    // Rebuild list of date formats.
    system_date_formats_rebuild();
  }
}

if (!function_exists('_us_system__add_date_format_type')) {
  /**
   * Create a new date format type.
   */
  function _us_system__add_date_format_type($machine_name, $title, $date_format) {
    // See if this value already exists in the table.
    $format_types = db_select('date_format_type', 'dft')
      ->fields('dft', array('type'))
      ->execute()
      ->fetchCol();

    // Make sure the date_format_type does not exist.
    if (!empty($format_types) && in_array($machine_name, $format_types)) {
      return;
    }

    // Make sure the date_format exits.
    $date_formats = system_get_date_formats('custom');
    if (empty($date_formats) || !in_array($date_format, array_keys($date_formats))) {
      return;
    }

    // Add a custom date type.
    $new_format_type = array();
    $new_format_type['title'] = $title;
    $new_format_type['type'] = $machine_name;
    $new_format_type['locked'] = 0;
    $new_format_type['is_new'] = 1;
    system_date_format_type_save($new_format_type);
    variable_set('date_format_' . $machine_name, $date_format);
  }
}

// Fix short date format. It should include the hour and minute since it is used
// to display watchdog entries.
$default_format = 'm/d/Y - H:i';
variable_set('date_format_short', $default_format);
db_query('SELECT * FROM date_format_locale;');
// Update all localized values of this format_type.
db_update('date_format_locale')
  ->fields(array('format' => $default_format))
  ->condition('type', 'short')
  ->execute();


// Add a custom date format "Apr 01, 2014".
$date_format = 'M d, Y';
_us_system__add_date_format($date_format);

$machine_name = 'shortest';
$title = 'Shortest';
_us_system__add_date_format_type($machine_name, $title, $date_format);

// Revert feature.
_us_revert_feature('ampp_projects');
