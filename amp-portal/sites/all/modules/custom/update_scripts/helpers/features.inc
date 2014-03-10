<?php

/**
 * @file
 * Update Scripts helper functions for the Features module.
 */

/**
 * Install features.
 */
function _us_install_features($feature_names) {
  static $modules_refreshed;
  if (update_scripts_php_from_command_line() && !$modules_refreshed) {
    module_list(TRUE, FALSE, TRUE);
    $modules_refreshed = TRUE;
  }

  foreach ($feature_names as $feature_name) {
    $feature_name = strtolower(trim($feature_name));

    // Sanity check
    if (module_exists($feature_name)) {
      $message = 'Feature "' . $feature_name . '" already enabled.';
      watchdog('debug-update-script', $message);
      continue;
    }

    // Load the feature.
    $feature = feature_load($feature_name, TRUE);

    // Sanity check.
    if (!$feature) {
      $message = 'Feature "' . $feature_name . '" was not found, thus cannot be enabled.';
      watchdog('debug-update-script', $message);
      continue;
    }

    // Include helper functions.
    module_load_include('inc', 'features', 'features.export');

    // Determine dependencies
    $dependencies = $feature->info['dependencies'];
    $dependencies = _features_export_minimize_dependencies($dependencies, $feature_name);

    // Install and enable dependencies.
    module_enable($dependencies, TRUE);
    _us_install_module($feature_name);
  }
}

/**
 * Install feature.
 */
function _us_install_feature($feature_name) {
  _us_install_features(array($feature_name));
}

/**
 * Revert features.
 */
function _us_revert_features($feature_names) {
  if (update_scripts_php_from_command_line()) {
    module_list(TRUE, FALSE, TRUE);
  }

  foreach ($feature_names as $feature_name) {
    $feature_name = strtolower(trim($feature_name));
    // Load the feature.
    $feature = feature_load($feature_name, TRUE);

    // Sanity check.
    if (!$feature) {
      $message = 'Feature "' . $feature_name . '" not found, thus cannot be reverted.';
      watchdog('debug-update-script', $message);
      continue;
    }
    elseif (!module_exists($feature_name)) {
      $message = 'Feature "' . $feature_name . '" not enabled, thus cannot be reverted.';
      watchdog('debug-update-script', $message);
      continue;
    }

    // Include helper functions.
    module_load_include('inc', 'features', 'features.export');

    // Find all overridden components.
    $overrides = features_detect_overrides($feature);
    $revert[$feature_name] = array_keys($overrides);

    // Revert all components.
    features_revert($revert);
  }
}

/**
 * Revert feature.
 */
function _us_revert_feature($feature_name) {
  _us_revert_features(array($feature_name));
}

/**
 * Uninstall features.
 */
function _us_uninstall_features($feature_names) {

}

/**
 * Uninstall feature.
 */
function _us_uninstall_feature($feature_name) {
  _us_uninstall_features(array($feature_name));
}

