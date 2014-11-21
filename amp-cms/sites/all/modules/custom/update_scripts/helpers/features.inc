<?php

/**
 * @file
 * Update Scripts helper functions for the Features module.
 */

/**
 * Install a list of features.
 */
function _us_features__install($feature_names) {
  static $modules_refreshed;

  if (is_string($feature_names)) {
    $feature_names = array($feature_names);
  }

  if (!$modules_refreshed) {
    module_list(TRUE, FALSE, TRUE);
    $modules_refreshed = TRUE;
  }

  foreach ($feature_names as $feature_name) {
    $feature_name = strtolower(trim($feature_name));

    // Sanity check.
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

    // Determine dependencies.
    $dependencies = $feature->info['dependencies'];
    $dependencies = _features_export_minimize_dependencies($dependencies, $feature_name);

    // Install and enable dependencies.
    module_enable($dependencies, TRUE);
    _us_module__install($feature_name);
  }
}

/**
 * Disable a list of features.
 */
function _us_features__disable($feature_names) {
  _us_module__install($feature_names, 'disable');
}

/**
 * Revert a list of features.
 */
function _us_features__revert($feature_names) {
  if (is_string($feature_names)) {
    $feature_names = array($feature_names);
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
 * Revert all enabled features to their definitions in code.
 */
function _us_features__revert_all($force = FALSE, $features_to_exclude = array()) {
  module_load_include('inc', 'features', 'features.export');

  $features_to_revert = array();
  foreach (features_get_features(NULL, TRUE) as $module) {
    if ($module->status && !in_array($module->name, $features_to_exclude)) {
      // If forced, add module regardless of status.
      if ($force) {
        $features_to_revert[] = $module->name;
      }
      else {
        switch (features_get_storage($module->name)) {
          case FEATURES_OVERRIDDEN:
          case FEATURES_NEEDS_REVIEW:
          case FEATURES_REBUILDABLE:
            $features_to_revert[] = $module->name;
            break;
        }
      }
    }
  }

  if ($features_to_revert) {
    _us_features__revert($features_to_revert);
    $message = 'Features revert: The following modules have been reverted: !modules';
    watchdog('debug-update-script', $message, array('!modules' => implode(', ', $features_to_revert)));
  }
  else {
    $message = 'Features revert: current state already matches defaults, aborting.';
    watchdog('debug-update-script', $message);
  }
}
