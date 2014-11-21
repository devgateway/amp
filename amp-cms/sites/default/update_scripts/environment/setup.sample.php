<?php

/**
 * @file: SETUP ENVIRONMENT: (database) sample
 * @desc: This script can be used to setup database sample environments.
 *
 * This is an update script for setting up database SAMPLE environments. The
 * script should run automatically at the end of the build process.
 */

// Save a watchdog entry for this build.
watchdog('us-environment', 'Setup SAMPLE Environment');

// Disable CSS and JavaScript aggregation.
variable_set('preprocess_css', 0);
variable_set('preprocess_js', 0);

// Disable LESS Developer mode.
variable_set('less_devel', 0);
variable_set('less_watch', 0);

// Error messages to display:
//   ERROR_REPORTING_HIDE => None
//   ERROR_REPORTING_DISPLAY_SOME => Errors and warnings
//   ERROR_REPORTING_DISPLAY_ALL => All messages
variable_set('error_level', ERROR_REPORTING_DISPLAY_ALL);

// Include the production setup script.
include $script_directory . '/setup.production.php';
