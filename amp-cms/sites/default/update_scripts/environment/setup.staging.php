<?php

/**
 * @file: SETUP ENVIRONMENT: (content) staging
 * @desc: This script can be used to setup content staging environments.
 *
 * This is an update script for setting up content STAGING environments. The
 * script should run automatically at the end of the build process.
 */

// Save a watchdog entry for this build.
watchdog('us-environment', 'Setup STAGING Environment');

// Include the preprod setup script.
include $script_directory . '/setup.preprod.php';
