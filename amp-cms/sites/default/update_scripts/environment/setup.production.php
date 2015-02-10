<?php

/**
 * @file: SETUP ENVIRONMENT: production
 * @desc: This script can be used to setup production environments.
 *
 * This is an update script for setting up PRODUCTION environments. The script
 * should run automatically at the end of the build process.
 */

// Skip if we build other environments based on production.
if (PROJECT_ENVIRONMENT == 'production') {
  // Save a watchdog entry for this build.
  watchdog('us-environment', 'Setup PRODUCTION Environment');

  // Enable CSS and JavaScript aggregation.
  variable_set('preprocess_css', 1);
  variable_set('preprocess_js', 1);

  // Disable LESS Developer mode.
  variable_set('less_devel', 0);
  variable_set('less_watch', 0);

  // Error messages to display:
  //   ERROR_REPORTING_HIDE => None
  //   ERROR_REPORTING_DISPLAY_SOME => Errors and warnings
  //   ERROR_REPORTING_DISPLAY_ALL => All messages
  variable_set('error_level', ERROR_REPORTING_HIDE);

  // By default cleanup the UI for all users.
  variable_del('helpergeneric_cleanup_ui_force');
}

// Provide a list of modules to be disabled and uninstalled.
$modules = array(
  'coder',
    'coder_review',
  // 'contextual',
  'devel',
    'devel_generate',
    'devel_node_access',
  // 'diff',
  'ds_ui',
  'field_ui',
  'feeds_ui',
  'feeds_tamper_ui',
  // 'l10n_client',
  'l10n_update',
  'menu',
  // 'memcache_admin',
  'module_filter',
  // 'og_ui',
  'page_manager',
  // 'panels_ipe',
  'stage_file_proxy',
  'variable_admin',
  'views_ui',
);
// Uninstall modules
_us_module__uninstall($modules);
