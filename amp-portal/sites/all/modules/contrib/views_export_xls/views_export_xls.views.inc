<?php
/**
 * @file
 * Views include file with views hooks.
 */

/**
 * Implements hook_views_plugins().
 */
function views_export_xls_views_plugins() {
  $path = drupal_get_path('module', 'views_export_xls');
  return array(
    'style' => array(
      'parent' => array(
        // this isn't really a display but is necessary so the file can
        // be included.
        'no ui' => TRUE,
        'handler' => 'views_plugin_style',
        'parent' => '',
      ),
      'xls' => array(
        'title' => t('XLS Feed'),
        'help' => t('Generates an XLS feed from a view.'),
        'handler' => 'views_export_xls_plugin_style_xls',
        'path' => $path . '/views',
        'theme path' => $path,
        'theme' => 'views_export_xls_view_xls',
        'uses fields' => TRUE,
        'uses row plugin' => FALSE,
        'uses options' => TRUE,
        'type' => 'feed',
      ),
    ),
  );
}

