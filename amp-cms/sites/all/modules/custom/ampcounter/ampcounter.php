<?php

/**
 * @file
 * This file updates the site counter.
 */

/**
 * Root directory of Drupal installation.
 */
$module_path = getcwd();
// Assume that the module folder is located in 'sites/all/modules/custom', and set the drupal root path.
define('DRUPAL_ROOT', realpath($module_path . '/../../../../..'));
chdir(DRUPAL_ROOT);

// Initialize the database system.
require_once DRUPAL_ROOT . '/includes/bootstrap.inc';
drupal_bootstrap(DRUPAL_BOOTSTRAP_VARIABLES);

// Get the last update timestamp, if any.
define('AMPCOUNTER_LAST_UPDATE', variable_get('ampcounter_last_update', NULL));

// How ofter the counter should be incremented.
define('AMPCOUNTER_UPDATE_FREQUENCY', variable_get('ampcounter_update_frequency', 1));

// Only log once every 10 seconds
if (is_null(AMPCOUNTER_LAST_UPDATE) || (REQUEST_TIME - (int) AMPCOUNTER_LAST_UPDATE) > AMPCOUNTER_UPDATE_FREQUENCY) {
  // Update the last update timestamp.
  variable_set('ampcounter_last_update', REQUEST_TIME);

  // Increment the site counter.
  $counter_value = variable_get('ampcounter_value', 0);
  $counter_value++;
  variable_set('ampcounter_value', $counter_value);
}

// Return a dummy image.
$image = imagecreatefromgif($module_path . '/spacer.gif');
header('Content-Type: image/gif');
imagegif($image);
imagedestroy($image);
exit;
