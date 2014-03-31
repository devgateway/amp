<?php

/**
 * Migrate 'projects_search_result' configuration to match the new field names.
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
  'organisation_names' => array(
    'show' => TRUE,
    'label' => t('Donors'),
  ),
  'organisation_names_1' => array(
    'show' => TRUE,
    'label' => t('Beneficiary Agencies'),
  ),
  'sector_names' => array(
    'show' => TRUE,
    'label' => t('Sectors'),
  ),
  'sector_names_1' => array(
    'show' => FALSE,
    'label' => t('Primary sectors'),
  ),
  'sector_names_2' => array(
    'show' => FALSE,
    'label' => t('Secondary sectors'),
  ),
  'program_names' => array(
    'show' => FALSE,
    'label' => t('Programs'),
  ),
  'location_names' => array(
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
  // 'organisation_names' => 'amp_org_name_agg', // required if current build included also update.0096.php
  'organisation_names' => 'organisation_role',
  'organisation_names_1' => 'organisation_role_1',
  'sector_names' => 'amp_sector_name_agg',
  'sector_names_1' => 'amp_primarysector_name_agg',
  'sector_names_2' => 'amp_secondarysector_name_agg',
  'program_names' => 'amp_theme_name_agg',
  'location_names' => 'category_location_name_agg',
);

// Get a list of projects_search_result pane instances.
$results = db_query('SELECT * FROM panels_pane WHERE type = :type', array(':type' => 'projects_search_result'));
foreach ($results as $row) {
  // Load the pane object.
  $pane = ctools_export_unpack_object('panels_pane', $row);

  // Override old pane configuration settings.
  $old_conf = $pane->configuration;
  $pane->configuration = array();

  // Migrate array settings.
  foreach ($plugin_definition_defaults as $current_key => $value) {
    if (!is_array($value)) {
      continue;
    }

    $old_key = $current_key;
    if (array_key_exists($current_key, $views_field_map)) {
      $old_key = $views_field_map[$current_key];
    }

    // Move settings.
    if (isset($old_conf[$old_key])) {
      $pane->configuration[$current_key] = $old_conf[$old_key];
    }
    else if (isset($old_conf[$current_key])) {
      $pane->configuration[$current_key] = $old_conf[$current_key];
    }

    unset($old_conf[$old_key]);
    unset($old_conf[$current_key]);
  }

  // Move old configuration that is not for the search view.
  if (!empty($old_conf)) {
    foreach ($old_conf as $current_key => $value) {
      $pane->configuration[$current_key] = $value;
    }
  }

  // Save the updated pane configuration.
  drupal_write_record('panels_pane', $pane, array('pid'));
}
