<?php

/**
 * Migrate 'projects_search_result' configuration to the updated format.
 */

ctools_include('export');
ctools_include('plugins');

// Get plugin defaults.
$plugin_definition = ctools_get_plugins('ctools', 'content_types', 'projects_search_result');

// An array of old view fields with the new field as key.
$views_field_map = array(
  'organisation_role' => 'amp_org_name_agg',
);

// Get a list of projects_search_result pane instances.
$results = db_query('SELECT * FROM panels_pane WHERE type = :type', array(':type' => 'projects_search_result'));
foreach ($results as $row) {
  // Load the pane object.
  $pane = ctools_export_unpack_object('panels_pane', $row);

  // Override old pane configuration settings.
  $old_conf = $pane->configuration;
  $pane->configuration = array();

  // Move 'hide_first_column' settings to 'show_info_column'.
  if (isset($old_conf['hide_first_column'])) {
    if (empty($old_conf['hide_first_column'])) {
      $pane->configuration['show_info_column'] = 1;
    }
    else {
      $pane->configuration['show_info_column'] = 0;
    }
    unset($old_conf['hide_first_column']);
  }
  else {
    if (isset($old_conf['show_info_column'])) {
      $pane->configuration['show_info_column'] = $old_conf['show_info_column'];
    }
    else {
      $pane->configuration['show_info_column'] = $plugin_definition['defaults']['show_info_column'];
    }
    unset($old_conf['show_info_column']);
  }

  // Move 'allowxlsexport' settings to 'show_export_links'.
  if (isset($old_conf['allowxlsexport'])) {
    if (empty($old_conf['allowxlsexport'])) {
      $pane->configuration['show_export_links'] = 0;
    }
    else {
      $pane->configuration['show_export_links'] = 1;
    }
    unset($old_conf['allowxlsexport']);
  }
  else {
    if (isset($old_conf['show_export_links'])) {
      $pane->configuration['show_export_links'] = $old_conf['show_export_links'];
    }
    else {
      $pane->configuration['show_export_links'] = $plugin_definition['defaults']['show_export_links'];
    }
    unset($old_conf['show_export_links']);
  }

  // Copy 'show_status_column' settings.
  if (isset($old_conf['show_status_column'])) {
    if (empty($old_conf['show_status_column'])) {
      $pane->configuration['show_status_column'] = 0;
    }
    else {
      $pane->configuration['show_status_column'] = 1;
    }
    unset($old_conf['show_status_column']);
  }
  else {
    $pane->configuration['show_export_links'] = $plugin_definition['defaults']['show_export_links'];
    unset($old_conf['show_export_links']);
  }

  // Migrate array settings.
  foreach ($plugin_definition['defaults'] as $key => $value) {
    if (!is_array($value)) {
      continue;
    }

    $from_key = $key;
    if (array_key_exists($key, $views_field_map)) {
      $from_key = $views_field_map[$key];
    }

    // Move flag.
    if (isset($old_conf[$from_key . '_flag'])) {
      $pane->configuration[$key]['show'] = (int) $old_conf[$from_key . '_flag'];
    }
    else if (isset($old_conf[$key]) && is_array($old_conf[$key])) {
      $pane->configuration[$key]['show'] = $old_conf[$key]['show'];
    }
    else {
      $pane->configuration[$key]['show'] = $value['show'];
    }

    // Move label.
    if (isset($old_conf[$from_key . '_label'])) {
      $pane->configuration[$key]['label'] = $old_conf[$from_key . '_label'];
    }
    else if (isset($old_conf[$key]) && is_array($old_conf[$key])) {
      $pane->configuration[$key]['label'] = $old_conf[$key]['label'];
    }
    else {
      $pane->configuration[$key]['label'] = $value['label'];
    }

    unset($old_conf[$from_key . '_flag']);
    unset($old_conf[$from_key . '_label']);
    unset($old_conf[$from_key]);
    unset($old_conf[$key]);
  }

  // Move old configuration that is not for the search view.
  if (!empty($old_conf)) {
    foreach ($old_conf as $key => $value) {
      $pane->configuration[$key] = $value;
    }
  }

  // Save the updated pane configuration.
  drupal_write_record('panels_pane', $pane, array('pid'));
}
