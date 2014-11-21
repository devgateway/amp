<?php

/**
 * @file
 * Update Scripts helper functions for the Views module.
 */

/**
 * Enable view.
 */
function _us_views__enable($view_names, $op = 'enable') {
  if (is_string($view_names)) {
    $view_names = array($view_names);
  }

  ctools_include('export-ui');
  foreach ($view_names as $view_name) {
    if ($view = views_get_view($view_name)) {
      // Load the plugin information.
      $plugin = ctools_get_export_ui('views_ui');
      $handler = ctools_export_ui_get_handler($plugin);

      if (!empty($handler->plugin['schema'])) {
        switch ($op) {
          case 'enable':
            ctools_export_crud_enable($handler->plugin['schema'], $view);
            break;

          case 'disable':
          default:
            ctools_export_crud_disable($handler->plugin['schema'], $view);
            break;
        }
      }
    }
  }
}

/**
 * Disable view.
 */
function _us_views__disable($view_names) {
  _us_views__enable($view_names, 'disable');
}
