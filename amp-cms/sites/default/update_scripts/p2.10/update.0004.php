<?php

// Provide a list of modules to be installed.
$modules = array(

  'date',
    'date_api',
    // 'date_migrate',
    // 'date_migrate_example',
    'date_popup',
    // 'date_repeat',
    // 'date_tools',
    // 'date_views',

  'path',
  'pathauto',
  'filefield_paths',
  'globalredirect',
  'redirect',
  'token',
  'transliteration',

);
_us_module__install($modules);

// Update site date settings.
variable_set('date_first_day', 1);
variable_set('date_format_long', 'l, F j, Y - H:i');
variable_set('date_format_medium', 'D, Y-m-d H:i');
variable_set('date_format_short', 'm/d/Y - H:i');


// Add a custom date format "Feb 28, 2012".
$date_format = 'M j, Y';
_us_system__add_date_format($date_format);

$machine_name = 'shortest';
$title = 'Shortest';
_us_system__add_date_format_type($machine_name, $title, $date_format);

// Cleanup pathauto patterns.
db_delete('variable')
  ->condition('name', db_like("pathauto_") . '%' . db_like('pattern'), 'LIKE')
  ->execute();
db_delete('variable')
  ->condition('name', db_like("pathauto_punctuation_") . '%', 'LIKE')
  ->execute();
variable_set('pathauto_node_pattern', '');
variable_set('pathauto_user_pattern', '');

// Update pathauto settings.
variable_set('pathauto_verbose', 0);
variable_set('pathauto_separator', '-');
variable_set('pathauto_case', '1');
variable_set('pathauto_max_length', '100');
variable_set('pathauto_max_component_length', '100');
variable_set('pathauto_update_action', '2');
variable_set('pathauto_transliterate', 1);
variable_set('pathauto_reduce_ascii', 1);
variable_set('pathauto_ignore_words', '');

// Update redirect settings.
variable_set('redirect_auto_redirect', 1);
variable_set('redirect_passthrough_querystring', 1);
variable_set('redirect_warning', false);
variable_set('redirect_default_status_code', '301');
variable_set('redirect_page_cache', 0);
$period = 60 * 60 * 24 * 30 * 6; // 180 days.
variable_set('redirect_purge_inactive', $period);

// Update globalredirect settings.
variable_set('redirect_global_home', 1);
variable_set('redirect_global_clean', 1);
variable_set('redirect_global_canonical', 1);
variable_set('redirect_global_deslash', 0);
variable_set('redirect_global_admin_paths', 0);
