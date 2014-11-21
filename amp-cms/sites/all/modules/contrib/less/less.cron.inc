<?php

/**
 * @file
 * Contains functions related to cron for LESS.
 */

/**
 * Implements hook_cron_queue_info().
 * 
 * This hook runs before cache flush during cron. Reliably lets us know if its
 * cron or not.
 */
function less_cron_queue_info() {
  drupal_static('less_cron', TRUE);
}

/**
 * Implements hook_cron().
 * 
 * Removes all stale compiled css files that are no longer in use.
 */
function less_cron() {
  $less_dir = _less_get_dir();
  
  $file_scan_options = array(
    // Adding current dir to excludes.
    'nomask' => '/(\.\.?|CVS|' . preg_quote($less_dir) . ')$/',
    'recurse' => FALSE,
  );
  $found_files = file_scan_directory('public://less', '/^.+$/', $file_scan_options);

  foreach ($found_files as $found_file) {
    file_unmanaged_delete_recursive($found_file->uri);
  }
}
