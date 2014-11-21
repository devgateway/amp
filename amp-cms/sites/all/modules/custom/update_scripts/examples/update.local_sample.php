<?php

/**
 * @file:
 * This is an example file for local environment setup. It should be used
 * whenever copying a database from production, staging or any other environment.
 *
 * This update script should be run to disable e-mail notifications, reset user
 * passwords, remove sensitive data.
 *
 * TODO:
 *  - Copy this script to your scripts folder and commit to your VCS in order to
 *    provide an example.
 *  - Always extend and improve this script.
 */
$DEVEL_MODE = TRUE;

// Save a watchdog entry for this build.
// watchdog('us-environment', 'Setup LOCAL Environment');

if ($DEVEL_MODE) {
  // Disable CSS and JavaScript aggregation.
  variable_set('preprocess_css', 0);
  variable_set('preprocess_js', 0);

  // Enable LESS Developer mode.
  // variable_set('less_devel', 1);
}
else {
  // Enable CSS and JavaScript aggregation.
  variable_set('preprocess_css', 1);
  variable_set('preprocess_js', 1);

  // Disable LESS Developer mode.
  variable_set('less_devel', 0);
}

// Disable update_scripts.module "Auto clear cache" and "Auto revert features".
variable_set('update_scripts_clear_cache', 'no');
variable_set('update_scripts_revert_features', 'no');

// Update the Temporary directory for the current system.
// variable_set('file_temporary_path', '/tmp');
// variable_set('file_temporary_path', 'tmp');
// variable_set('file_temporary_path', 'C:\\Windows\\Temp');
// variable_set('file_temporary_path', 'C:\\Users\\USERNAME\\AppData\\Local\\Temp');

// Use the development version of Modernizr.
variable_set('modernizr_build', 'development');

// Do not use any CDN for jQuery and jQuery UI
variable_set('jquery_update_jquery_cdn', 'none');

// Make sure that jQuery version 1.8.x is used.
variable_set('jquery_update_jquery_version', '1.8');

// Make sure that jQuery version 1.5.x is used on the admin interface.
variable_set('jquery_update_jquery_admin_version', '1.5');

// Provide a list of modules to be disabled and uninstalled.
$modules = array(
  'admin',
);
// Uninstall modules
_us_module__uninstall($modules);

// Provide a list of modules to be installed.
$modules = array(
  // 'admin',
  'admin_menu',
    // 'admin_devel',
    'admin_menu_toolbar',
    // 'admin_views',
  // 'coder',
    // 'coder_review',
  'devel',
    // 'devel_generate',
    // 'devel_node_access',
  'diff',
  'ds_ui',
  // 'feeds_ui',
  // 'menu',
  'module_filter',
  // 'og_ui',
  'page_manager',
  // 'panels_ipe',
  // 'schema',
  // 'stage_file_proxy',
  // 'variable_admin',
  'views_ui',
);
// Install modules
_us_module__install($modules);
