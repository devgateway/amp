<?php

/**
 * @file:
 * This is an example update script for PREPROD and PRODUCTION environment setup.
 * It should be run automatically at the end of the build process.
 *
 * This update script should be run to enable/disable CSS and JavaScript
 * aggregation; enable/disable block and page caching, enable/disable developer
 * modules.
 *
 * TODO:
 *  - Rename this file to: update.production.php and copy it to your scripts folder.
 *  - Always extend and improve this script.
 */

// Save a watchdog entry for this build.
watchdog('us-environment', 'Setup PRODUCTION Environment');

// Enable CSS and JavaScript aggregation.
variable_set('preprocess_css', 1);
variable_set('preprocess_js', 1);

// Disable LESS Developer mode.
variable_set('less_devel', 0);

// Make sure the compressed version of Modernizr library is loaded.
variable_set('modernizr_build', 'production');

// Make sure the compressed version of jQuery and jQuery UI libraries are loaded.
variable_set('jquery_update_compression_type', 'min');

// Do not use any CDN for jQuery and jQuery UI
variable_set('jquery_update_jquery_cdn', 'none');

// Make sure that jQuery version 1.8.x is used on the frontend of the website.
variable_set('jquery_update_jquery_version', '1.8');

// Make sure that jQuery version 1.5.x is used on the admin interface.
variable_set('jquery_update_jquery_admin_version', '1.5');

// Provide a list of modules to be disabled and uninstalled.
$modules = array(
  'coder',
    'coder_review',
  'devel',
    'devel_generate',
    'devel_node_access',
  'diff',
  'ds_ui',
  'feeds_ui',
  'menu',
  'module_filter',
  'og_ui',
  'page_manager',
  'panels_ipe',
  'stage_file_proxy',
  'variable_admin',
  'views_ui',
);
// Uninstall modules
_us_module__uninstall($modules);
