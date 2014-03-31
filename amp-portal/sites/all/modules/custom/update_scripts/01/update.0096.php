<?php

/**
 * Migrate 'projects_search_result' configuration to the updated format.
 */

ctools_include('export');
ctools_include('plugins');

// Plugin definition might have changed before this script was ran.
$plugin_definition_defaults = array(
  'show_info_column' => TRUE,
  'show_status_column' => FALSE,
  'show_export_links' => FALSE,
  'name' => array(
    'show' => TRUE,
    'label' => t('Project title'),
  ),
  'organisation_role' => array(
    'show' => TRUE,
    'label' => t('Donors'),
  ),
  'organisation_role_1' => array(
    'show' => TRUE,
    'label' => t('Beneficiary Agencies'),
  ),
  'amp_sector_name_agg' => array(
    'show' => TRUE,
    'label' => t('Sectors'),
  ),
  'amp_primarysector_name_agg' => array(
    'show' => FALSE,
    'label' => t('Primary sectors'),
  ),
  'amp_secondarysector_name_agg' => array(
    'show' => FALSE,
    'label' => t('Secondary sectors'),
  ),
  'amp_theme_name_agg' => array(
    'show' => FALSE,
    'label' => t('Programs'),
  ),
  'category_location_name_agg' => array(
    'show' => TRUE,
    'label' => t('Locations'),
  ),
  'actual_start_date' => array(
    'show' => TRUE,
    'label' => t('Start date'),
  ),
  'actual_completion_date' => array(
    'show' => TRUE,
    'label' => t('End date'),
  ),
  'commitment_amount' => array(
    'show' => TRUE,
    'label' => t('Commitment amount'),
  ),
  'disbursement_amount' => array(
    'show' => TRUE,
    'label' => t('Commitment amount'),
  ),
);

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
      $pane->configuration['show_info_column'] = $plugin_definition_defaults['show_info_column'];
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
      $pane->configuration['show_export_links'] = $plugin_definition_defaults['show_export_links'];
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
    $pane->configuration['show_export_links'] = $plugin_definition_defaults['show_export_links'];
    unset($old_conf['show_export_links']);
  }

  // Migrate array settings.
  foreach ($plugin_definition_defaults as $current_key => $value) {
    if (!is_array($value)) {
      continue;
    }

    $old_key = $current_key;
    if (array_key_exists($current_key, $views_field_map)) {
      $old_key = $views_field_map[$current_key];
    }

    // Move flag.
    if (isset($old_conf[$old_key . '_flag'])) {
      $pane->configuration[$current_key]['show'] = (int) $old_conf[$old_key . '_flag'];
    }
    else if (isset($old_conf[$current_key]) && is_array($old_conf[$current_key])) {
      $pane->configuration[$current_key]['show'] = $old_conf[$current_key]['show'];
    }
    else {
      $pane->configuration[$current_key]['show'] = $value['show'];
    }

    // Move label.
    if (isset($old_conf[$old_key . '_label'])) {
      $pane->configuration[$current_key]['label'] = $old_conf[$old_key . '_label'];
    }
    else if (isset($old_conf[$current_key]) && is_array($old_conf[$current_key])) {
      $pane->configuration[$current_key]['label'] = $old_conf[$current_key]['label'];
    }
    else {
      $pane->configuration[$current_key]['label'] = $value['label'];
    }

    unset($old_conf[$old_key . '_flag']);
    unset($old_conf[$old_key . '_label']);
    unset($old_conf[$old_key]);
    unset($old_conf[$current_key]);
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
