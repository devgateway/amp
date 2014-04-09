<?php

/**
 * Update the 'projects_search_form' configuration.
 */

ctools_include('export');
ctools_include('plugins');

// Plugin definition might have changed before this script was ran.
$plugin_definition_defaults = array(
  'search_path' => 'search',
  'period_flag' => FALSE,
  'budget_flag' => FALSE,
  'country_level_projects_flag' => FALSE,
  'preview_amount_flag' => FALSE,
  'activity_status_flag' => FALSE,

  'sectors' => array(
    'show' => TRUE,
    'label' => 'Sectors',
    'popup' => FALSE,
  ),
  'sectors_primary' => array(
    'show' => FALSE,
    'label' => 'Primary Sectors',
    'popup' => FALSE,
  ),
  'sectors_secondary' => array(
    'show' => FALSE,
    'label' => 'Secondary Sectors',
    'popup' => FALSE,
  ),
  'programs' => array(
    'show' => FALSE,
    'label' => 'Programs',
    'popup' => FALSE,
  ),
  'locations' => array(
    'show' => TRUE,
    'label' => 'Locations',
    'popup' => FALSE,
  ),
  'organisations_dn' => array(
    'show' => TRUE,
    'label' => 'Donors',
    'popup' => FALSE,
  ),
  'organisations_ba' => array(
    'show' => FALSE,
    'label' => 'Beneficiary Agency',
    'popup' => FALSE,
  ),
  'keywords' => array(
    'show' => TRUE,
    'label' => 'Keywords',
    // 'popup' => FALSE,
  ),
);

// An array of old field names with the new field names as key.
$form_field_names_map = array(
  'sectors_primary' => 'primary_sectors',
  'sectors_secondary' => 'secondary_sectors',
  'organisations_dn' => 'donors',
  'organisations_ba' => 'agencies',
);

// Get a list of projects_search_result pane instances.
$results = db_query('SELECT * FROM panels_pane WHERE type = :type', array(':type' => 'projects_search_form'));
foreach ($results as $row) {
  // Load the pane object.
  $pane = ctools_export_unpack_object('panels_pane', $row);

  // Override old pane configuration settings.
  $old_conf = $pane->configuration;
  $pane->configuration = array();

  // Move 'search_path' settings.
  if (isset($old_conf['search_path'])) {
    $pane->configuration['search_path'] = $old_conf['search_path'];
    unset($old_conf['search_path']);
  }
  else {
    $pane->configuration['search_path'] = $plugin_definition_defaults['search_path'];
    unset($old_conf['search_path']);
  }

  // Move 'preview_amount_flag' settings.
  if (isset($old_conf['preview_amount'])) {
    $pane->configuration['preview_amount_flag'] = (bool) $old_conf['preview_amount'];
    unset($old_conf['preview_amount']);
  }
  else if (isset($old_conf['preview_amount_flag'])) {
    $pane->configuration['preview_amount_flag'] = (bool) $old_conf['preview_amount_flag'];
    unset($old_conf['preview_amount_flag']);
  }
  else {
    $pane->configuration['preview_amount_flag'] = $plugin_definition_defaults['preview_amount_flag'];
  }

  // Move 'activity_status_flag' settings.
  if (isset($old_conf['activity_status_flag'])) {
    $pane->configuration['activity_status_flag'] = (bool) $old_conf['activity_status_flag'];
    unset($old_conf['activity_status_flag']);
  }
  else {
    $pane->configuration['activity_status_flag'] = $plugin_definition_defaults['activity_status_flag'];
  }

  // Migrate boolean settings.
  foreach ($plugin_definition_defaults as $current_key => $default_value) {
    if (is_array($default_value) || $current_key == 'search_path') {
      continue;
    }

    $old_key = $current_key;
    if (array_key_exists($current_key, $form_field_names_map)) {
      $old_key = $form_field_names_map[$current_key];
    }

    // Move and convert settings.
    if (isset($old_conf[$old_key])) {
      $pane->configuration[$current_key] = (bool) $old_conf[$old_key];
      unset($old_conf[$old_key]);
    }
    else if (isset($old_conf[$current_key])) {
      $pane->configuration[$current_key] = (bool) $old_conf[$current_key];
      unset($old_conf[$current_key]);
    }
    else {
      $pane->configuration[$current_key] = $plugin_definition_defaults[$current_key];
    }
  }

  // Migrate array settings.
  foreach ($plugin_definition_defaults as $current_key => $default_value) {
    if (!is_array($default_value)) {
      continue;
    }

    $old_key = $current_key;
    if (array_key_exists($current_key, $form_field_names_map)) {
      $old_key = $form_field_names_map[$current_key];
    }

    // Move flag.
    if (isset($old_conf[$old_key . '_flag'])) {
      $pane->configuration[$current_key]['show'] = (bool) $old_conf[$old_key . '_flag'];
    }
    else if (isset($old_conf[$old_key]) && is_array($old_conf[$old_key])) {
      $pane->configuration[$current_key]['show'] = (bool) $old_conf[$old_key]['show'];
    }
    else {
      $pane->configuration[$current_key]['show'] = $default_value['show'];
    }

    // Move label.
    if (isset($old_conf[$old_key . '_label'])) {
      $pane->configuration[$current_key]['label'] = $old_conf[$old_key . '_label'];
    }
    else if (isset($old_conf[$old_key]) && is_array($old_conf[$old_key])) {
      $pane->configuration[$current_key]['label'] = $old_conf[$old_key]['label'];
    }
    else {
      $pane->configuration[$current_key]['label'] = $default_value['label'];
    }

    // Move popup if fields supports it.
    if (isset($old_conf[$old_key . '_popup'])) {
      $pane->configuration[$current_key]['popup'] = (bool) $old_conf[$old_key . '_popup'];
    }
    else if (isset($old_conf[$old_key]) && is_array($old_conf[$old_key])) {
      if (isset($old_conf[$old_key]['popup'])) {
        $pane->configuration[$current_key]['popup'] = (bool) $old_conf[$old_key]['popup'];
      }
    }
    else if (isset($default_value['popup'])) {
      $pane->configuration[$current_key]['popup'] = $default_value['popup'];
    }

    unset($old_conf[$old_key . '_flag']);
    unset($old_conf[$current_key . '_flag']);
    unset($old_conf[$old_key . '_label']);
    unset($old_conf[$current_key . '_label']);
    unset($old_conf[$old_key . '_popup']);
    unset($old_conf[$current_key . '_popup']);
    unset($old_conf[$old_key]);
    unset($old_conf[$current_key]);
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
