<?php

/**
 * @file
 * Update Scripts helper functions.
 */

/**
 * Force table index.
 */
function _us_database__set_next_id($tablename, $value) {
  $current = db_next_id(db_query("SELECT MAX(vid) FROM {" . $tablename . "}")->fetchField());
  if ($current < $value) {
    db_query("ALTER TABLE {" . $tablename . "} AUTO_INCREMENT = " . $value);
    return db_next_id(db_query("SELECT MAX(vid) FROM {" . $tablename . "}")->fetchField());
  }
}

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

/**
 * Enable a theme.
 *
 * TODO: Add messages and watchdog entries.
 */
function _us_theme__enable($theme_name, $set_default = FALSE) {
  require_once DRUPAL_ROOT . '/includes/theme.inc';

  // Get a list of all themes.
  $themes = list_themes();

  // Check if the specified theme is one recognized by the system.
  if (!empty($themes[$theme_name])) {
    theme_enable(array($theme_name));

    if ($set_default) {
      // Set default theme.
      variable_set('theme_default', $theme_name);
    }
  }
}

/**
 * Disable a theme.
 *
 * TODO: Add messages and watchdog entries.
 */
function _us_theme__disable($theme_name) {
  require_once DRUPAL_ROOT . '/includes/theme.inc';

  // Get the default theme.
  $default_theme = variable_get('theme_default', '');

  // Do not disable the default theme.
  if ($theme_name == $default_theme) {
    return;
  }

  // Get a list of all themes.
  $themes = list_themes();

  // Check if the specified theme is one recognized by the system.
  if (!empty($themes[$theme_name])) {
    theme_disable(array($theme_name));
  }
}
