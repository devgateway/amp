<?php

/**
 * @file:
 * This is an example update script for STAGING environment setup. It should
 * be run automatically at the end of the build process.
 *
 * This update script should be run to enable/disable CSS and JavaScript
 * aggregation; enable/disable block and page caching, enable/disable developer
 * modules.
 *
 * TODO:
 *  - Rename this file to: update.staging.php and copy it to your scripts folder.
 *  - Always extend and improve this script.
 */

// Save a watchdog entry for the first update script in this phase.
watchdog('us-environment', 'Setup STAGING Environment');

// Disable CSS and JavaScript aggregation.
variable_set('preprocess_css', 0);
variable_set('preprocess_js', 0);

// Enable LESS Developer mode.
// variable_set('less_devel', 1);

// Use the development version of Modernizr.
variable_set('modernizr_build', 'development');

// Do not use any CDN for jQuery and jQuery UI
variable_set('jquery_update_jquery_cdn', 'none');

// Make sure that jQuery version 1.8.x is used on the frontend of the website.
variable_set('jquery_update_jquery_version', '1.8');

// Make sure that jQuery version 1.5.x is used on the admin interface.
variable_set('jquery_update_jquery_admin_version', '1.5');
