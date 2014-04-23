<?php

/**
 * Update the 'query' pane configuration.
 */

ctools_include('export');
ctools_include('plugins');

// Get a list of projects_search_result pane instances.
$results = db_query('SELECT * FROM panels_pane WHERE type = :type', array(':type' => 'query'));
foreach ($results as $row) {
  // Load the pane object.
  $pane = ctools_export_unpack_object('panels_pane', $row);

  // Override old pane configuration settings.
  $old_conf = $pane->configuration;
  $pane->configuration = array();

  // Move rename '!iso2' token settings.
  if (isset($old_conf['where'])) {
    $old_conf['where'] = preg_replace("/(.+)\ [\"']!iso2[\"'](.*)/", "$1 :iso2 $2", $old_conf['where']);
  }

  // Move old configuration that has not changed.
  if (!empty($old_conf)) {
    foreach ($old_conf as $current_key => $default_value) {
      $pane->configuration[$current_key] = $default_value;
    }
  }

  // Save the updated pane configuration.
  drupal_write_record('panels_pane', $pane, array('pid'));
}
