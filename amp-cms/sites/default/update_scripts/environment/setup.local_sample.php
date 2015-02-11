<?php

/**
 * @file: SETUP ENVIRONMENT: local (development)
 * @desc: This script can be used to prepare the environment for local development.
 *
 * This is an example update script for setting up development environments. The
 * script should be used whenever a new database is imported.
 *
 * This update script should be run to disable e-mail notifications, reset user
 * passwords, remove sensitive data.
 *
 * TODO: Always extend and improve this script.
 */
$DEVEL_MODE = TRUE;

// Save a watchdog entry for this build.
// watchdog('us-environment', 'Setup LOCAL Environment');

// Reset the admin password.
if (!variable_get('update_scripts_user_one_updated', FALSE)) {
  $user = user_load(1);
  $user_object = user_save($user, array('pass' => $user->name));
  variable_set('update_scripts_user_one_updated', TRUE);
}

if ($DEVEL_MODE) {
  // Disable CSS and JavaScript aggregation.
  variable_set('preprocess_css', 0);
  variable_set('preprocess_js', 0);

  // Enable LESS Developer mode.
  // variable_set('less_devel', 1);
  variable_set('less_watch', 0);
}
else {
  // Enable CSS and JavaScript aggregation.
  variable_set('preprocess_css', 1);
  variable_set('preprocess_js', 1);

  // Disable LESS Developer mode.
  variable_set('less_devel', 0);
  variable_set('less_watch', 0);
}

// Error messages to display:
//   ERROR_REPORTING_HIDE => None
//   ERROR_REPORTING_DISPLAY_SOME => Errors and warnings
//   ERROR_REPORTING_DISPLAY_ALL => All messages
variable_set('error_level', ERROR_REPORTING_DISPLAY_ALL);

// Disable update_scripts.module "Auto clear cache" and "Auto revert features".
variable_set('update_scripts_clear_cache', 'no');
variable_set('update_scripts_revert_features', 'no');

// Fix isssue with "Temporary directory" when using a DB from a different OS.
variable_del('file_temporary_path');

// Disable the loading of external fonts.
// variable_set('helpertheme_load_external_fonts', FALSE);

// Disable the UI cleanup.
variable_set('helpergeneric_cleanup_ui_force', FALSE);

// Reset the hostname for the AMP API rest endpoint
// variable_set('restclient_hostname', 'http://amp210-local.dev/rest');
// variable_set('restclient_hostname', 'http://amp211-local.dev/rest');

// Provide a list of modules to be disabled and uninstalled.
$modules = array(
  // 'entitycache',
  // 'memcache',
  // 'memcache_admin',
);
// Uninstall modules
_us_module__uninstall($modules);

// Provide a list of modules to be installed.
$modules = array(
  'admin_menu',
    // 'admin_devel',
    'admin_menu_toolbar',
    // 'admin_views',
  // 'coder',
    // 'coder_review',
  // 'contextual',
  'devel',
    // 'devel_generate',
    // 'devel_node_access',
  'diff',
  'ds_ui',
  'field_ui',
  'feeds_ui',
  'feeds_tamper_ui',
  // 'l10n_client',
  // 'l10n_update',
  // 'menu',
  'module_filter',
  // 'og_ui',
  // 'page_manager',
  // 'panels_ipe',
  // 'schema',
  // 'stage_file_proxy',
  // 'update',
  'variable_admin',
  'views_ui',
);
// Install modules
_us_module__install($modules);
